package com.example.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

data class ErroDTO(
    val mensagem: String,
    val codigoHttp: Int,
    val errosCampos: Map<String, String>? = null
)

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RegraNaoEncontradoException::class)
    fun tratarNaoEncontrado(ex: RegraNaoEncontradoException): ResponseEntity<ErroDTO> {
        val dto = ErroDTO(mensagem = ex.message ?: "Recurso nao encontrado", codigoHttp = 404)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun tratarValidacaoDeCampos(ex: MethodArgumentNotValidException): ResponseEntity<ErroDTO> {
        val erros = ex.bindingResult.fieldErrors.associate { campo ->
            campo.field to (campo.defaultMessage ?: "Campo invalido")
        }
        val dto = ErroDTO(
            mensagem = "Foram encontrados erros de validacao na requisicao",
            codigoHttp = 400,
            errosCampos = erros
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto)
    }

    @ExceptionHandler(ErroValidacaoException::class)
    fun tratarRegraNegocio(ex: ErroValidacaoException): ResponseEntity<ErroDTO> {
        val dto = ErroDTO(mensagem = ex.message ?: "Erro de validacao", codigoHttp = 400)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto)
    }

    @ExceptionHandler(EmailJaCadastradoException::class)
    fun tratarEmailDuplicado(ex: EmailJaCadastradoException): ResponseEntity<ErroDTO> {
        val dto = ErroDTO(mensagem = ex.message ?: "Este e-mail já está cadastrado.", codigoHttp = 400)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto)
    }

    @ExceptionHandler(CredenciaisIncorretasException::class)
    fun tratarCredenciaisIncorretas(ex: CredenciaisIncorretasException): ResponseEntity<ErroDTO> {
        val dto = ErroDTO(mensagem = ex.message ?: "E-mail ou senha incorretos.", codigoHttp = 401)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto)
    }

    @ExceptionHandler(Exception::class)
    fun tratarGenerico(ex: Exception): ResponseEntity<ErroDTO> {
        val dto = ErroDTO(mensagem = "Erro interno no servidor: ${ex.message}", codigoHttp = 500)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto)
    }
}
