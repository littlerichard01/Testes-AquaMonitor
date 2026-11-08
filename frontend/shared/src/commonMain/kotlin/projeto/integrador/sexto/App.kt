package projeto.integrador.sexto

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import projeto.integrador.sexto.ui.navigation.AppNavController
import projeto.integrador.sexto.ui.navigation.AppNavHost
import projeto.integrador.sexto.ui.theme.AzulEscuro
import projeto.integrador.sexto.ui.theme.AzulFundo
import projeto.integrador.sexto.ui.theme.Branco
import projeto.integrador.sexto.ui.theme.Ciano
import projeto.integrador.sexto.ui.theme.RosaErro

@Composable
@Preview
fun App() {
    val cores = darkColorScheme(
        primary = Ciano,
        onPrimary = AzulFundo,
        background = AzulFundo,
        onBackground = Branco,
        surface = AzulEscuro,
        onSurface = Branco,
        error = RosaErro
    )

    MaterialTheme(colorScheme = cores) {
        val navController = remember { AppNavController() }
        AppNavHost(navController)
    }
}