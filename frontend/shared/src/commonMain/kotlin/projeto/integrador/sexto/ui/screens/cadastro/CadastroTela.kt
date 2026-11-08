package projeto.integrador.sexto.ui.screens.cadastro

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import projeto.integrador.sexto.generated.resources.Res
import projeto.integrador.sexto.generated.resources.email
import projeto.integrador.sexto.generated.resources.lock
import projeto.integrador.sexto.generated.resources.usuario
import projeto.integrador.sexto.ui.components.Botao
import projeto.integrador.sexto.ui.components.CampoTexto
import projeto.integrador.sexto.ui.theme.AzulFundo
import projeto.integrador.sexto.ui.theme.Ciano
import projeto.integrador.sexto.ui.theme.CinzaTexto
import projeto.integrador.sexto.ui.theme.RosaErro

@Composable
fun CadastroTela(
    aoVoltarParaLogin: () -> Unit,
    aoRegistrarComSucesso: (nomeUsuario: String) -> Unit
) {
    val viewModel = remember { CadastroViewModel() }
    val state = viewModel.uiState
    val scrollState = rememberScrollState()

    LaunchedEffect(state.cadastroRealizadoComSucesso) {
        state.cadastroRealizadoComSucesso?.let { nome ->
            aoRegistrarComSucesso(nome)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulFundo)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 380.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Registre seu perfil",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Controle e registre dados do seu aquário",
                color = CinzaTexto,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(28.dp))

            CampoTexto(
                valor = state.nome,
                aoMudarValor = viewModel::aoMudarNome,
                rotulo = "NOME",
                placeholder = "Nemo Peixoto",
                iconeEsquerda = Res.drawable.usuario,
                mensagemErro = state.erroNome,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(Modifier.height(14.dp))

            CampoTexto(
                valor = state.email,
                aoMudarValor = viewModel::aoMudarEmail,
                rotulo = "E-MAIL",
                placeholder = "peixoto@gmail.com",
                iconeEsquerda = Res.drawable.email,
                mensagemErro = state.erroEmail,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(Modifier.height(14.dp))

            CampoTexto(
                valor = state.senha,
                aoMudarValor = viewModel::aoMudarSenha,
                rotulo = "SENHA",
                placeholder = "••••••••••••",
                iconeEsquerda = Res.drawable.lock,
                eSenha = true,
                mensagemErro = state.erroSenha,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(Modifier.height(14.dp))

            CampoTexto(
                valor = state.confirmarSenha,
                aoMudarValor = viewModel::aoMudarConfirmarSenha,
                rotulo = "CONFIRME SUA SENHA",
                placeholder = "••••••••••••",
                iconeEsquerda = Res.drawable.lock,
                eSenha = true,
                mensagemErro = state.erroConfirmarSenha,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            state.erroGeral?.let { msg ->
                Spacer(Modifier.height(8.dp))
                Text(
                    text = msg,
                    color = RosaErro
                )
            }

            Spacer(Modifier.height(28.dp))

            Botao(
                texto = if (state.carregando) "Cadastrando..." else "Criar Conta",
                onClick = { viewModel.tentarCadastrar() },
                habilitado = !state.carregando
            )

            if (state.carregando) {
                Spacer(Modifier.height(12.dp))
                CircularProgressIndicator(color = Ciano)
            }

            Spacer(Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Já possui uma conta? ",
                    color = CinzaTexto,
                    fontSize = 13.sp
                )
                Text(
                    text = "Faça seu login",
                    color = Ciano,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { aoVoltarParaLogin() }
                )
            }
        }
    }
}