package com.example.habittracker.presentation.web.styles

import kotlinx.css.CssBuilder

internal fun CssBuilder.pageLayoutStyles() {
    css("main") {
        put("margin", "0 auto")
        put("max-width", "1180px")
        put("padding", "40px")
    }
    css(".hero, .page-heading, .auth-page") {
        put("margin-bottom", "28px")
    }
    css(".home-hero") {
        put("align-items", "stretch")
        put("display", "grid")
        put("gap", "28px")
        put("grid-template-columns", "minmax(0, 1.3fr) minmax(320px, 0.7fr)")
    }
    css(".hero-copy") {
        put("padding", "56px")
    }
    css(".hero-panel, .form-panel, .auth-card") {
        put("padding", "28px")
    }
    css(".hero-copy p, .page-heading p, .auth-copy p") {
        put("color", "var(--muted)")
        put("max-width", "640px")
    }
}

internal fun CssBuilder.cardStyles() {
    css(".hero-copy, .hero-panel, .form-panel, .auth-card, .suggestion-card, .habit-card, .stat-card, .leaderboard") {
        put("background", "rgba(255, 255, 255, 0.88)")
        put("border", "1px solid var(--line)")
        put("border-radius", "8px")
        put("box-shadow", "var(--shadow)")
    }
    css(".hero-copy, .stat-card:nth-child(2n), .suggestion-card:nth-child(2n), .habit-card:nth-child(2n)") {
        put("background", "rgba(255, 251, 235, 0.9)")
    }
    css(".eyebrow, .category-chip, .stat-label, .muted") {
        put("color", "var(--muted)")
    }
    css(".eyebrow, .category-chip, .stat-label") {
        put("font-size", "13px")
        put("font-weight", "800")
        put("text-transform", "uppercase")
    }
    css(".mini-habit, .habit-card-header, .leaderboard-row") {
        put("align-items", "center")
        put("display", "flex")
        put("justify-content", "space-between")
    }
    css(".mini-habit") {
        put("border-top", "1px solid var(--line)")
        put("padding", "16px 0")
    }
    css(".stats-grid") {
        put("display", "grid")
        put("gap", "16px")
        put("grid-template-columns", "repeat(4, minmax(0, 1fr))")
        put("margin-bottom", "28px")
    }
    css(".stat-card") {
        put("padding", "20px")
    }
    css(".stat-value") {
        put("color", "var(--surface-strong)")
        put("display", "block")
        put("font-size", "30px")
        put("font-weight", "800")
        put("margin-top", "8px")
    }
    css(".stat-detail") {
        put("color", "var(--muted)")
        put("font-size", "14px")
    }
}

internal fun CssBuilder.formStyles() {
    css(".auth-page") {
        put("align-items", "start")
        put("display", "grid")
        put("gap", "36px")
        put("grid-template-columns", "1fr 420px")
    }
    css("label") {
        put("color", "var(--surface-strong)")
        put("display", "grid")
        put("font-weight", "700")
        put("gap", "8px")
        put("margin-bottom", "16px")
    }
    css("input, select, textarea") {
        put("border", "1px solid var(--line)")
        put("border-radius", "8px")
        put("color", "var(--text)")
        put("min-height", "46px")
        put("padding", "10px 12px")
        put("width", "100%")
    }
    css("textarea") {
        put("resize", "vertical")
    }
    css(".form-error") {
        put("background", "#fee2e2")
        put("border-radius", "8px")
        put("color", "#991b1b")
        put("font-weight", "700")
        put("padding", "10px 12px")
    }
    css(".auth-switch") {
        put("align-items", "center")
        put("background", "rgba(255, 251, 235, 0.86)")
        put("border", "1px solid var(--line)")
        put("border-radius", "8px")
        put("display", "inline-flex")
        put("gap", "6px")
        put("padding", "10px 12px")
    }
    css(".auth-switch a") {
        put("color", "var(--primary-dark)")
        put("text-decoration", "underline")
        put("text-decoration-thickness", "2px")
        put("text-underline-offset", "3px")
    }
    css(".habit-place-grid") {
        put("display", "grid")
        put("gap", "24px")
        put("grid-template-columns", "360px 1fr")
    }
}
