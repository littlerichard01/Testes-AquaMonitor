package com.example.backend.controller

import com.example.backend.dto.VendaRequest
import com.example.backend.dto.VendaResponse
import com.example.backend.service.VendaService
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
@RequestMapping("/api/vendas")
@Tag(
    name = "Vendas",
    description = "Registro de Vendas. Regra XOR: cada venda tem exatamente UM entre Especime ou Aquario. Nao existe atualizacao de venda (historico imutavel)."
)
class VendaController(
    private val service: VendaService
) {

    @GetMapping
    @Operation(
        summary = "Lista as vendas da loja autenticada",
        description = "Ordenadas por data da venda (mais recente primeiro)."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", useReturnTypeSchema = true)
        ]
    )
    fun listar(): ResponseEntity<List<VendaResponse>> {
        return ResponseEntity.ok(service.listarTodosDaLoja())
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Busca uma venda por ID (da loja autenticada)",
        description = "404 se nao existir ou nao pertencer a loja."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Venda encontrada"),
            ApiResponse(
                responseCode = "404", description = "Venda nao encontrada na loja",
                content = [Content(schema = Schema(implementation = Any::class))]
            )
        ]
    )
    fun buscarPorId(
        @Parameter(description = "ID da venda") @PathVariable id: Long
    ): ResponseEntity<VendaResponse> {
        return ResponseEntity.ok(service.buscarPorIdDaLoja(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Registra uma nova venda",
        description = "Valida Regra XOR e que item nao esteja vendido. Atualiza status do alvo para VENDIDO."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Venda registrada com sucesso"),
            ApiResponse(
                responseCode = "400",
                description = "Regra XOR violada, item ja vendido, valor <= 0, data futura ou itens de outra loja"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Comprador ou item (especime/aquario) nao encontrado na loja"
            )
        ]
    )
    fun criar(@Valid @RequestBody request: VendaRequest): ResponseEntity<VendaResponse> {
        val nova = service.criar(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(nova)
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualiza uma venda existente",
        description = "Atualiza valor, data, comprador, especime ou aquario. 404 se a venda nao pertencer a loja."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Venda atualizada com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados invalidos, regra XOR violada ou item ja vendido"),
            ApiResponse(responseCode = "404", description = "Venda nao encontrada na loja")
        ]
    )
    fun atualizar(
        @Parameter(description = "ID da venda") @PathVariable id: Long,
        @Valid @RequestBody request: VendaRequest
    ): ResponseEntity<VendaResponse> {
        return ResponseEntity.ok(service.atualizar(id, request))
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Exclui uma venda",
        description = "Remove uma venda do banco e restaura o status do item para DISPONIVEL. 404 se nao pertencer a loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Venda excluida com sucesso"),
            ApiResponse(responseCode = "404", description = "Venda nao encontrada na loja")
        ]
    )
    fun remover(
        @Parameter(description = "ID da venda") @PathVariable id: Long
    ): ResponseEntity<Void> {
        service.remover(id)
        return ResponseEntity.noContent().build()
    }
}
