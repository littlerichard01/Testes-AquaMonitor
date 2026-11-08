package com.example.backend.controller

import com.example.backend.dto.ConfiguracaoTemperaturaRequest
import com.example.backend.dto.ConfiguracaoTemperaturaResponse
import com.example.backend.service.ConfiguracaoTemperaturaService
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
@RequestMapping("/api/configuracoes-temperatura")
@Tag(
    name = "Configuracoes de Temperatura",
    description = "CRUD de ConfiguracaoTemperatura. Regra 1:1 por Aquario (cada aquario tem no maximo uma configuracao)."
)
class ConfiguracaoTemperaturaController(
    private val service: ConfiguracaoTemperaturaService
) {

    @GetMapping
    @Operation(
        summary = "Lista as configuracoes de temperatura da loja autenticada",
        description = "Traz apenas as configuracoes associadas a aquarios da loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", useReturnTypeSchema = true)
        ]
    )
    fun listar(): ResponseEntity<List<ConfiguracaoTemperaturaResponse>> {
        return ResponseEntity.ok(service.listarTodosDaLoja())
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Busca uma configuracao de temperatura por ID",
        description = "404 se nao existir ou o aquario pertencer a outra loja."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Configuracao encontrada"),
            ApiResponse(
                responseCode = "404", description = "Configuracao nao encontrada na loja",
                content = [Content(schema = Schema(implementation = Any::class))]
            )
        ]
    )
    fun buscarPorId(
        @Parameter(description = "ID da configuracao de temperatura") @PathVariable id: Long
    ): ResponseEntity<ConfiguracaoTemperaturaResponse> {
        return ResponseEntity.ok(service.buscarPorIdDaLoja(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Cria uma nova configuracao de temperatura",
        description = "Valida que o aquario pertence a loja, que temperatura minima <= maxima e que nao existe outra configuracao para o mesmo aquario."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Configuracao criada com sucesso"),
            ApiResponse(responseCode = "400", description = "Aquario ja tem configuracao, limites invalidos ou aquario de outra loja"),
            ApiResponse(responseCode = "404", description = "Aquario referenciado nao encontrado na loja")
        ]
    )
    fun criar(@Valid @RequestBody request: ConfiguracaoTemperaturaRequest)
        : ResponseEntity<ConfiguracaoTemperaturaResponse> {
        val nova = service.criar(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(nova)
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualiza uma configuracao de temperatura existente",
        description = "Permite inclusive mudar o aquario associado, respeitando a regra 1:1."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Atualizada com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados invalidos"),
            ApiResponse(responseCode = "404", description = "Configuracao ou aquario nao encontrado na loja")
        ]
    )
    fun atualizar(
        @Parameter(description = "ID da configuracao que sera atualizada") @PathVariable id: Long,
        @Valid @RequestBody request: ConfiguracaoTemperaturaRequest
    ): ResponseEntity<ConfiguracaoTemperaturaResponse> {
        return ResponseEntity.ok(service.atualizar(id, request))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Exclui uma configuracao de temperatura",
        description = "Exclui apenas se pertencer a loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Excluida com sucesso"),
            ApiResponse(responseCode = "404", description = "Configuracao nao encontrada na loja")
        ]
    )
    fun excluir(
        @Parameter(description = "ID da configuracao que sera excluida") @PathVariable id: Long
    ): ResponseEntity<Unit> {
        service.excluir(id)
        return ResponseEntity.noContent().build()
    }
}
