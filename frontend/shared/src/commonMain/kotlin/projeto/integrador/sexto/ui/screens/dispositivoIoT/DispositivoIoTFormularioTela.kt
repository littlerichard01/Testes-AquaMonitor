package projeto.integrador.sexto.ui.screens.dispositivoiot

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
fun DispositivoIoTFormularioTela(
    aoVoltar: () -> Unit
) {
    val viewModel = remember { DispositivoIoTFormularioViewModel() }
    val state = viewModel.uiState
    val scrollState = rememberScrollState()
    val statusDispositivo = listOf("ONLINE", "OFFLINE", "INATIVO")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastrar Dispositivo IoT") },
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
                valor = state.nome,
                aoMudarValor = viewModel::aoMudarNome,
                rotulo = "NOME",
                placeholder = "Ex: Sensor de Temperatura A1",
                mensagemErro = state.erroNome,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.identificacao,
                aoMudarValor = viewModel::aoMudarIdentificacao,
                rotulo = "IDENTIFICAÇÃO (MAC ADDRESS)",
                placeholder = "Ex: 00:1B:44:11:3A:B7",
                mensagemErro = state.erroIdentificacao,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.mqttTopic,
                aoMudarValor = viewModel::aoMudarMqttTopic,
                rotulo = "TÓPICO MQTT",
                placeholder = "Ex: aquarios/1/temperatura",
                mensagemErro = state.erroMqttTopic,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )

            CampoSelect(
                valor = state.status,
                opcoes = statusDispositivo,
                aoSelecionar = viewModel::aoMudarStatus,
                rotulo = "STATUS",
                placeholder = "Selecione o status",
                mensagemErro = state.erroStatus
            )

            state.erroGeral?.let { msg ->
                Text(
                    text = msg,
                    color = RosaErro,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            state.mensagemSucesso?.let { msg ->
                Text(
                    text = msg,
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