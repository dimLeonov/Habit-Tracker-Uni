
package com.example.habittracker.presentation.web.styles

import kotlinx.css.CssBuilder

@Suppress("LongMethod")
internal fun CssBuilder.habitStyles() {
    css(".suggestion-grid, .habit-board") {
        put("display", "grid")
        put("gap", "16px")
        put("grid-template-columns", "repeat(2, minmax(0, 1fr))")
    }
    css(".suggestion-card, .habit-card") {
        put("padding", "22px")
    }
    css(".category-chip") {
        put("background", "#fce7f3")
        put("border-radius", "999px")
        put("color", "#9d174d")
        put("display", "inline-flex")
        put("margin-bottom", "12px")
        put("padding", "5px 9px")
    }
    css(".habit-metrics") {
        put("color", "var(--muted)")
        put("font-size", "14px")
        put("justify-content", "flex-start")
        put("margin", "10px 0 16px")
    }
    css(".week-row") {
        put("display", "grid")
        put("gap", "8px")
        put("grid-template-columns", "repeat(7, 1fr)")
        put("margin-bottom", "18px")
    }
    css(".week-dot") {
        put("background", "#fde68a")
        put("border-radius", "999px")
        put("height", "10px")
    }
    css(".week-dot.complete") {
        put("background", "linear-gradient(90deg, var(--accent), var(--secondary))")
    }
    css(".step-list") {
        put("display", "grid")
        put("gap", "8px")
        put("margin-bottom", "16px")
    }
    css(".step-row, .habit-actions") {
        put("align-items", "center")
        put("display", "flex")
        put("gap", "10px")
        put("justify-content", "space-between")
    }
    css(".habit-actions") {
        put("margin", "14px 0")
    }
    css(".edit-habit-form") {
        put("border-top", "1px solid var(--line)")
        put("display", "grid")
        put("gap", "12px")
        put("margin-top", "16px")
        put("padding-top", "16px")
    }
    css(".status-pill") {
        put("border-radius", "999px")
        put("font-size", "13px")
        put("font-weight", "800")
        put("padding", "5px 10px")
    }
    css(".status-pill.done") {
        put("background", "#dcfce7")
        put("color", "#166534")
    }
    css(".status-pill.pending") {
        put("background", "#fef3c7")
        put("color", "var(--warning)")
    }
}

internal fun CssBuilder.leaderboardStyles() {
    css(".leaderboard") {
        put("overflow", "hidden")
    }
    css(".leaderboard-row") {
        put("border-bottom", "1px solid var(--line)")
        put("display", "grid")
        put("gap", "18px")
        put("grid-template-columns", "56px 52px 1fr auto")
        put("padding", "18px 22px")
    }
    css(".leaderboard-row:last-child") {
        put("border-bottom", "0")
    }
    css(".rank") {
        put("color", "var(--pink)")
        put("font-weight", "900")
    }
    css(".avatar") {
        put("align-items", "center")
        put("background", "linear-gradient(135deg, var(--secondary), var(--primary))")
        put("border-radius", "50%")
        put("color", "#ffffff")
        put("display", "flex")
        put("font-weight", "800")
        put("height", "44px")
        put("justify-content", "center")
        put("width", "44px")
    }
    css(".leader-name, .leader-score") {
        put("display", "grid")
    }
    css(".leader-score") {
        put("justify-items", "end")
    }
}

@Suppress("LongMethod")
internal fun CssBuilder.calendarStyles() {
    css(".calendar-toolbar") {
        put("align-items", "center")
        put("display", "flex")
        put("flex-wrap", "wrap")
        put("gap", "12px")
        put("margin", "18px 0")
    }
    css(".calendar-month") {
        put("color", "var(--surface-strong)")
        put("font-size", "20px")
        put("font-weight", "900")
    }
    css(".calendar-score") {
        put("background", "rgba(255, 255, 255, 0.78)")
        put("border", "1px solid var(--line)")
        put("border-radius", "8px")
        put("display", "inline-grid")
        put("padding", "16px 20px")
    }
    css(".calendar-card") {
        put("background", "rgba(255, 255, 255, 0.88)")
        put("border", "1px solid var(--line)")
        put("border-radius", "8px")
        put("box-shadow", "var(--shadow)")
        put("padding", "18px")
    }
    css(".calendar-weekdays, .calendar-week") {
        put("display", "grid")
        put("gap", "8px")
        put("grid-template-columns", "repeat(7, minmax(0, 1fr))")
    }
    css(".calendar-weekdays") {
        put("color", "var(--muted)")
        put("font-size", "13px")
        put("font-weight", "800")
        put("margin-bottom", "8px")
        put("text-align", "center")
        put("text-transform", "uppercase")
    }
    css(".calendar-week") { put("margin-bottom", "8px") }
    css(".calendar-day") {
        put("background", "#fff7ed")
        put("border", "1px solid #fed7aa")
        put("border-radius", "8px")
        put("display", "grid")
        put("min-height", "96px")
        put("padding", "10px")
    }
    css(".calendar-day.complete") {
        put("background", "linear-gradient(135deg, #dcfce7, #ccfbf1)")
        put("border-color", "#86efac")
    }
    css(".calendar-day.outside-month") {
        put("opacity", "0.42")
    }
    css(".calendar-day-number") {
        put("font-weight", "900")
    }
    css(".calendar-day-progress") {
        put("align-self", "end")
        put("color", "var(--muted)")
        put("font-size", "13px")
        put("font-weight", "800")
    }
}

internal fun CssBuilder.responsiveStyles() {
    media("(max-width: 860px)") {
        css(".topbar, .home-hero, .auth-page, .habit-place-grid") {
            put("grid-template-columns", "1fr")
        }
        css(".nav-links, .auth-links") {
            put("flex-wrap", "wrap")
        }
        css("main, .topbar") {
            put("padding", "24px")
        }
        css(".hero-copy") {
            put("padding", "32px")
        }
        css("h1") {
            put("font-size", "36px")
        }
        css(".stats-grid, .suggestion-grid, .habit-board") {
            put("grid-template-columns", "1fr")
        }
        css(".leaderboard-row") {
            put("grid-template-columns", "44px 44px 1fr")
        }
        css(".leader-score") {
            put("grid-column", "3")
            put("justify-items", "start")
        }
        css(".calendar-day") {
            put("min-height", "76px")
            put("padding", "8px")
        }
    }
}
