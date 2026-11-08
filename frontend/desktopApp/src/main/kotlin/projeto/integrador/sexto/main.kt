package projeto.integrador.sexto

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ProjetoIntegrador6Sem",
    ) {
        App()
    }
}