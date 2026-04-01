package com.example.plantuml.editors

import com.example.plantuml.PlantUmlPreviewEditor
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.TextEditorWithPreview
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class YamlGraphSplitEditor(
    project: Project,
    file: VirtualFile
) : TextEditorWithPreview(

    TextEditorProvider.Companion.getInstance().createEditor(project, file) as TextEditor,

    PlantUmlPreviewEditor(project, file),

    "PlantUML Preview"
)