package projeto.integrador.sexto.ui.screens.comprador

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompradorFormularioTela(
    aoVoltar: () -> Unit
) {
    val viewModel = remember { CompradorFormularioViewModel() }
    DisposableEffect(Unit) {
        onDispose { viewModel.onCleared() }
    }
    val state = viewModel.uiState
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cadastrar Comprador",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = aoVoltar) {
                        Icon(
                            painter = painterResource(Res.drawable.voltar),
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingInterno ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            CampoTexto(
                valor = state.nome,
                aoMudarValor = viewModel::aoMudarNome,
                rotulo = "NOME",
                placeholder = "Ex: Joao da Silva",
                mensagemErro = state.erroNome,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.cpf,
                aoMudarValor = viewModel::aoMudarCpf,
                rotulo = "CPF",
                placeholder = "Opcional - 11 digitos",
                mensagemErro = state.erroCpf,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.email,
                aoMudarValor = viewModel::aoMudarEmail,
                rotulo = "EMAIL",
                placeholder = "Opcional",
                mensagemErro = state.erroEmail,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.telefone,
                aoMudarValor = viewModel::aoMudarTelefone,
                rotulo = "TELEFONE",
                placeholder = "Opcional - com DDD",
                mensagemErro = state.erroTelefone,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.observacoes,
                aoMudarValor = viewModel::aoMudarObservacoes,
                rotulo = "OBSERVACOES",
                placeholder = "Opcional",
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )

            state.erroGeral?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            state.mensagemSucesso?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
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
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}