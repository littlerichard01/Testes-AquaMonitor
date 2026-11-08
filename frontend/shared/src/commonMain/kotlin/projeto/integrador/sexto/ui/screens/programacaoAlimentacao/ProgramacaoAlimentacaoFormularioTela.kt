package projeto.integrador.sexto.ui.screens.programacaoAlimentacao

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Switch
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
fun ProgramacaoAlimentacaoFormularioTela(
    aoVoltar: () -> Unit
) {
    val viewModel = remember { ProgramacaoAlimentacaoFormularioViewModel() }
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
                        text = "Cadastrar Programacao de Alimentacao",
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
                valor = state.horario,
                aoMudarValor = viewModel::aoMudarHorario,
                rotulo = "HORARIO",
                placeholder = "Ex: 08:30",
                mensagemErro = state.erroHorario,
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
                valor = state.aquarioId,
                aoMudarValor = viewModel::aoMudarAquarioId,
                rotulo = "ID DO AQUARIO",
                placeholder = "Ex: 1",
                mensagemErro = state.erroAquarioId,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (state.ativo) "Programacao ativa" else "Programacao inativa",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Switch(
                    checked = state.ativo,
                    onCheckedChange = viewModel::aoMudarAtivo
                )
            }

            state.erroGeral?.let { mensagem ->
                Text(
                    text = mensagem,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            state.mensagemSucesso?.let { mensagem ->
                Text(
                    text = mensagem,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(8.dp))

            Botao(
                texto = if (state.carregando) "Gravando..." else "Gravar",
                onClick = viewModel::gravar,
                habilitado = !state.carregando
            )

            Botao(
                texto = "Limpar Campos",
                onClick = viewModel::limparCampos,
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