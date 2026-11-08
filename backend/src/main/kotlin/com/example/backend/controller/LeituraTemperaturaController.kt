package com.example.backend.controller

import com.example.backend.dto.LeituraTemperaturaRequest
import com.example.backend.dto.LeituraTemperaturaResponse
import com.example.backend.service.LeituraTemperaturaService
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
@RequestMapping("/api/leituras-temperatura")
@Tag(
    name = "Leituras de Temperatura",
    description = "Append-only: apenas criar e consultar leituras. Nao ha endpoints para atualizar ou excluir."
)
class LeituraTemperaturaController(
    private val service: LeituraTemperaturaService
) {

    @GetMapping
    @Operation(
        summary = "Lista as ultimas leituras de temperatura da loja autenticada",
        description = "Retorna as leituras mais recentes (ate 500 por padrao). Use o parametro limite para ajustar."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", useReturnTypeSchema = true)
        ]
    )
    fun listar(
        @Parameter(description = "Quantidade maxima de registros retornados (ate 500). Default = 500")
        @RequestParam(required = false, defaultValue = "500") limite: Int
    ): ResponseEntity<List<LeituraTemperaturaResponse>> {
        return ResponseEntity.ok(service.listarUltimasDaLoja(limite))
    }

    @GetMapping("/por-aquario/{aquarioId}")
    @Operation(
        summary = "Lista ultimas leituras por aquario da loja autenticada",
        description = "Retorna as leituras mais recentes de um aquario especifico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            ApiResponse(
                responseCode = "404", description = "Aquario nao pertence a loja autenticada",
                content = [Content(schema = Schema(implementation = Any::class))]
            )
        ]
    )
    fun listarPorAquario(
        @Parameter(description = "ID do aquario") @PathVariable aquarioId: Long,
        @Parameter(description = "Limite de registros. Default = 500")
        @RequestParam(required = false, defaultValue = "500") limite: Int
    ): ResponseEntity<List<LeituraTemperaturaResponse>> {
        return ResponseEntity.ok(service.listarUltimasPorAquario(aquarioId, limite))
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Busca uma leitura de temperatura por ID",
        description = "404 se a leitura nao existir ou pertencer a aquario de outra loja."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Leitura encontrada"),
            ApiResponse(
                responseCode = "404", description = "Leitura nao encontrada na loja",
                content = [Content(schema = Schema(implementation = Any::class))]
            )
        ]
    )
    fun buscarPorId(
        @Parameter(description = "ID da leitura de temperatura") @PathVariable id: Long
    ): ResponseEntity<LeituraTemperaturaResponse> {
        return ResponseEntity.ok(service.buscarPorIdDaLoja(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Registra uma nova leitura de temperatura (append-only)",
        description = "Cria e tambem atualiza o campo ultimaComunicacao do DispositivoIoT associado. Nao ha edicao posterior."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Leitura registrada com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados invalidos, dispositivo nao pertence a loja ou data futura"),
            ApiResponse(responseCode = "404", description = "Aquario ou dispositivo nao encontrados na loja")
        ]
    )
    fun criar(@Valid @RequestBody request: LeituraTemperaturaRequest): ResponseEntity<LeituraTemperaturaResponse> {
        val nova = service.criar(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(nova)
    }
    @PutMapping("/{id}")
    @Operation(
        summary = "Atualiza uma leitura de temperatura existente",
        description = "Atualiza temperatura, data/hora, aquario e dispositivo. 404 se a leitura nao pertencer a loja."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Leitura atualizada com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados invalidos ou dispositivo nao pertence a loja"),
            ApiResponse(responseCode = "404", description = "Leitura nao encontrada na loja")
        ]
    )
    fun atualizar(
        @Parameter(description = "ID da leitura") @PathVariable id: Long,
        @Valid @RequestBody request: LeituraTemperaturaRequest
    ): ResponseEntity<LeituraTemperaturaResponse> {
        return ResponseEntity.ok(service.atualizar(id, request))
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Exclui uma leitura de temperatura",
        description = "Remove a leitura do banco. 404 se nao pertencer a loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Leitura excluida com sucesso"),
            ApiResponse(responseCode = "404", description = "Leitura nao encontrada na loja")
        ]
    )
    fun remover(
        @Parameter(description = "ID da leitura") @PathVariable id: Long
    ): ResponseEntity<Void> {
        service.remover(id)
        return ResponseEntity.noContent().build()
    }
}
