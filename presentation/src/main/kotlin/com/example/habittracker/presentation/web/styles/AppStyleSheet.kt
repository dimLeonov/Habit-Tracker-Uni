package com.example.habittracker.presentation.web.styles

import kotlinx.css.CssBuilder

fun appStyleSheet(): String {
    val styles = CssBuilder()
    styles.apply {
        designTokens()
        baseStyles()
        navigationStyles()
        buttonStyles()
        pageLayoutStyles()
        cardStyles()
        formStyles()
        habitStyles()
        calendarStyles()
        leaderboardStyles()
        responsiveStyles()
    }
    return styles.toString()
}

internal fun CssBuilder.css(selector: String, declarations: CssBuilder.() -> Unit) {
    rule(selector, declarations)
}
