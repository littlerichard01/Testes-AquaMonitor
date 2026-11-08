package com.example.backend.config

import com.example.backend.service.LojaUserDetailsService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val tokenUtil: TokenUtil,
    private val lojaUserDetailsService: LojaUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")

        if (!header.isNullOrBlank() && header.startsWith("Bearer ", ignoreCase = true)) {
            val token = header.substring(7).trim()

            if (tokenUtil.validarToken(token)) {
                val email = tokenUtil.extrairEmailDoToken(token)

                if (!email.isNullOrBlank() && SecurityContextHolder.getContext().authentication == null) {
                    val userDetails = lojaUserDetailsService.loadUserByUsername(email)
                    val authentication = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }

        filterChain.doFilter(request, response)
    }
}
