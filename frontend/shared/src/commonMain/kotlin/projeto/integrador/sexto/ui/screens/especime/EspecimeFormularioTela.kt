package projeto.integrador.sexto.ui.screens.especime

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import projeto.integrador.sexto.generated.resources.Res
import projeto.integrador.sexto.generated.resources.voltar
import projeto.integrador.sexto.ui.components.Botao
import projeto.integrador.sexto.ui.components.CampoTexto
import projeto.integrador.sexto.ui.theme.AzulFundo
import projeto.integrador.sexto.ui.theme.Ciano
import projeto.integrador.sexto.ui.theme.RosaErro
import projeto.integrador.sexto.ui.components.CampoSelect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EspecimeFormularioTela(
    aoVoltar: () -> Unit
) {
    val viewModel = remember { EspecimeFormularioViewModel() }
    val state = viewModel.uiState
    val scrollState = rememberScrollState()
    val statusEspecime = listOf(
        "DISPONIVEL",
        "VENDIDO",
        "INDISPONIVEL"
    )

    DisposableEffect(viewModel) {
        onDispose {
            viewModel.onCleared()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastrar Especime") },
                navigationIcon = {
                    IconButton(onClick = aoVoltar) {
                        Icon(
                            painter = painterResource(Res.drawable.voltar),
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        },
        containerColor = AzulFundo
    ) { paddingInterno ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {

            CampoTexto(
                valor = state.identificacao,
                aoMudarValor = viewModel::aoMudarIdentificacao,
                rotulo = "IDENTIFICACAO",
                placeholder = "Ex: PEIXE-034",
                mensagemErro = state.erros["identificacao"],
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.preco,
                aoMudarValor = viewModel::aoMudarPreco,
                rotulo = "PRECO (R$)",
                placeholder = "Ex: 120,00",
                mensagemErro = state.erros["preco"],
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )

            CampoSelect(
                valor = state.status,
                opcoes = statusEspecime,
                aoSelecionar = viewModel::aoMudarStatus,
                rotulo = "STATUS",
                placeholder = "Selecione o status",
                mensagemErro = state.erros["status"]
            )


            CampoTexto(
                valor = state.observacoes,
                aoMudarValor = viewModel::aoMudarObservacoes,
                rotulo = "OBSERVACOES",
                placeholder = "Opcional",
                mensagemErro = state.erros["observacoes"],
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.dataEntrada,
                aoMudarValor = viewModel::aoMudarDataEntrada,
                rotulo = "DATA DE ENTRADA",
                placeholder = "AAAA-MM-DD",
                mensagemErro = state.erros["dataEntrada"],
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.especieId,
                aoMudarValor = viewModel::aoMudarEspecieId,
                rotulo = "ID DA ESPECIE",
                placeholder = "Ex: 1",
                mensagemErro = state.erros["especieId"],
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.aquarioId,
                aoMudarValor = viewModel::aoMudarAquarioId,
                rotulo = "ID DO AQUARIO",
                placeholder = "Opcional",
                mensagemErro = state.erros["aquarioId"],
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            Text(
                text = "Informe IDs existentes. Deixe o aquario vazio " +
                        "se o especime ainda nao estiver alocado.",
                modifier = Modifier.padding(vertical = 4.dp)
            )

            state.erroGeral?.let { mensagem ->
                Text(
                    text = mensagem,
                    color = RosaErro,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            state.mensagemSucesso?.let { mensagem ->
                Text(
                    text = mensagem,
                    color = Ciano,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            Botao(
                texto = if (state.carregando) "Gravando..." else "Gravar",
                onClick = { viewModel.gravar() },
                habilitado = !state.carregando
            )

            Botao(
                texto = "Limpar Campos",
                onClick = { viewModel.limparCampos() },
                habilitado = !state.carregando
            )

            if (state.carregando) {
                Spacer(Modifier.height(8.dp))

                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Ciano
                )
            }
        }
    }
}