package com.example.plantuml

import com.example.plantuml.services.RenderService
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.GridBagLayout
import java.awt.image.BufferedImage
import java.beans.PropertyChangeListener
import javax.swing.*

class PlantUmlPreviewEditor(
    private val project: Project,
    private val file: VirtualFile
) : UserDataHolderBase(), FileEditor {

    companion object {
        private const val EDITOR_NAME = "Preview"
        private const val NO_DOCUMENT_ERROR = "Document is null for file"
        private const val IMAGE_PADDING = 16
        private const val SCROLL_STEP = 16
    }

    private val panel = JPanel(BorderLayout())

    private val imageLabel = JLabel().apply {
        horizontalAlignment = JLabel.CENTER
        verticalAlignment = JLabel.CENTER
        border = JBUI.Borders.empty(IMAGE_PADDING)
        isOpaque = false
    }

    private val centerPanel = JPanel(GridBagLayout()).apply {
        background = JBColor.PanelBackground
        add(imageLabel)
    }

    private val scrollPane = JScrollPane(centerPanel).apply {
        border = null
        viewport.border = null
        horizontalScrollBar.unitIncrement = SCROLL_STEP
        verticalScrollBar.unitIncrement = SCROLL_STEP
    }

    private val document =
        FileDocumentManager.getInstance().getDocument(file)
            ?: throw IllegalStateException("$NO_DOCUMENT_ERROR: $file")

    private val renderService = RenderService(
        onSuccess = { updateImage(it) },
        onError = { updateError(it) }
    )

    private val listener = object : DocumentListener {
        override fun documentChanged(event: DocumentEvent) {
            renderService.scheduleRender(document.text)
        }
    }

    init {
        panel.background = JBColor.PanelBackground
        panel.add(scrollPane, BorderLayout.CENTER)

        document.addDocumentListener(listener)

        renderService.scheduleRender(document.text)
    }

    private fun updateImage(image: BufferedImage) {
        ApplicationManager.getApplication().invokeLater {
            imageLabel.icon = ImageIcon(image)
            imageLabel.text = null
        }
    }

    private fun updateError(message: String) {
        ApplicationManager.getApplication().invokeLater {
            imageLabel.icon = null
            imageLabel.text = message
            imageLabel.horizontalAlignment = SwingConstants.CENTER
        }
    }

    override fun getComponent(): JComponent = panel

    override fun getPreferredFocusedComponent(): JComponent? = null

    override fun getName(): String = EDITOR_NAME

    override fun setState(state: FileEditorState) {}

    override fun isModified(): Boolean = false

    override fun isValid(): Boolean = true

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {}

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {}

    override fun getFile(): VirtualFile = file

    override fun dispose() {
        document.removeDocumentListener(listener)
        renderService.dispose()
    }

    override fun selectNotify() {}

    override fun deselectNotify() {}
}