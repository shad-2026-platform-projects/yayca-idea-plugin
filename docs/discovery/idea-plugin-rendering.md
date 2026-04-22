# IDEA Plugin Rendering Discovery

## Main idea

The goal is to implement diagram/graph rendering in an IntelliJ IDEA plugin using split view:

- left side — source text (YAML)
- right side — generated PNG diagram

The solution must:
- support realtime updates
- avoid blocking the UI thread
- work correctly during IDE indexing (Dumb Mode)

---

## Way to solution

The following approaches for UI integration in IntelliJ IDEA were explored:

### 1. Rendering approaches

- ToolWindow → not suitable (not file-bound)
- Editor-based UI → suitable

Chosen approach:
- using `TextEditorWithPreview`

---

### 2. Split view

IntelliJ provides a built-in mechanism:

```kotlin
TextEditorWithPreview(
    textEditor,
    previewEditor,
    "Preview"
)
```

This provides:

- split layout out of the box
- support for modes (editor / preview / both)

### 3. Split editor implementation

```kotlin
class YamlGraphSplitEditor(
    project: Project,
    file: VirtualFile
) : TextEditorWithPreview(

    TextEditorProvider.Companion.getInstance().createEditor(project, file) as TextEditor,

    PlantUmlPreviewEditor(project, file),

    "PlantUML Preview"
)
```

Architecture:
VirtualFile -> TextEditor (left) + PlantUmlPreviewEditor (right)

### 4. PNG rendering

The preview is implemented using Swing:

- BufferedImage
- JLabel + ImageIcon

### 5. Realtime updates
 - subscription to document changes (DocumentListener)
 - debounce/throttling — updates are triggered no more than once every N milliseconds
 - rendering is performed asynchronously

### 6. Indexing awareness (Dumb Mode)

During IntelliJ indexing (Dumb Mode):

- some APIs are unavailable
- heavy operations may cause issues

Solution:

use DumbAware interface or defer execution until indexing is finished

### 7. Alternatives

Custom split via Swing

- full control
- duplicates IntelliJ functionality

SVG instead of PNG

- better scalability
- more complex rendering
### 8. Sources

- IntelliJ Platform SDK
https://plugins.jetbrains.com/docs/intellij/welcome.html
- Editors API
https://plugins.jetbrains.com/docs/intellij/editors.html