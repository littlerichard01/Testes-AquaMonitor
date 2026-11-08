package projeto.integrador.sexto.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import projeto.integrador.sexto.ui.components.Botao
import projeto.integrador.sexto.ui.theme.AzulFundo
import projeto.integrador.sexto.ui.theme.Ciano
import projeto.integrador.sexto.ui.theme.CinzaTexto

private val entidadesDashboard = listOf(
    "Cadastrar Loja",
    "Especies",
    "Aquarios",
    "Compradores",
    "Especimes",
    "Configuracoes de Temperatura",
    "Programacoes de Alimentacao",
    "Alertas",
    "Dispositivos IoT",
    "Leituras de Temperatura",
    "Vendas"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTela(
    nomeUsuario: String,
    aoSair: () -> Unit,
    aoSelecionarEntidade: (String) -> Unit
) {
    val viewModel = remember(nomeUsuario) {
        HomeViewModel(nomeUsuario)
    }
    val state = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "AquaMonitor") }
            )
        },
        containerColor = AzulFundo
    ) { paddingInterno ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                if (state.carregando) {
                    CircularProgressIndicator(color = Ciano)
                } else {
                    Text(
                        text = state.saudacao,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            item {
                Text(
                    text = "Escolha uma entidade para testar as rotas:",
                    fontSize = 16.sp,
                    color = CinzaTexto
                )
            }

            items(entidadesDashboard) { entidade ->
                Botao(
                    texto = entidade,
                    onClick = {
                        viewModel.abrirEntidade(entidade)
                        aoSelecionarEntidade(entidade)
                    }
                )
            }

            item {
                Botao(
                    texto = "Sair",
                    onClick = {
                        viewModel.pedirParaSair()
                        aoSair()
                    }
                )
            }
        }
    }
}