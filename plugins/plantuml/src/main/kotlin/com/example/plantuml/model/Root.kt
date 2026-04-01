package com.example.plantuml.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Root(
    val flows: Map<String, Flow> = emptyMap()
)