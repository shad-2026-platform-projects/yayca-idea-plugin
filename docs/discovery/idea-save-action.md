# IntelliJ IDEA Save Actions Discovery

## Main idea

This document describes how to detect file save events in IntelliJ IDEA and trigger preview refresh in the PlantUML plugin.

Goal:

- detect when user saves a file
- trigger diagram re-render
- ensure stable and consistent preview state

---

## How save works in IntelliJ IDEA

IntelliJ IDEA does NOT provide a direct "onSave()" callback.

Instead, saving is handled via:

- Document persistence system
- FileDocumentManager lifecycle
- MessageBus events

---

## Recommended API

### FileDocumentManagerListener (MAIN APPROACH)

The correct way to detect save events is:

```kotlin id="listener_api"
interface FileDocumentManagerListener {

    fun beforeDocumentSaving(document: Document)

    fun beforeDocumentSaving(document: Document, file: VirtualFile)

    fun beforeAllDocumentsSaving()

    fun afterDocumentSaving(document: Document)
}
```
When to use which event
Recommended:
-afterDocumentSaving

Why:

- file is already persisted
- stable content
- safe for rendering

How to register listener

```kotlin
ApplicationManager.getApplication().messageBus
    .connect()
    .subscribe(FileDocumentManagerListener.TOPIC, object : FileDocumentManagerListener {

        override fun afterDocumentSaving(document: Document) {
            // trigger preview refresh here
        }
    })
```

Save refresh strategy for plugin
Hybrid approach (BEST PRACTICE)

Plugin should support both:

1. Realtime typing (existing)
   DocumentListener
   debounce rendering
2. Save trigger (NEW)
   force refresh on save

Sources:

- IntelliJ Platform SDK – File Editor and Documents
https://plugins.jetbrains.com/docs/intellij/editors.html
- IntelliJ Platform SDK – Message Bus
https://plugins.jetbrains.com/docs/intellij/messaging-infrastructure.html
- IntelliJ Platform SDK – FileDocumentManager
https://plugins.jetbrains.com/docs/intellij/documents.html
- IntelliJ Platform SDK – Virtual File System
https://plugins.jetbrains.com/docs/intellij/virtual-file-system.html
- IntelliJ Platform SDK – Plugin Development Guide
https://plugins.jetbrains.com/docs/intellij/welcome.html