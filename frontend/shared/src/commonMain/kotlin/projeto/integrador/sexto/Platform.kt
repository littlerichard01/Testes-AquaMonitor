package projeto.integrador.sexto

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform