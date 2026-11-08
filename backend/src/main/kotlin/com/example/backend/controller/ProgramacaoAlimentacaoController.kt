package com.example.backend.controller

import com.example.backend.dto.ProgramacaoAlimentacaoRequest
import com.example.backend.dto.ProgramacaoAlimentacaoResponse
import com.example.backend.service.ProgramacaoAlimentacaoService
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
@RequestMapping("/api/programacoes-alimentacao")
@Tag(
    name = "Programacoes de Alimentacao",
    description = "CRUD de horarios agendados de alimentacao por Aquario."
)
class ProgramacaoAlimentacaoController(
    private val service: ProgramacaoAlimentacaoService
) {

    @GetMapping
    @Operation(
        summary = "Lista as programacoes de alimentacao da loja autenticada",
        description = "Ordenadas por horario ASC e nome do aquario."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", useReturnTypeSchema = true)
        ]
    )
    fun listar(): ResponseEntity<List<ProgramacaoAlimentacaoResponse>> {
        return ResponseEntity.ok(service.listarTodosDaLoja())
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Busca uma programacao de alimentacao por ID",
        description = "404 se nao existir ou pertencer a outra loja."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Programacao encontrada"),
            ApiResponse(
                responseCode = "404", description = "Programacao nao encontrada na loja",
                content = [Content(schema = Schema(implementation = Any::class))]
            )
        ]
    )
    fun buscarPorId(
        @Parameter(description = "ID da programacao de alimentacao") @PathVariable id: Long
    ): ResponseEntity<ProgramacaoAlimentacaoResponse> {
        return ResponseEntity.ok(service.buscarPorIdDaLoja(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Cria uma nova programacao de alimentacao",
        description = "Valida que o aquario pertence a loja autenticada e que o horario esta no formato HH:mm."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Programacao criada com sucesso"),
            ApiResponse(responseCode = "400", description = "Horario invalido ou dados invalidos"),
            ApiResponse(responseCode = "404", description = "Aquario referenciado nao encontrado na loja")
        ]
    )
    fun criar(@Valid @RequestBody request: ProgramacaoAlimentacaoRequest)
        : ResponseEntity<ProgramacaoAlimentacaoResponse> {
        val nova = service.criar(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(nova)
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualiza uma programacao de alimentacao existente",
        description = "Atualiza apenas se pertencer a loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Atualizada com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados invalidos"),
            ApiResponse(responseCode = "404", description = "Programacao ou aquario nao encontrado na loja")
        ]
    )
    fun atualizar(
        @Parameter(description = "ID da programacao que sera atualizada") @PathVariable id: Long,
        @Valid @RequestBody request: ProgramacaoAlimentacaoRequest
    ): ResponseEntity<ProgramacaoAlimentacaoResponse> {
        return ResponseEntity.ok(service.atualizar(id, request))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Exclui uma programacao de alimentacao",
        description = "Remove apenas se pertencer a loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Excluida com sucesso"),
            ApiResponse(responseCode = "404", description = "Programacao nao encontrada na loja")
        ]
    )
    fun excluir(
        @Parameter(description = "ID da programacao que sera excluida") @PathVariable id: Long
    ): ResponseEntity<Unit> {
        service.excluir(id)
        return ResponseEntity.noContent().build()
    }
}
