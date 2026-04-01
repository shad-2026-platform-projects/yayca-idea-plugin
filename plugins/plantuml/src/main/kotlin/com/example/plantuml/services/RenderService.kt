package com.example.plantuml.services

import com.example.plantuml.renderer.PlantUmlRenderer
import java.util.concurrent.Executors
import javax.swing.Timer

class RenderService(
    private val onSuccess: (java.awt.image.BufferedImage) -> Unit,
    private val onError: (String) -> Unit
) {

    private val executor = Executors.newSingleThreadExecutor()
    private var debounceTimer: Timer? = null

    fun scheduleRender(content: String) {
        debounceTimer?.stop()

        debounceTimer = Timer(400) {
            render(content)
        }.apply {
            isRepeats = false
            start()
        }
    }

    private fun render(content: String) {
        executor.submit {
            try {
                val plantUml = GraphService.buildPlantUml(content)
                val image = PlantUmlRenderer.renderDiagram(plantUml)

                onSuccess(image)
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun dispose() {
        debounceTimer?.stop()
        executor.shutdownNow()
    }
}