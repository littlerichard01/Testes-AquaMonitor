package com.example.backend.exception

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class FalhaAutenticacaoEntryPoint : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val mensagem = when {
            authException.message?.contains("Conta inativa", true) == true -> "Conta inativa, contate o suporte."
            else -> "Nao autorizado: faca login para acessar este recurso."
        }
        escreverErro(response, HttpStatus.UNAUTHORIZED.value(), mensagem)
    }

    private fun escreverErro(response: HttpServletResponse, codigo: Int, mensagem: String) {
        val json = """{"mensagem":${escapeJson(mensagem)},"codigoHttp":$codigo}"""
        response.status = codigo
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = Charsets.UTF_8.name()
        response.writer.write(json)
        response.writer.flush()
    }

    private fun escapeJson(valor: String): String {
        return buildString {
            append('"')
            for (ch in valor) {
                when (ch) {
                    '"' -> append("\\\"")
                    '\\' -> append("\\\\")
                    '\n' -> append("\\n")
                    '\r' -> append("\\r")
                    '\t' -> append("\\t")
                    else -> if (ch.code < 0x20) append("\\u%04x".format(ch.code)) else append(ch)
                }
            }
            append('"')
        }
    }
}

@Component
class AcessoNegadoHandler : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: org.springframework.security.access.AccessDeniedException
    ) {
        val codigo = HttpStatus.FORBIDDEN.value()
        val mensagem = "Acesso negado: voce nao tem permissao para acessar este recurso."
        val json = """{"mensagem":"$mensagem","codigoHttp":$codigo}"""
        response.status = codigo
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = Charsets.UTF_8.name()
        response.writer.write(json)
        response.writer.flush()
    }
}
