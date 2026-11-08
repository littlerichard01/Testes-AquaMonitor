package projeto.integrador.sexto.ui.screens.login

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
import androidx.compose.foundation.text.KeyboardOptions
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
import projeto.integrador.sexto.ui.components.Botao
import projeto.integrador.sexto.ui.components.CampoTexto
import projeto.integrador.sexto.ui.theme.AzulFundo
import projeto.integrador.sexto.ui.theme.Ciano
import projeto.integrador.sexto.ui.theme.CinzaTexto
import projeto.integrador.sexto.ui.theme.RosaErro

@Composable
fun LoginTela(
    aoLogarComSucesso: (nomeUsuario: String) -> Unit,
    aoNavegarParaCadastro: () -> Unit
) {
    val viewModel = remember { LoginViewModel() }
    val state = viewModel.uiState

    LaunchedEffect(state.loginRealizadoComSucesso) {
        state.loginRealizadoComSucesso?.let { nome ->
            aoLogarComSucesso(nome)
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
                .widthIn(max = 380.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Text(
                    text = "Aqua",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Monitor",
                    color = Ciano,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Monitore seu aquário",
                color = Color.White,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(24.dp))

            state.mensagemInformativa?.let { msg ->
                Text(
                    text = msg,
                    color = Ciano,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
            }

            CampoTexto(
                valor = state.email,
                aoMudarValor = viewModel::aoMudarEmail,
                rotulo = "E-MAIL:",
                placeholder = "peixoto@gmail.com",
                iconeEsquerda = Res.drawable.email,
                mensagemErro = state.erroEmail,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(Modifier.height(16.dp))

            CampoTexto(
                valor = state.senha,
                aoMudarValor = viewModel::aoMudarSenha,
                rotulo = "SENHA:",
                placeholder = "Sua senha",
                iconeEsquerda = Res.drawable.lock,
                eSenha = true,
                mensagemErro = state.erroSenha,
                tecladoOpcoes = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "Esqueci minha senha",
                    color = RosaErro,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { }
                )
            }

            state.erroGeral?.let { msg ->
                Spacer(Modifier.height(8.dp))
                Text(
                    text = msg,
                    color = RosaErro
                )
            }

            Spacer(Modifier.height(28.dp))

            Botao(
                texto = if (state.carregando) "Entrando..." else "Entrar",
                onClick = viewModel::tentarLogar,
                habilitado = !state.carregando
            )

            if (state.carregando) {
                Spacer(Modifier.height(12.dp))
                CircularProgressIndicator(color = Ciano)
            }

            Spacer(Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Ainda não tem uma conta? ",
                    color = CinzaTexto,
                    fontSize = 13.sp
                )
                Text(
                    text = "Cadastre-se",
                    color = Ciano,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { aoNavegarParaCadastro() }
                )
            }
        }
    }
}