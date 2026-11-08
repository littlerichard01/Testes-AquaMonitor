package com.example.backend.controller

import com.example.backend.dto.LojaLoginRequest
import com.example.backend.dto.LojaRequest
import com.example.backend.dto.LojaResponse
import com.example.backend.dto.LoginResponse
import com.example.backend.service.LojaService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Tag(
    name = "Autenticacao", description = "Endpoints para cadastro e login de lojas (contas de acesso)."
)
class AuthController(
    private val lojaService: LojaService
) {

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar uma nova Loja no sistema")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Loja cadastrada com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados invalidos ou email ja cadastrado")
        ]
    )
    fun cadastrar(@Valid @RequestBody request: LojaRequest): LojaResponse {
        return lojaService.cadastrar(request)
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Realizar login de uma Loja")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            ApiResponse(responseCode = "401", description = "Email ou senha incorretos, ou conta inativa")
        ]
    )
    fun login(@Valid @RequestBody request: LojaLoginRequest): LoginResponse {
        return lojaService.login(request)
    }
}
