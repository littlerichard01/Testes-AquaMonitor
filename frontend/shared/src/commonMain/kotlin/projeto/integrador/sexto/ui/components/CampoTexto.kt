package projeto.integrador.sexto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import projeto.integrador.sexto.ui.theme.AzulCampo
import projeto.integrador.sexto.ui.theme.Ciano
import projeto.integrador.sexto.ui.theme.CinzaTexto
import projeto.integrador.sexto.ui.theme.RosaErro

@Composable
fun CampoTexto(
    valor: String,
    aoMudarValor: (String) -> Unit,
    rotulo: String,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    iconeEsquerda: DrawableResource? = null,
    eSenha: Boolean = false,
    mensagemErro: String? = null,
    tecladoOpcoes: KeyboardOptions = KeyboardOptions.Default,
    acoesTeclado: KeyboardActions = KeyboardActions.Default
) {
    val temErro = !mensagemErro.isNullOrBlank()
    var senhaVisivel by remember { mutableStateOf(false) }
    val corBorda = if (temErro) RosaErro else Ciano

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        TextField(
            value = valor,
            onValueChange = aoMudarValor,
            label = { Text(rotulo, color = CinzaTexto) },
            placeholder = { Text(placeholder, color = CinzaTexto) },
            leadingIcon = if (iconeEsquerda != null) {
                {
                    Icon(
                        painter = painterResource(iconeEsquerda),
                        contentDescription = rotulo,
                        tint = Ciano
                    )
                }
            } else null,
            trailingIcon = if (eSenha) {
                {
                    Text(
                        text = if (senhaVisivel) "Ocultar" else "Ver",
                        color = Ciano,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .clickable { senhaVisivel = !senhaVisivel }
                            .padding(end = 8.dp)
                    )
                }
            } else null,
            visualTransformation = if (eSenha && !senhaVisivel) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = tecladoOpcoes,
            keyboardActions = acoesTeclado,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AzulCampo,
                unfocusedContainerColor = AzulCampo,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(AzulCampo, shape = RoundedCornerShape(12.dp))
                .border(1.dp, corBorda, shape = RoundedCornerShape(12.dp))
        )

        if (temErro) {
            Text(
                text = mensagemErro ?: "",
                color = RosaErro,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}