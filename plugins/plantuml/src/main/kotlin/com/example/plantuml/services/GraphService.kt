package com.example.plantuml.services

import com.example.plantuml.DependencyGraphBuilder

object GraphService {

    fun buildPlantUml(yamlContent: String): String {
        val depMap = DependencyGraphBuilder.parseDependencies(yamlContent)

        if (depMap.isEmpty()) {
            throw IllegalStateException("Graph is empty")
        }

        return buildString {
            append("@startuml\n")

            append("""
                skinparam componentStyle rectangle
                skinparam backgroundColor #FFFFFF
                skinparam component {
                    BackgroundColor #F5F5F5
                    BorderColor #2C3E50
                    FontColor #2C3E50
                    RoundCorner 10
                    Shadowing true
                }
                skinparam ArrowColor #34495E
                skinparam ArrowThickness 2
                skinparam defaultFontName Arial
                skinparam defaultFontSize 12
            """.trimIndent())

            append("\n\n")

            for (node in depMap.keys) {
                append("component \"$node\" as $node\n")
            }

            append("\n")

            for ((source, targets) in depMap) {
                for (target in targets) {
                    append("$source --> $target\n")
                }
            }

            append("\n@enduml")
        }
    }
}