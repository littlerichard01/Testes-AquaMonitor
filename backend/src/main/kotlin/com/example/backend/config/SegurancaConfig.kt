package com.example.backend.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import java.time.temporal.ChronoUnit

@Configuration
class SegurancaConfig {

    @Bean
    fun tokenUtil(): TokenUtil {
        return TokenUtil()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}

class TokenUtil {

    @Value("\${jwt.secret}")
    private lateinit var chaveSecreta: String

    @Value("\${jwt.issuer:backend-aquario}")
    private lateinit var emissor: String

    private val algoritmo by lazy { Algorithm.HMAC256(chaveSecreta) }

    private val verificar by lazy {
        JWT.require(algoritmo)
            .withIssuer(emissor)
            .build()
    }

    fun gerarToken(email: String): String {
        return JWT.create()
            .withIssuer(emissor)
            .withSubject(email)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
            .sign(algoritmo)
    }

    fun validarToken(token: String): Boolean {
        return try {
            verificar.verify(token)
            true
        } catch (ex: JWTVerificationException) {
            false
        } catch (ex: IllegalArgumentException) {
            false
        }
    }

    fun extrairEmailDoToken(token: String): String? {
        return try {
            val jwt = verificar.verify(token)
            jwt.subject
        } catch (ex: TokenExpiredException) {
            null
        } catch (ex: JWTVerificationException) {
            null
        } catch (ex: IllegalArgumentException) {
            null
        }
    }
}