package com.example.plantuml

import com.example.plantuml.model.Root
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.intellij.openapi.diagnostic.Logger

object DependencyGraphBuilder {
    private val log = Logger.getInstance(DependencyGraphBuilder::class.java)

    private val mapper = ObjectMapper(YAMLFactory())
        .registerKotlinModule()

    fun parseDependencies(yamlContent: String): Map<String, List<String>> {
        return try {
            val root: Root = mapper.readValue(yamlContent)

            root.flows.values
                .flatMap { it.jobs.entries }
                .associate { (jobName, job) ->
                    jobName to job.needs
                }

        } catch (e: Exception) {
            log.warn("Failed to parse YAML", e)
            emptyMap()
        }
    }
}