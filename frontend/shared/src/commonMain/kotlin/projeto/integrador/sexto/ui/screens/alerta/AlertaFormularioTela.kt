package projeto.integrador.sexto.ui.screens.alerta

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Switch
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
fun AlertaFormularioTela(
    aoVoltar: () -> Unit
) {
    val viewModel = remember { AlertaFormularioViewModel() }
    val state = viewModel.uiState
    val scrollState = rememberScrollState()
    val tiposAlerta = listOf(
        "TEMPERATURA",
        "ALIMENTACAO"
    )

    val severidadesAlerta = listOf(
        "BAIXA",
        "MEDIA",
        "ALTA"
    )

    DisposableEffect(viewModel) {
        onDispose {
            viewModel.onCleared()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastrar Alerta") },
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

            CampoSelect(
                valor = state.tipo,
                opcoes = tiposAlerta,
                aoSelecionar = viewModel::aoMudarTipo,
                rotulo = "TIPO",
                placeholder = "Selecione o tipo",
                mensagemErro = state.erroTipo
            )

            CampoSelect(
                valor = state.severidade,
                opcoes = severidadesAlerta,
                aoSelecionar = viewModel::aoMudarSeveridade,
                rotulo = "SEVERIDADE",
                placeholder = "Selecione a severidade",
                mensagemErro = state.erroSeveridade
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Alerta ativo")

                Spacer(Modifier.weight(1f))

                Switch(
                    checked = state.ativo,
                    onCheckedChange = viewModel::aoMudarAtivo,
                    enabled = !state.carregando
                )
            }

            CampoTexto(
                valor = state.aquarioId,
                aoMudarValor = viewModel::aoMudarAquarioId,
                rotulo = "ID DO AQUARIO",
                placeholder = "Ex: 3",
                mensagemErro = state.erroAquarioId,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            Text(
                text = "Informe o ID de um aquario da sua loja.",
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