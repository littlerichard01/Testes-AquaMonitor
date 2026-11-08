package projeto.integrador.sexto.ui.screens.especime

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import projeto.integrador.sexto.data.dto.EspecimeRequestDto
import projeto.integrador.sexto.data.remote.AquaMonitorApi
import projeto.integrador.sexto.data.remote.ResultadoApi
import projeto.integrador.sexto.di.ProvedorDependencias

class EspecimeFormularioViewModel(
    private val api: AquaMonitorApi = ProvedorDependencias.api
) {

    data class UiState(
        val identificacao: String = "",
        val preco: String = "",
        val status: String = "DISPONIVEL",
        val observacoes: String = "",
        val dataEntrada: String = "",
        val especieId: String = "",
        val aquarioId: String = "",
        val carregando: Boolean = false,
        val erros: Map<String, String> = emptyMap(),
        val erroGeral: String? = null,
        val mensagemSucesso: String? = null
    )

    var uiState by mutableStateOf(UiState())
        private set

    private val scope =
        CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private fun atualizarCampo(
        campo: String,
        novoEstado: UiState
    ) {
        uiState = novoEstado.copy(
            erros = novoEstado.erros - campo,
            erroGeral = null,
            mensagemSucesso = null
        )
    }

    fun aoMudarIdentificacao(valor: String) {
        atualizarCampo(
            "identificacao",
            uiState.copy(identificacao = valor)
        )
    }

    fun aoMudarPreco(valor: String) {
        atualizarCampo("preco", uiState.copy(preco = valor))
    }

    fun aoMudarStatus(valor: String) {
        atualizarCampo("status", uiState.copy(status = valor))
    }

    fun aoMudarObservacoes(valor: String) {
        atualizarCampo(
            "observacoes",
            uiState.copy(observacoes = valor)
        )
    }

    fun aoMudarDataEntrada(valor: String) {
        atualizarCampo(
            "dataEntrada",
            uiState.copy(dataEntrada = valor)
        )
    }

    fun aoMudarEspecieId(valor: String) {
        atualizarCampo("especieId", uiState.copy(especieId = valor))
    }

    fun aoMudarAquarioId(valor: String) {
        atualizarCampo("aquarioId", uiState.copy(aquarioId = valor))
    }

    fun limparCampos() {
        if (uiState.carregando) return
        uiState = UiState()
    }

    fun gravar() {
        if (uiState.carregando) return


        val dados = uiState
        val erros = mutableMapOf<String, String>()

        val identificacao = dados.identificacao.trim()
        val preco = dados.preco.trim()
            .replace(',', '.')
            .toDoubleOrNull()

        val status = dados.status.trim().uppercase()
        val dataEntrada = dados.dataEntrada.trim()
        val especieId = dados.especieId.trim().toLongOrNull()

        val aquarioTexto = dados.aquarioId.trim()
        val aquarioId = aquarioTexto.toLongOrNull()

        if (identificacao.isBlank()) {
            erros["identificacao"] = "Informe a identificacao."
        }

        if (preco == null || !preco.isFinite() || preco <= 0.0) {
            erros["preco"] = "Informe um preco maior que zero."
        }

        if (status !in setOf(
                "DISPONIVEL",
                "VENDIDO",
                "INDISPONIVEL"
            )
        ) {
            erros["status"] =
                "Use DISPONIVEL, VENDIDO ou INDISPONIVEL."
        }

        if (!dataValida(dataEntrada)) {
            erros["dataEntrada"] =
                "Informe uma data valida no formato AAAA-MM-DD."
        }

        if (especieId == null || especieId <= 0L) {
            erros["especieId"] = "Informe um ID de especie positivo."
        }

        if (aquarioTexto.isNotEmpty() &&
            (aquarioId == null || aquarioId <= 0L)
        ) {
            erros["aquarioId"] =
                "Informe um ID de aquario positivo ou deixe vazio."
        }

        if (erros.isNotEmpty()) {
            uiState = uiState.copy(
                erros = erros,
                erroGeral = null,
                mensagemSucesso = null
            )
            return
        }


        val request = EspecimeRequestDto(
            identificacao = identificacao,
            preco = preco!!,
            status = status,
            observacoes = dados.observacoes.trim()
                .ifBlank { null },
            dataEntrada = dataEntrada,
            especieId = especieId!!,
            aquarioId = aquarioId
        )

        uiState = uiState.copy(
            carregando = true,
            erros = emptyMap(),
            erroGeral = null,
            mensagemSucesso = null
        )

        scope.launch {
            try {
                when (val resultado = api.criarEspecime(request)) {
                    is ResultadoApi.Sucesso -> {
                        // Limpa os campos e preserva a mensagem.
                        uiState = UiState(
                            mensagemSucesso =
                                "Especime cadastrado com sucesso! " +
                                        "ID: ${resultado.dados.id}"
                        )
                    }

                    is ResultadoApi.Falha -> {
                        uiState = uiState.copy(
                            carregando = false,
                            erroGeral = resultado.mensagem
                        )
                    }
                }
            } finally {
                uiState = uiState.copy(carregando = false)
            }
        }
    }

    private fun dataValida(valor: String): Boolean {
        if (!Regex("""\d{4}-\d{2}-\d{2}""").matches(valor)) {
            return false
        }

        val partes = valor.split("-")
        val ano = partes[0].toIntOrNull() ?: return false
        val mes = partes[1].toIntOrNull() ?: return false
        val dia = partes[2].toIntOrNull() ?: return false

        if (ano !in 1..9999 || mes !in 1..12) return false

        val bissexto =
            ano % 400 == 0 || (ano % 4 == 0 && ano % 100 != 0)

        val diasNoMes = when (mes) {
            2 -> if (bissexto) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }

        return dia in 1..diasNoMes
    }

    fun onCleared() {
        scope.cancel()
    }
}