package projeto.integrador.sexto.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import projeto.integrador.sexto.di.ProvedorDependencias
import projeto.integrador.sexto.data.BASE_URL_BACKEND
import projeto.integrador.sexto.data.dto.AquarioRequestDto
import projeto.integrador.sexto.data.dto.AquarioResponseDto
import projeto.integrador.sexto.data.dto.LojaLoginRequestDto
import projeto.integrador.sexto.data.dto.LojaRequestDto
import projeto.integrador.sexto.data.dto.LojaResponseDto
import projeto.integrador.sexto.data.dto.LoginResponseDto
import projeto.integrador.sexto.data.dto.ConfiguracaoTemperaturaRequestDto
import projeto.integrador.sexto.data.dto.ConfiguracaoTemperaturaResponseDto
import projeto.integrador.sexto.data.dto.LeituraTemperaturaRequestDto
import projeto.integrador.sexto.data.dto.LeituraTemperaturaResponseDto
import projeto.integrador.sexto.data.dto.CompradorRequestDto
import projeto.integrador.sexto.data.dto.CompradorResponseDto
import projeto.integrador.sexto.data.dto.ProgramacaoAlimentacaoRequestDto
import projeto.integrador.sexto.data.dto.ProgramacaoAlimentacaoResponseDto
import projeto.integrador.sexto.data.dto.VendaRequestDto
import projeto.integrador.sexto.data.dto.VendaResponseDto
import projeto.integrador.sexto.data.dto.EspecieRequestDto
import projeto.integrador.sexto.data.dto.EspecieResponseDto
import projeto.integrador.sexto.data.dto.DispositivoIoTRequestDto
import projeto.integrador.sexto.data.dto.DispositivoIoTResponseDto
import projeto.integrador.sexto.data.dto.EspecimeRequestDto
import projeto.integrador.sexto.data.dto.EspecimeResponseDto
import projeto.integrador.sexto.data.dto.AlertaRequestDto
import projeto.integrador.sexto.data.dto.AlertaResponseDto
import projeto.integrador.sexto.data.local.AuthStore

class AquaMonitorApi(
    private val authStore: AuthStore = ProvedorDependencias.authStore
) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = false
    }

    val client: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        defaultRequest {
            url(BASE_URL_BACKEND)
        }
    }

    suspend fun login(email: String, senha: String): ResultadoApi<LoginResponseDto> {
        return executarChamadaPost(
            caminho = "/api/auth/signin",
            corpo = LojaLoginRequestDto(email = email.trim().lowercase(), senha = senha),
            requerAutenticacao = false
        )
    }

    suspend fun cadastrarLoja(
        nome: String,
        email: String,
        senha: String
    ): ResultadoApi<LojaResponseDto> {
        return executarChamadaPost(
            caminho = "/api/auth/signup",
            corpo = LojaRequestDto(
                nome = nome.trim(),
                email = email.trim().lowercase(),
                senha = senha
            )
        )
    }

    suspend fun criarAquario(
        request: AquarioRequestDto
    ): ResultadoApi<AquarioResponseDto> {
        return executarChamadaPost(
            caminho = "/api/aquarios",
            corpo = request,
            requerAutenticacao = true
        )
    }

    suspend fun criarComprador(
        request: CompradorRequestDto
    ): ResultadoApi<CompradorResponseDto> {
        return executarChamadaPost(
            caminho = "/api/compradores",
            corpo = request,
            requerAutenticacao = true
        )
    }

    suspend fun criarProgramacaoAlimentacao(
        request: ProgramacaoAlimentacaoRequestDto
    ): ResultadoApi<ProgramacaoAlimentacaoResponseDto> {
        return executarChamadaPost(
            caminho = "/api/programacoes-alimentacao",
            corpo = request,
            requerAutenticacao = true
        )
    }

    suspend fun criarVenda(
        request: VendaRequestDto
    ): ResultadoApi<VendaResponseDto> {
        return executarChamadaPost(
            caminho = "/api/vendas",
            corpo = request,
            requerAutenticacao = true
        )
    }

    suspend fun criarConfiguracaoTemperatura(
        request: ConfiguracaoTemperaturaRequestDto
    ): ResultadoApi<ConfiguracaoTemperaturaResponseDto> {
        return executarChamadaPost(
            caminho = "/api/configuracoes-temperatura",
            corpo = request,
            requerAutenticacao = true
        )
    }

    suspend fun criarLeituraTemperatura(
        request: LeituraTemperaturaRequestDto
    ): ResultadoApi<LeituraTemperaturaResponseDto> {
        return executarChamadaPost(
            caminho = "/api/leituras-temperatura",
            corpo = request,
            requerAutenticacao = true
        )
    }

    suspend fun criarEspecie(
        request: EspecieRequestDto
    ): ResultadoApi<EspecieResponseDto> {
        return executarChamadaPost(
            caminho = "/api/especies",
            corpo = request,
            requerAutenticacao = true
        )
    }

    suspend fun criarDispositivoIoT(
        request: DispositivoIoTRequestDto
    ): ResultadoApi<DispositivoIoTResponseDto> {
        return executarChamadaPost(
            caminho = "/api/dispositivos-iot",
            corpo = request,
            requerAutenticacao = true
        )
    }

    suspend fun criarEspecime(
        request: EspecimeRequestDto
    ): ResultadoApi<EspecimeResponseDto> {
        return executarChamadaPost(
            caminho = "/api/especimes",
            corpo = request,
            requerAutenticacao = true
        )
    }

    suspend fun criarAlerta(
        request: AlertaRequestDto
    ): ResultadoApi<AlertaResponseDto> {
        return executarChamadaPost(
            caminho = "/api/alertas",
            corpo = request,
            requerAutenticacao = true
        )
    }

    private suspend inline fun <reified T : Any> executarChamadaPost(
        caminho: String,
        corpo: Any,
        requerAutenticacao: Boolean = false
    ): ResultadoApi<T> {
        val cabecalhoAutorizacao = if (requerAutenticacao) {
            val token = authStore.obterToken()
            if (token.isNullOrBlank()) {
                return ResultadoApi.Falha(
                    codigo = 401,
                    mensagem = "Sessao ausente. Faca login antes de realizar esta operacao."
                )
            }
            "${authStore.obterTipoToken()} $token"
        } else {
            null
        }

        return try {
            val response: HttpResponse = client.post(caminho) {
                contentType(ContentType.Application.Json)
                cabecalhoAutorizacao?.let {
                    header(HttpHeaders.Authorization, it)
                }
                setBody(corpo)
            }
            when (response.status) {
                HttpStatusCode.OK,
                HttpStatusCode.Created -> ResultadoApi.Sucesso(response.body<T>())

                HttpStatusCode.BadRequest -> {
                    val mensagem = extrairMensagemErro(response)
                        ?: "Dados invalidos. Verifique os campos e tente novamente."
                    ResultadoApi.Falha(codigo = 400, mensagem = mensagem)
                }

                HttpStatusCode.Unauthorized -> {
                    val mensagem = extrairMensagemErro(response)
                        ?: "Email ou senha incorretos, ou conta inativa."
                    ResultadoApi.Falha(codigo = 401, mensagem = mensagem)
                }
                HttpStatusCode.NotFound -> {
                    val mensagem = extrairMensagemErro(response)
                        ?: "Recurso nao encontrado no servidor."
                    ResultadoApi.Falha(codigo = 404, mensagem = mensagem)
                }
                HttpStatusCode.InternalServerError -> ResultadoApi.Falha(
                    codigo = 500,
                    mensagem = "Erro interno no servidor. Tente novamente mais tarde."
                )

                else -> ResultadoApi.Falha(
                    codigo = response.status.value,
                    mensagem = "Falha na requisicao (codigo ${response.status.value})."
                )
            }
        } catch (ex: Exception) {
            val mensagem = when {
                ex.message?.contains("Connection refused", ignoreCase = true) == true ->
                    "Nao foi possivel conectar ao servidor. Verifique se o backend esta rodando na porta 8081."

                else -> "Falha de comunicacao: ${ex.message ?: "erro desconhecido"}"
            }
            ResultadoApi.Falha(codigo = 0, mensagem = mensagem)
        }
    }

    private suspend fun extrairMensagemErro(response: HttpResponse): String? {
        return runCatching {
            val texto = response.body<String>()
            if (texto.isBlank()) return null
            val possivelJson = runCatching { json.parseToJsonElement(texto) }.getOrNull()
                ?: return texto.take(250)
            val obj = possivelJson.jsonObject

            fun extrairTexto(chave: String): String? {
                val elemento = obj[chave] as? JsonPrimitive ?: return null
                return if (elemento.isString) elemento.content else elemento.toString()
            }

            val mensagem = extrairTexto("mensagem")
                ?: extrairTexto("message")
                ?: extrairTexto("error")
                ?: extrairTexto("erro")
                ?: texto.take(250)

            val errosCampos = (obj["errosCampos"] as? JsonObject)
                ?.mapNotNull { (campo, erro) ->
                    val detalhe = (erro as? JsonPrimitive)
                        ?.takeIf { it.isString }
                        ?.content
                        ?: return@mapNotNull null
                    "$campo: $detalhe"
                }
                ?.takeIf { it.isNotEmpty() }
                ?.joinToString("; ")

            if (errosCampos != null) "$mensagem: $errosCampos" else mensagem
        }.getOrNull()
    }
}

sealed interface ResultadoApi<out T> {
    data class Sucesso<T>(val dados: T) : ResultadoApi<T>
    data class Falha(val codigo: Int, val mensagem: String) : ResultadoApi<Nothing>
}
