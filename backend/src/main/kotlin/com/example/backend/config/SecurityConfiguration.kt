package com.example.backend.config

import com.example.backend.exception.AcessoNegadoHandler
import com.example.backend.exception.FalhaAutenticacaoEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfiguration(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val lojaUserDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder,
    private val falhaAutenticacaoEntryPoint: FalhaAutenticacaoEntryPoint,
    private val acessoNegadoHandler: AcessoNegadoHandler
) {

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        return DaoAuthenticationProvider(lojaUserDetailsService).apply {
            setPasswordEncoder(passwordEncoder)
            setHideUserNotFoundExceptions(false)
        }
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuracao = CorsConfiguration()
        configuracao.addAllowedHeader("*")
        configuracao.addAllowedMethod("*")
        configuracao.addAllowedOriginPattern("*")
        configuracao.allowCredentials = true

        val fonte = UrlBasedCorsConfigurationSource()
        fonte.registerCorsConfiguration("/**", configuracao)
        return fonte
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { cors -> cors.configurationSource(corsConfigurationSource()) }
            .csrf { csrf -> csrf.disable() }
            .formLogin { form -> form.disable() }
            .httpBasic { basic -> basic.disable() }
            .logout { logout -> logout.disable() }
            .sessionManagement { sessao ->
                sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .exceptionHandling { excecoes ->
                excecoes.authenticationEntryPoint(falhaAutenticacaoEntryPoint)
                excecoes.accessDeniedHandler(acessoNegadoHandler)
            }
            .authorizeHttpRequests { autorizar ->
                autorizar
                    .requestMatchers(
                        "/api/auth/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger",
                        "/error"
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
