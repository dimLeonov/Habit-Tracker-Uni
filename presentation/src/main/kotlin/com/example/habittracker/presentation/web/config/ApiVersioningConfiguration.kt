
package com.example.habittracker.presentation.web.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

const val API_VERSION_HEADER = "API-Version"
const val API_VERSION_1 = "1.0"
const val API_VERSION_2 = "2.0"

@Configuration
class ApiVersioningConfiguration : WebMvcConfigurer {
    override fun configureApiVersioning(configurer: ApiVersionConfigurer) {
        configurer
            .useRequestHeader(API_VERSION_HEADER)
            .addSupportedVersions(API_VERSION_1, API_VERSION_2)
            .setDefaultVersion(API_VERSION_2)
            .setVersionRequired(false)
    }
}

