package com.example.plantuml

import com.example.plantuml.services.RenderService
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.JBSplitter
import java.awt.BorderLayout
import java.awt.Dimension
import java.beans.PropertyChangeListener
import javax.swing.*

class YamlGraphFileEditor(
    private val project: Project,
    private val myFile: VirtualFile
) : UserDataHolderBase(), FileEditor {

    private val panel = JPanel(BorderLayout())
    private val splitter = JBSplitter(true, 0.5f)

    private val document =
        FileDocumentManager.getInstance().getDocument(myFile)
            ?: error("Cannot get document for file")

    private val editor = EditorFactory.getInstance().createEditor(document, project)

    private val imageLabel = JLabel()
    private val scrollPane = JScrollPane(imageLabel)
    private val rightPanel = JPanel(BorderLayout())

    private val renderService = RenderService(
        onSuccess = { image -> updateUIImage(image) },
        onError = { msg -> updateUIError(msg) }
    )

    private val documentListener = object : DocumentListener {
        override fun documentChanged(event: DocumentEvent) {
            renderService.scheduleRender(document.text)
        }
    }

    init {
        document.addDocumentListener(documentListener)

        editor.settings.apply {
            isLineNumbersShown = true
            isLineMarkerAreaShown = true
            isFoldingOutlineShown = true
            isIndentGuidesShown = true
        }

        rightPanel.add(scrollPane, BorderLayout.CENTER)

        splitter.apply {
            firstComponent = editor.component
            secondComponent = rightPanel
            isShowDividerControls = true
            proportion = 0.5f
        }

        splitter.firstComponent.minimumSize = Dimension(200, 0)
        splitter.secondComponent.minimumSize = Dimension(200, 0)

        panel.add(splitter, BorderLayout.CENTER)

        renderService.scheduleRender(document.text)
    }

    private fun updateUIImage(image: java.awt.image.BufferedImage) {
        ApplicationManager.getApplication().invokeLater {
            imageLabel.icon = ImageIcon(image)
            imageLabel.text = null
        }
    }

    private fun updateUIError(message: String) {
        ApplicationManager.getApplication().invokeLater {
            imageLabel.icon = null
            imageLabel.text = message
            imageLabel.horizontalAlignment = SwingConstants.CENTER
        }
    }

    override fun getComponent(): JComponent = panel

    override fun getPreferredFocusedComponent(): JComponent? = editor.component

    override fun getName(): String = "YAML Split Editor"

    override fun setState(state: FileEditorState) {}

    override fun isModified(): Boolean = false

    override fun isValid(): Boolean = true

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {}

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {}

    override fun getFile(): VirtualFile = myFile

    override fun dispose() {
        document.removeDocumentListener(documentListener)
        EditorFactory.getInstance().releaseEditor(editor)
        renderService.dispose()
    }

    override fun selectNotify() {}
    override fun deselectNotify() {}
}