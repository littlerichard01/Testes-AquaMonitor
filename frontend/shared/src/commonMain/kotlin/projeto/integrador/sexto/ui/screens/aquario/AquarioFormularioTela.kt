package projeto.integrador.sexto.ui.screens.aquario

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
import projeto.integrador.sexto.ui.components.CampoSelect
import projeto.integrador.sexto.ui.components.CampoTexto
import projeto.integrador.sexto.ui.theme.AzulFundo
import projeto.integrador.sexto.ui.theme.Ciano
import projeto.integrador.sexto.ui.theme.RosaErro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AquarioFormularioTela(
    aoVoltar: () -> Unit
) {
    val viewModel = remember { AquarioFormularioViewModel() }
    val state = viewModel.uiState
    val scrollState = rememberScrollState()

    val tiposAquario = listOf("AGUA_DOCE", "AGUA_SALGADA")
    val statusAquario = listOf("ATIVO", "INATIVO", "VENDIDO")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastrar Aquario") },
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
                placeholder = "Ex: Aquario Marinho 100L",
                mensagemErro = state.erroNome,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.descricao,
                aoMudarValor = viewModel::aoMudarDescricao,
                rotulo = "DESCRICAO",
                placeholder = "Opcional",
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.volumeLitros,
                aoMudarValor = viewModel::aoMudarVolumeLitros,
                rotulo = "VOLUME (LITROS)",
                placeholder = "Ex: 100",
                mensagemErro = state.erroVolumeLitros,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )

            CampoSelect(
                valor = state.tipo,
                opcoes = tiposAquario,
                aoSelecionar = viewModel::aoMudarTipo,
                rotulo = "TIPO",
                placeholder = "Selecione o tipo",
                mensagemErro = state.erroTipo
            )

            CampoTexto(
                valor = state.localizacao,
                aoMudarValor = viewModel::aoMudarLocalizacao,
                rotulo = "LOCALIZACAO",
                placeholder = "Opcional",
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            CampoSelect(
                valor = state.status,
                opcoes = statusAquario,
                aoSelecionar = viewModel::aoMudarStatus,
                rotulo = "STATUS",
                placeholder = "Selecione o status"
            )

            CampoTexto(
                valor = state.preco,
                aoMudarValor = viewModel::aoMudarPreco,
                rotulo = "PRECO (R$)",
                placeholder = "Ex: 250.00",
                mensagemErro = state.erroPreco,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                )
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