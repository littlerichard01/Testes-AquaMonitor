package com.example.backend.controller

import com.example.backend.dto.CompradorRequest
import com.example.backend.dto.CompradorResponse
import com.example.backend.service.CompradorService
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
@RequestMapping("/api/compradores")
@Tag(
    name = "Compradores",
    description = "CRUD de Compradores associados a loja autenticada (relacao N:M com lojas)."
)
class CompradorController(
    private val service: CompradorService
) {

    @GetMapping
    @Operation(
        summary = "Lista os compradores da loja autenticada",
        description = "Apenas compradores associados via tabela de ligacao lojas_compradores."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", useReturnTypeSchema = true)
        ]
    )
    fun listar(): ResponseEntity<List<CompradorResponse>> {
        return ResponseEntity.ok(service.listarTodosDaLoja())
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Busca um comprador por ID (da loja autenticada)",
        description = "404 se nao existir ou nao estiver associado a loja."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Comprador encontrado"),
            ApiResponse(
                responseCode = "404", description = "Comprador nao encontrado na loja",
                content = [Content(schema = Schema(implementation = Any::class))]
            )
        ]
    )
    fun buscarPorId(
        @Parameter(description = "ID do comprador") @PathVariable id: Long
    ): ResponseEntity<CompradorResponse> {
        return ResponseEntity.ok(service.buscarPorIdDaLoja(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Cria um novo comprador",
        description = "Cria o comprador e ja associa a loja autenticada (adiciona na tabela N:M)."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Comprador criado e associado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados invalidos (CPF invalido etc.)")
        ]
    )
    fun criar(@Valid @RequestBody request: CompradorRequest): ResponseEntity<CompradorResponse> {
        val novo = service.criar(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(novo)
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualiza um comprador existente",
        description = "Atualiza apenas se o comprador pertencer a loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Comprador atualizado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados invalidos"),
            ApiResponse(responseCode = "404", description = "Comprador nao encontrado")
        ]
    )
    fun atualizar(
        @Parameter(description = "ID do comprador que sera atualizado") @PathVariable id: Long,
        @Valid @RequestBody request: CompradorRequest
    ): ResponseEntity<CompradorResponse> {
        return ResponseEntity.ok(service.atualizar(id, request))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Desassocia e opcionalmente exclui um comprador",
        description = "Remove a ligacao da loja autenticada. Se o comprador nao tiver mais lojas associadas, e excluido permanentemente."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Operacao concluida com sucesso"),
            ApiResponse(responseCode = "404", description = "Comprador nao encontrado na loja")
        ]
    )
    fun excluir(
        @Parameter(description = "ID do comprador") @PathVariable id: Long
    ): ResponseEntity<Unit> {
        service.excluir(id)
        return ResponseEntity.noContent().build()
    }
}
