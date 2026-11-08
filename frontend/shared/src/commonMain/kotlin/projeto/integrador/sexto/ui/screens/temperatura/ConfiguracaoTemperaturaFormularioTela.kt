package projeto.integrador.sexto.ui.screens.configuracaotemperatura

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracaoTemperaturaFormularioTela(
    aoVoltar: () -> Unit
) {
    val viewModel = remember { ConfiguracaoTemperaturaFormularioViewModel() }
    val state = viewModel.uiState
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Configurar Temperatura", color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    IconButton(onClick = aoVoltar) {
                        Icon(
                            painter = painterResource(Res.drawable.voltar),
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
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
                valor = state.temperaturaMinima,
                aoMudarValor = viewModel::aoMudarTemperaturaMinima,
                rotulo = "TEMPERATURA MÍNIMA (°C)",
                placeholder = "Ex: 22.5",
                mensagemErro = state.erroTemperaturaMinima,
                tecladoOpcoes = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next)
            )

            CampoTexto(
                valor = state.temperaturaMaxima,
                aoMudarValor = viewModel::aoMudarTemperaturaMaxima,
                rotulo = "TEMPERATURA MÁXIMA (°C)",
                placeholder = "Ex: 28.0",
                mensagemErro = state.erroTemperaturaMaxima,
                tecladoOpcoes = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next)
            )

            CampoTexto(
                valor = state.aquarioId,
                aoMudarValor = viewModel::aoMudarAquarioId,
                rotulo = "ID DO AQUÁRIO",
                placeholder = "Ex: 2",
                mensagemErro = state.erroAquarioId,
                tecladoOpcoes = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
            )

            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (state.ativo) "Configuração Ativa" else "Configuração Inativa",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Switch(
                    checked = state.ativo,
                    onCheckedChange = viewModel::aoMudarAtivo,
                    enabled = !state.carregando
                )
            }

            state.erroGeral?.let { msg ->
                Text(text = msg, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }

            state.mensagemSucesso?.let { msg ->
                Text(text = msg, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium)
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