package com.example.backend.controller

import com.example.backend.dto.EspecieRequest
import com.example.backend.dto.EspecieResponse
import com.example.backend.service.EspecieService
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
@RequestMapping("/api/especies")
@Tag(
    name = "Especies",
    description = "CRUD de Especies. Exemplo: Peixe-palhaco, Guppy, Carangueijo, etc."
)
class EspecieController(
    private val service: EspecieService
) {

    @GetMapping
    @Operation(
        summary = "Lista todas as especies",
        description = "Retorna todas as especies cadastradas no banco de dados."
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", useReturnTypeSchema = true)
    ])
    fun listar(): ResponseEntity<List<EspecieResponse>> {
        return ResponseEntity.ok(service.listarTodos())
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Busca uma especie por ID",
        description = "Retorna os dados de uma unica especie pelo seu identificador."
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Especie encontrada"),
        ApiResponse(
            responseCode = "404", description = "Especie nao encontrada",
            content = [Content(schema = Schema(implementation = Any::class))]
        )
    ])
    fun buscarPorId(
        @Parameter(description = "ID da especie") @PathVariable id: Long
    ): ResponseEntity<EspecieResponse> {
        return ResponseEntity.ok(service.buscarPorId(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Cria uma nova especie",
        description = "Cadastra uma especie nova com os dados informados no corpo da requisicao."
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Especie criada com sucesso"),
        ApiResponse(responseCode = "400", description = "Dados invalidos na requisicao (campos obrigatorios faltando ou temperatura invalida)")
    ])
    fun criar(@Valid @RequestBody request: EspecieRequest): ResponseEntity<EspecieResponse> {
        val nova = service.criar(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(nova)
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualiza uma especie existente",
        description = "Sobrescreve todos os campos da especie com o ID informado."
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Especie atualizada com sucesso"),
        ApiResponse(responseCode = "400", description = "Dados invalidos na requisicao"),
        ApiResponse(responseCode = "404", description = "Especie nao encontrada")
    ])
    fun atualizar(
        @Parameter(description = "ID da especie que sera atualizada") @PathVariable id: Long,
        @Valid @RequestBody request: EspecieRequest
    ): ResponseEntity<EspecieResponse> {
        return ResponseEntity.ok(service.atualizar(id, request))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Exclui uma especie",
        description = "Remove permanentemente uma especie do banco de dados pelo ID."
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Especie excluida com sucesso (sem corpo de resposta)"),
        ApiResponse(responseCode = "404", description = "Especie nao encontrada")
    ])
    fun excluir(
        @Parameter(description = "ID da especie que sera excluida") @PathVariable id: Long
    ): ResponseEntity<Unit> {
        service.excluir(id)
        return ResponseEntity.noContent().build()
    }
}
