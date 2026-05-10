package com.example.habittracker.presentation.web.controllers

import com.example.habittracker.presentation.web.styles.appStyleSheet
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AppStylesController {
    @GetMapping("/styles/app.css", produces = ["text/css"])
    fun appStyles(): String = appStyleSheet()
}
