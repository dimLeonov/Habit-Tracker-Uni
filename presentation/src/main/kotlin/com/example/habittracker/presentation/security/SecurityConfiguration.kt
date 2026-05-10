@file:Suppress("CascadingCallWrapping", "ChainMethodContinuation")

package com.example.habittracker.presentation.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfiguration {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain = http
        .authorizeHttpRequests { requests ->
            requests.requestMatchers(
                "/",
                "/login",
                "/register",
                "/leaderboard",
                "/api/**",
                "/styles/**",
                "/h2-console/**",
            ).permitAll().anyRequest().authenticated()
        }.formLogin { login ->
            login.loginPage("/login").defaultSuccessUrl("/workspace", true).permitAll()
        }.logout { logout ->
            logout.logoutSuccessUrl("/").permitAll()
        }.csrf { csrf ->
            csrf.ignoringRequestMatchers("/h2-console/**")
        }.headers { headers ->
            headers.frameOptions { frameOptions ->
                frameOptions.sameOrigin()
            }
        }.build()
}
