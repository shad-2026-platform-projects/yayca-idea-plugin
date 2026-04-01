package com.example.plantuml.providers

import com.example.plantuml.editors.YamlGraphSplitEditor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class YamlGraphFileEditorProvider : FileEditorProvider, DumbAware {

    override fun accept(project: Project, file: VirtualFile): Boolean {
        val ext = file.extension?.lowercase()
        if (ext != "yaml" && ext != "yml") return false

        val document = FileDocumentManager.getInstance().getDocument(file) ?: return false
        val text = document.text.trimStart()

        return text.startsWith("flows:")
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        return YamlGraphSplitEditor(project, file)
    }

    override fun getEditorTypeId(): String = "yaml-graph-editor"

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR
}