package projeto.integrador.sexto.ui.screens.especie

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
fun EspecieFormularioTela(
    aoVoltar: () -> Unit
) {
    val viewModel = remember { EspecieFormularioViewModel() }
    val state = viewModel.uiState
    val scrollState = rememberScrollState()
    val tiposEspecie = listOf("PEIXE", "CRUSTACEO", "MOLUSCO", "CNIDARIO", "OUTRO")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastrar Espécie") },
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
                valor = state.nomePopular,
                aoMudarValor = viewModel::aoMudarNomePopular,
                rotulo = "NOME POPULAR",
                placeholder = "Ex: Peixe Palhaço",
                mensagemErro = state.erroNomePopular,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.nomeCientifico,
                aoMudarValor = viewModel::aoMudarNomeCientifico,
                rotulo = "NOME CIENTÍFICO",
                placeholder = "Ex: Amphiprioninae",
                mensagemErro = state.erroNomeCientifico,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )

            CampoTexto(
                valor = state.descricao,
                aoMudarValor = viewModel::aoMudarDescricao,
                rotulo = "DESCRIÇÃO",
                placeholder = "Detalhes sobre a espécie",
                mensagemErro = state.erroDescricao,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.temperaturaMinima,
                aoMudarValor = viewModel::aoMudarTemperaturaMinima,
                rotulo = "TEMPERATURA MÍNIMA (°C)",
                placeholder = "Ex: 24.5",
                mensagemErro = state.erroTemperaturaMinima,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.temperaturaMaxima,
                aoMudarValor = viewModel::aoMudarTemperaturaMaxima,
                rotulo = "TEMPERATURA MÁXIMA (°C)",
                placeholder = "Ex: 28.0",
                mensagemErro = state.erroTemperaturaMaxima,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            CampoSelect(
                valor = state.tipo,
                opcoes = tiposEspecie,
                aoSelecionar = viewModel::aoMudarTipo,
                rotulo = "TIPO",
                placeholder = "Selecione o tipo",
                mensagemErro = state.erroTipo
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