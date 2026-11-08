package com.example.backend.controller

import com.example.backend.dto.EspecimeRequest
import com.example.backend.dto.EspecimeResponse
import com.example.backend.service.EspecimeService
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
@RequestMapping("/api/especimes")
@Tag(
    name = "Especimes",
    description = "CRUD de Especimes (criaturas individuais da loja). Cada especime pertence a uma Especie e opcionalmente a um Aquario da loja."
)
class EspecimeController(
    private val service: EspecimeService
) {

    @GetMapping
    @Operation(
        summary = "Lista os especimes da loja autenticada",
        description = "Retorna apenas os especimes pertencentes a loja autenticada, com resumo de especie e aquario."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", useReturnTypeSchema = true)
        ]
    )
    fun listar(): ResponseEntity<List<EspecimeResponse>> {
        return ResponseEntity.ok(service.listarTodosDaLoja())
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Busca um especime por ID (da loja autenticada)",
        description = "404 se o ID nao existir ou nao pertencer a loja."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Especime encontrado"),
            ApiResponse(
                responseCode = "404", description = "Especime nao encontrado na loja",
                content = [Content(schema = Schema(implementation = Any::class))]
            )
        ]
    )
    fun buscarPorId(
        @Parameter(description = "ID do especime") @PathVariable id: Long
    ): ResponseEntity<EspecimeResponse> {
        return ResponseEntity.ok(service.buscarPorIdDaLoja(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Cria um novo especime",
        description = "Cria e associa a loja autenticada. Valida identificacao UNIQUE, especie existente e aquario da loja."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Especime criado com sucesso"),
            ApiResponse(responseCode = "400", description = "Identificacao duplicada, especie inexistente, aquario de outra loja ou dados invalidos"),
            ApiResponse(responseCode = "404", description = "Especie ou aquario referenciado nao encontrado")
        ]
    )
    fun criar(@Valid @RequestBody request: EspecimeRequest): ResponseEntity<EspecimeResponse> {
        val novo = service.criar(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(novo)
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualiza um especime existente",
        description = "Atualiza apenas se o especime pertencer a loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Especime atualizado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados invalidos"),
            ApiResponse(responseCode = "404", description = "Especime nao encontrado na loja")
        ]
    )
    fun atualizar(
        @Parameter(description = "ID do especime que sera atualizado") @PathVariable id: Long,
        @Valid @RequestBody request: EspecimeRequest
    ): ResponseEntity<EspecimeResponse> {
        return ResponseEntity.ok(service.atualizar(id, request))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Exclui um especime",
        description = "Remove apenas se pertencer a loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Especime excluido com sucesso"),
            ApiResponse(responseCode = "404", description = "Especime nao encontrado na loja")
        ]
    )
    fun excluir(
        @Parameter(description = "ID do especime que sera excluido") @PathVariable id: Long
    ): ResponseEntity<Unit> {
        service.excluir(id)
        return ResponseEntity.noContent().build()
    }
}
