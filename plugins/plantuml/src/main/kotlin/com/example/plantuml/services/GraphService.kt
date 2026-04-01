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

            for (node in depMap.keys) {
                append("component \"$node\" as $node\n")
            }

            for ((source, targets) in depMap) {
                for (target in targets) {
                    append("$source --> $target\n")
                }
            }

            append("@enduml")
        }
    }
}