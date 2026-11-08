package projeto.integrador.sexto.ui.screens.loja

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LojaFormularioTela(
    aoVoltar: () -> Unit
) {
    val viewModel = remember { LojaFormularioViewModel() }
    val state = viewModel.uiState
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastrar Loja") },
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
                rotulo = "NOME DA LOJA",
                placeholder = "Ex: AquaPeixes",
                mensagemErro = state.erroNome,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.email,
                aoMudarValor = viewModel::aoMudarEmail,
                rotulo = "E-MAIL",
                placeholder = "loja@email.com",
                mensagemErro = state.erroEmail,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            CampoTexto(
                valor = state.senha,
                aoMudarValor = viewModel::aoMudarSenha,
                rotulo = "SENHA",
                placeholder = "••••••••",
                eSenha = true,
                mensagemErro = state.erroSenha,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Loja ativa", color = Color.White)
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = state.ativo,
                    onCheckedChange = viewModel::aoMudarAtivo
                )
            }

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