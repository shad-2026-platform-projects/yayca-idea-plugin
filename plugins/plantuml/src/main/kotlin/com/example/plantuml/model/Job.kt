package com.example.plantuml.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Job(
    val needs: List<String> = emptyList()
)