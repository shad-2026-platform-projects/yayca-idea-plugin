package com.example.plantuml.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "PlantUmlSettings",
    storages = [Storage("plantuml_settings.xml")]
)
@Service(Service.Level.APP)
class PlantUmlSettings : PersistentStateComponent<PlantUmlSettings> {

    var maxInputSize: Int = 100_000
    var timeoutMs: Long = 10000L

    override fun getState(): PlantUmlSettings = this

    override fun loadState(state: PlantUmlSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): PlantUmlSettings =
            ApplicationManager.getApplication().getService(PlantUmlSettings::class.java)
    }
}