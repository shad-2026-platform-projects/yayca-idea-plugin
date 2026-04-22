package com.example.plantuml.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Flow(
    val jobs: Map<String, Job> = emptyMap()
)