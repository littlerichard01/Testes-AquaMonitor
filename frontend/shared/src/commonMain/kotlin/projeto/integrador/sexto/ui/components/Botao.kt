package projeto.integrador.sexto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import projeto.integrador.sexto.ui.theme.AzulCampo
import projeto.integrador.sexto.ui.theme.Ciano
import projeto.integrador.sexto.ui.theme.CinzaTexto

@Composable
fun Botao(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true
) {
    val corFundo = if (habilitado) Ciano else AzulCampo
    val corTexto = if (habilitado) Color.Black else CinzaTexto

    Button(
        onClick = onClick,
        enabled = habilitado,
        shape = RoundedCornerShape(26.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = corFundo,
            disabledContainerColor = AzulCampo
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(corFundo, shape = RoundedCornerShape(26.dp))
            .border(2.dp, Ciano, shape = RoundedCornerShape(26.dp))
    ) {
        Text(
            text = texto,
            color = corTexto,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}