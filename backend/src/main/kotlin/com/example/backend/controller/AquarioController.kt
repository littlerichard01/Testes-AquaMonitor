package com.example.backend.controller

import com.example.backend.dto.AquarioRequest
import com.example.backend.dto.AquarioResponse
import com.example.backend.service.AquarioService
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
@RequestMapping("/api/aquarios")
@Tag(
    name = "Aquarios",
    description = "CRUD de Aquarios. Cada aquario pertence exclusivamente a loja autenticada."
)
class AquarioController(
    private val service: AquarioService
) {

    @GetMapping
    @Operation(
        summary = "Lista os aquarios da loja autenticada",
        description = "Retorna apenas os aquarios pertencentes a loja do token JWT."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", useReturnTypeSchema = true)
        ]
    )
    fun listar(): ResponseEntity<List<AquarioResponse>> {
        return ResponseEntity.ok(service.listarTodosDaLoja())
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Busca um aquario por ID (da loja autenticada)",
        description = "Retorna 404 se o ID nao existir ou pertencer a outra loja."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Aquario encontrado"),
            ApiResponse(
                responseCode = "404", description = "Aquario nao encontrado na loja",
                content = [Content(schema = Schema(implementation = Any::class))]
            )
        ]
    )
    fun buscarPorId(
        @Parameter(description = "ID do aquario") @PathVariable id: Long
    ): ResponseEntity<AquarioResponse> {
        return ResponseEntity.ok(service.buscarPorIdDaLoja(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Cria um novo aquario",
        description = "Cadastra um aquario vinculado a loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Aquario criado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados invalidos na requisicao")
        ]
    )
    fun criar(@Valid @RequestBody request: AquarioRequest): ResponseEntity<AquarioResponse> {
        val nova = service.criar(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(nova)
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualiza um aquario existente",
        description = "Atualiza apenas se o aquario pertencer a loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Aquario atualizado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados invalidos na requisicao"),
            ApiResponse(responseCode = "404", description = "Aquario nao encontrado na loja")
        ]
    )
    fun atualizar(
        @Parameter(description = "ID do aquario que sera atualizado") @PathVariable id: Long,
        @Valid @RequestBody request: AquarioRequest
    ): ResponseEntity<AquarioResponse> {
        return ResponseEntity.ok(service.atualizar(id, request))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Exclui um aquario",
        description = "Remove permanentemente apenas se pertencer a loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Aquario excluido com sucesso"),
            ApiResponse(responseCode = "404", description = "Aquario nao encontrado na loja")
        ]
    )
    fun excluir(
        @Parameter(description = "ID do aquario que sera excluido") @PathVariable id: Long
    ): ResponseEntity<Unit> {
        service.excluir(id)
        return ResponseEntity.noContent().build()
    }
}
