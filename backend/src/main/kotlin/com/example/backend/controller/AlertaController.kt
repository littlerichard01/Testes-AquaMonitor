package com.example.backend.controller

import com.example.backend.dto.AlertaRequest
import com.example.backend.dto.AlertaResponse
import com.example.backend.service.AlertaService
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
@RequestMapping("/api/alertas")
@Tag(
    name = "Alertas",
    description = "CRUD de Alertas por Aquario. Tipo: TEMPERATURA (usa ConfiguracaoTemperatura do aquario) ou ALIMENTACAO (usa ProgramacaoAlimentacao)."
)
class AlertaController(
    private val service: AlertaService
) {

    @GetMapping
    @Operation(
        summary = "Lista os alertas da loja autenticada",
        description = "Ordenados por severidade (ALTA primeiro) e nome do aquario."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", useReturnTypeSchema = true)
        ]
    )
    fun listar(): ResponseEntity<List<AlertaResponse>> {
        return ResponseEntity.ok(service.listarTodosDaLoja())
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Busca um alerta por ID (da loja autenticada)",
        description = "404 se nao existir ou nao pertencer a loja."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Alerta encontrado"),
            ApiResponse(
                responseCode = "404", description = "Alerta nao encontrado na loja",
                content = [Content(schema = Schema(implementation = Any::class))]
            )
        ]
    )
    fun buscarPorId(
        @Parameter(description = "ID do alerta") @PathVariable id: Long
    ): ResponseEntity<AlertaResponse> {
        return ResponseEntity.ok(service.buscarPorIdDaLoja(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Cria um novo alerta",
        description = "Valida que o aquario pertence a loja autenticada. Alerta de TEMPERATURA nao armazena limites — sempre reutiliza ConfiguracaoTemperatura do aquario."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Alerta criado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados invalidos"),
            ApiResponse(responseCode = "404", description = "Aquario referenciado nao encontrado na loja")
        ]
    )
    fun criar(@Valid @RequestBody request: AlertaRequest): ResponseEntity<AlertaResponse> {
        val novo = service.criar(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(novo)
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualiza um alerta existente",
        description = "Atualiza apenas se pertencer a loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Alerta atualizado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados invalidos"),
            ApiResponse(responseCode = "404", description = "Alerta ou aquario nao encontrado na loja")
        ]
    )
    fun atualizar(
        @Parameter(description = "ID do alerta que sera atualizado") @PathVariable id: Long,
        @Valid @RequestBody request: AlertaRequest
    ): ResponseEntity<AlertaResponse> {
        return ResponseEntity.ok(service.atualizar(id, request))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Exclui um alerta",
        description = "Remove apenas se pertencer a loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Alerta excluido com sucesso"),
            ApiResponse(responseCode = "404", description = "Alerta nao encontrado na loja")
        ]
    )
    fun excluir(
        @Parameter(description = "ID do alerta que sera excluido") @PathVariable id: Long
    ): ResponseEntity<Unit> {
        service.excluir(id)
        return ResponseEntity.noContent().build()
    }
}
