package com.example.habittracker.infrastructure.security

import com.example.habittracker.infrastructure.persistence.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class JpaUserDetailsService(private val users: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = users.findByUsername(username.trim()) ?: throw UsernameNotFoundException(username)
        return User
            .withUsername(user.username)
            .password(user.passwordHash)
            .roles("USER")
            .build()
    }
}
