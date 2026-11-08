package com.example.backend.service

import com.example.backend.repository.LojaRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class LojaUserDetailsService(
    private val lojaRepository: LojaRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val emailTratado = username.trim().lowercase()

        val loja = lojaRepository.findByEmail(emailTratado)
            ?: throw UsernameNotFoundException("Credenciais invalidas.")

        if (!loja.ativo) {
            throw UsernameNotFoundException("Conta inativa.")
        }

        val autoridades = listOf(SimpleGrantedAuthority("ROLE_LOJA"))

        return User
            .withUsername(loja.email)
            .password(loja.senha)
            .authorities(autoridades)
            .accountLocked(false)
            .disabled(false)
            .credentialsExpired(false)
            .accountExpired(false)
            .build()
    }
}
