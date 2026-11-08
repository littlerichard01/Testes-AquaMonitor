package com.example.backend.controller

import com.example.backend.dto.LojaAtualizacaoRequest
import com.example.backend.dto.LojaResponse
import com.example.backend.service.LojaService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/lojas")
@Tag(
    name = "Lojas",
    description = "CRUD de Lojas (contas de acesso). O cadastro publico fica em POST /api/auth/signup."
)
class LojaController(
    private val service: LojaService
) {

    @GetMapping("/perfil")
    @Operation(
        summary = "Retorna os dados da loja autenticada",
        description = "Busca a loja associada ao token JWT enviado na requisicao."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Dados da loja autenticada"),
            ApiResponse(
                responseCode = "401", description = "Token invalido, expirado ou nao informado",
                content = [Content(schema = Schema(implementation = Any::class))]
            )
        ]
    )
    fun buscarPerfil(): ResponseEntity<LojaResponse> {
        return ResponseEntity.ok(service.buscarPerfilAutenticado())
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualiza uma loja existente",
        description = "Sobrescreve os campos da loja com o ID informado. Apenas a propria loja autenticada pode se atualizar."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Loja atualizada com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados invalidos, email duplicado ou tentativa de alterar outra loja"),
            ApiResponse(responseCode = "401", description = "Nao autenticado"),
            ApiResponse(responseCode = "404", description = "Loja nao encontrada")
        ]
    )
    fun atualizar(
        @Parameter(description = "ID da loja que sera atualizada") @PathVariable id: Long,
        @Valid @RequestBody request: LojaAtualizacaoRequest
    ): ResponseEntity<LojaResponse> {
        return ResponseEntity.ok(service.atualizar(id, request))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Exclui uma loja",
        description = "Remove permanentemente uma loja pelo ID. Apenas a propria loja autenticada pode se excluir."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Loja excluida com sucesso (sem corpo de resposta)"),
            ApiResponse(responseCode = "400", description = "Tentativa de excluir outra loja"),
            ApiResponse(responseCode = "401", description = "Nao autenticado"),
            ApiResponse(responseCode = "404", description = "Loja nao encontrada")
        ]
    )
    fun excluir(
        @Parameter(description = "ID da loja que sera excluida") @PathVariable id: Long
    ): ResponseEntity<Unit> {
        service.excluir(id)
        return ResponseEntity.noContent().build()
    }
}
