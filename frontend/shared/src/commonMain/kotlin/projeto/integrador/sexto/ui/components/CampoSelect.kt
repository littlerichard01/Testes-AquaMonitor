package projeto.integrador.sexto.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import projeto.integrador.sexto.ui.theme.Ciano
import projeto.integrador.sexto.ui.theme.CinzaTexto
import projeto.integrador.sexto.ui.theme.AzulCampo

// Componente de selecao (dropdown) reutilizavel com estilo padronizado.
// Use para campos de enum (tipo, status, severidade, etc.) onde o usuario
// deve escolher uma opcao a partir de uma lista fechada.
//
// Parametros:
//   valor -> valor atualmente selecionado (estado controlado pelo caller).
//   opcoes -> lista de opcoes disponiveis para selecao.
//   aoSelecionar -> funcao chamada quando o usuario seleciona uma opcao.
//   rotulo -> texto acima do campo (label), em caixa alta.
//   placeholder -> texto de dica quando nenhuma opcao esta selecionada.
//   modifier -> modificador opcional.
//   mensagemErro -> se nao-nula/vazia, mostra a mensagem em vermelho abaixo.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoSelect(
    valor: String,
    opcoes: List<String>,
    aoSelecionar: (String) -> Unit,
    rotulo: String,
    placeholder: String = "Selecione uma opcao",
    modifier: Modifier = Modifier,
    mensagemErro: String? = null
) {
    val temErro = !mensagemErro.isNullOrBlank()
    var expandido by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Label fixada acima do campo com formatacao superior em caixa alta.
        if (rotulo.isNotBlank()) {
            Text(
                text = rotulo.uppercase(),
                color = CinzaTexto,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
            )
        }

        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = { expandido = !expandido }
        ) {
            OutlinedTextField(
                value = valor,
                onValueChange = {},
                readOnly = true,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = CinzaTexto.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    )
                },
                isError = temErro,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = AzulCampo,
                    unfocusedContainerColor = AzulCampo,
                    errorContainerColor = AzulCampo,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Ciano,
                    unfocusedBorderColor = Color.Transparent,
                    errorBorderColor = MaterialTheme.colorScheme.error
                )
            )
            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                opcoes.forEach { opcao ->
                    DropdownMenuItem(
                        text = { Text(opcao) },
                        onClick = {
                            aoSelecionar(opcao)
                            expandido = false
                        }
                    )
                }
            }
        }

        // Mensagem de validacao exibida logo abaixo do campo caso haja erro.
        if (temErro) {
            Text(
                text = mensagemErro.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}
