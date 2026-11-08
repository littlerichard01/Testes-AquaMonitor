package projeto.integrador.sexto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import projeto.integrador.sexto.ui.theme.AzulCampo
import projeto.integrador.sexto.ui.theme.Ciano

@Composable
fun CardSimples(
    titulo: String,
    modifier: Modifier = Modifier,
    corFundo: Color? = null,
    conteudo: @Composable () -> Unit
) {
    val fundo = corFundo ?: AzulCampo

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(fundo, shape = RoundedCornerShape(16.dp))
            .border(1.dp, Ciano, shape = RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))
        conteudo()
    }
}