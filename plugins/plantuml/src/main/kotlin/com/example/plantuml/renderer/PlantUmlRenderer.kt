package com.example.plantuml.renderer

import com.example.plantuml.settings.PlantUmlSettings
import com.intellij.openapi.diagnostic.logger
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicReference
import javax.imageio.ImageIO

object PlantUmlRenderer {

    private val LOG = logger<PlantUmlRenderer>()
    private val executor = Executors.newSingleThreadExecutor()
    private val taskFuture = AtomicReference<Future<*>>(null)

    fun dispose() {
        taskFuture.get()?.cancel(true)
        executor.shutdownNow()
    }

    fun renderDiagram(plantUmlSource: String): BufferedImage {
        val settings = PlantUmlSettings.getInstance()
        require(plantUmlSource.length <= settings.maxInputSize) {
            "PlantUML input too large: ${plantUmlSource.length}"
        }

        val future = executor.submit<BufferedImage> {
            val outputStream = ByteArrayOutputStream()
            val reader = SourceStringReader(plantUmlSource)

            reader.generateImage(outputStream, FileFormatOption(FileFormat.PNG))
                ?: throw IOException("Failed to generate diagram")

            val bytes = outputStream.toByteArray()
            ImageIO.read(ByteArrayInputStream(bytes))
                ?: throw IOException("Failed to decode generated image")
        }

        taskFuture.set(future)

        return try {
            future.get(settings.timeoutMs, TimeUnit.MILLISECONDS).also {
                LOG.debug("Diagram rendered successfully")
            }
        } catch (e: TimeoutException) {
            future.cancel(true)
            LOG.warn("PlantUML rendering timed out after ${settings.timeoutMs} ms")
            throw IOException("PlantUML rendering timed out", e)
        } catch (e: Exception) {
            LOG.error("PlantUML rendering failed", e)
            throw IOException("PlantUML rendering failed", e)
        } finally {
            taskFuture.set(null)
        }
    }
}