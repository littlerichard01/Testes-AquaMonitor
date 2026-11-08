package com.example.backend.controller

import com.example.backend.dto.DispositivoIoTRequest
import com.example.backend.dto.DispositivoIoTResponse
import com.example.backend.service.DispositivoIoTService
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
@RequestMapping("/api/dispositivos-iot")
@Tag(
    name = "Dispositivos IoT",
    description = "CRUD de Dispositivos IoT (ESP32). Um dispositivo pode estar alocado a 1 aquario (opcional). Apenas os alocados a aquarios da loja autenticada aparecem."
)
class DispositivoIoTController(
    private val service: DispositivoIoTService
) {

    @GetMapping
    @Operation(
        summary = "Lista dispositivos IoT alocados a aquarios da loja autenticada",
        description = "Por enquanto, dispositivos sem aquario associado nao aparecem em listagens."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", useReturnTypeSchema = true)
        ]
    )
    fun listar(): ResponseEntity<List<DispositivoIoTResponse>> {
        return ResponseEntity.ok(service.listarTodosDaLoja())
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Busca um dispositivo por ID",
        description = "404 se nao existir ou nao estiver alocado a aquario da loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Dispositivo encontrado"),
            ApiResponse(
                responseCode = "404", description = "Dispositivo nao encontrado na loja",
                content = [Content(schema = Schema(implementation = Any::class))]
            )
        ]
    )
    fun buscarPorId(
        @Parameter(description = "ID do dispositivo") @PathVariable id: Long
    ): ResponseEntity<DispositivoIoTResponse> {
        return ResponseEntity.ok(service.buscarPorIdDaLoja(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Cria um novo dispositivo IoT",
        description = "Valida unicidade de identificacao (MAC) e, se informado, que o aquario pertence a loja autenticada."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Dispositivo criado com sucesso"),
            ApiResponse(responseCode = "400", description = "Identificacao duplicada ou aquario de outra loja"),
            ApiResponse(responseCode = "404", description = "Aquario referenciado nao encontrado na loja")
        ]
    )
    fun criar(@Valid @RequestBody request: DispositivoIoTRequest): ResponseEntity<DispositivoIoTResponse> {
        val novo = service.criar(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(novo)
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualiza um dispositivo existente",
        description = "Atualiza apenas se pertencer a loja autenticada (via aquario)."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados invalidos"),
            ApiResponse(responseCode = "404", description = "Dispositivo ou aquario nao encontrado na loja")
        ]
    )
    fun atualizar(
        @Parameter(description = "ID do dispositivo que sera atualizado") @PathVariable id: Long,
        @Valid @RequestBody request: DispositivoIoTRequest
    ): ResponseEntity<DispositivoIoTResponse> {
        return ResponseEntity.ok(service.atualizar(id, request))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Exclui um dispositivo IoT",
        description = "Exclui apenas se pertencer a loja autenticada (via aquario)."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Excluido com sucesso"),
            ApiResponse(responseCode = "404", description = "Dispositivo nao encontrado na loja")
        ]
    )
    fun excluir(
        @Parameter(description = "ID do dispositivo que sera excluido") @PathVariable id: Long
    ): ResponseEntity<Unit> {
        service.excluir(id)
        return ResponseEntity.noContent().build()
    }
}
