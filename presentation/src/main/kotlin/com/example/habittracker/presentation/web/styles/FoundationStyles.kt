package com.example.habittracker.presentation.web.styles

import kotlinx.css.CssBuilder

internal fun CssBuilder.designTokens() {
    css(":root") {
        put("--bg", "#fff7ed")
        put("--bg-soft", "#ecfeff")
        put("--surface", "#ffffff")
        put("--surface-warm", "#fffbeb")
        put("--surface-strong", "#263044")
        put("--text", "#293247")
        put("--muted", "#68758d")
        put("--line", "#f7d6b5")
        put("--primary", "#f97316")
        put("--primary-dark", "#c2410c")
        put("--secondary", "#14b8a6")
        put("--secondary-dark", "#0f766e")
        put("--accent", "#22c55e")
        put("--pink", "#ec4899")
        put("--warning", "#d97706")
        put("--shadow", "0 18px 50px rgba(249, 115, 22, 0.14)")
    }
}

internal fun CssBuilder.baseStyles() {
    css("*") {
        put("box-sizing", "border-box")
    }
    css("body") {
        put("margin", "0")
        put("background", "linear-gradient(135deg, var(--bg) 0%, #fef3c7 38%, var(--bg-soft) 100%)")
        put("color", "var(--text)")
        put(
            "font-family",
            "Inter, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, \"Segoe UI\", sans-serif",
        )
        put("font-size", "16px")
        put("line-height", "1.5")
    }
    css("a") {
        put("color", "inherit")
        put("text-decoration", "none")
    }
    css("button, input, select, textarea") {
        put("font", "inherit")
    }
    css("h1, h2, p") {
        put("margin-top", "0")
    }
    css("h1") {
        put("color", "var(--surface-strong)")
        put("font-size", "48px")
        put("line-height", "1.05")
        put("margin-bottom", "18px")
    }
    css("h2") {
        put("color", "var(--surface-strong)")
        put("font-size", "20px")
        put("margin-bottom", "14px")
    }
}

internal fun CssBuilder.navigationStyles() {
    css(".app-shell") {
        put("min-height", "100vh")
    }
    css(".topbar") {
        put("align-items", "center")
        put("background", "rgba(255, 251, 235, 0.94)")
        put("border-bottom", "1px solid var(--line)")
        put("display", "grid")
        put("gap", "24px")
        put("grid-template-columns", "160px 1fr auto")
        put("padding", "16px 40px")
        put("position", "sticky")
        put("top", "0")
        put("z-index", "10")
    }
    css(".brand") {
        put("color", "var(--primary-dark)")
        put("font-size", "22px")
        put("font-weight", "800")
    }
    css(".nav-links, .auth-links, .hero-actions, .habit-metrics") {
        put("align-items", "center")
        put("display", "flex")
        put("gap", "12px")
    }
    css(".nav-link, .ghost-link, .logout-link") {
        put("background", "transparent")
        put("border", "0")
        put("border-radius", "8px")
        put("color", "var(--muted)")
        put("cursor", "pointer")
        put("padding", "8px 10px")
    }
    css(".nav-link.active, .nav-link:hover, .ghost-link:hover, .logout-link:hover") {
        put("background", "#fed7aa")
        put("color", "#7c2d12")
    }
}

internal fun CssBuilder.buttonStyles() {
    css(".primary-link, .button") {
        put("border", "0")
        put("border-radius", "8px")
        put("cursor", "pointer")
        put("display", "inline-flex")
        put("font-weight", "700")
        put("justify-content", "center")
        put("padding", "10px 14px")
    }
    css(".primary-link, .button.primary") {
        put("background", "linear-gradient(135deg, var(--primary), var(--pink))")
        put("color", "#ffffff")
    }
    css(".button.primary:hover, .primary-link:hover") {
        put("filter", "brightness(0.96)")
        put("transform", "translateY(-1px)")
    }
    css(".button.secondary") {
        put("background", "#ccfbf1")
        put("color", "var(--secondary-dark)")
    }
    css(".button.danger") {
        put("background", "#fee2e2")
        put("color", "#991b1b")
    }
    css(".button.full") {
        put("width", "100%")
    }
    css(".inline-form") {
        put("margin", "0")
    }
}
