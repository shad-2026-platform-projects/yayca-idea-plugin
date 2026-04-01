package com.example.plantuml

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class YamlGraphFileEditorProvider : FileEditorProvider, DumbAware {
    override fun accept(project: Project, file: VirtualFile): Boolean {
        val ext = file.extension
        return ext.equals("yaml", ignoreCase = true) || ext.equals("yml", ignoreCase = true)
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        return YamlGraphFileEditor(project, file)
    }

    override fun getEditorTypeId(): String = "yaml-graph-editor"

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR
}