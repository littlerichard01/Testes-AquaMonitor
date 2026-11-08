package projeto.integrador.sexto.data.local

class AuthStore {

    private companion object Chaves {
        private const val CHAVE_TOKEN = "auth_token"
        private const val CHAVE_TIPO_TOKEN = "auth_tipo_token"
        private const val CHAVE_LOJA_ID = "auth_loja_id"
        private const val CHAVE_LOJA_NOME = "auth_loja_nome"
        private const val CHAVE_LOJA_EMAIL = "auth_loja_email"
    }

    private val dados: MutableMap<String, Any?> = mutableMapOf()

    fun salvarSessao(
        token: String,
        tipoToken: String,
        lojaId: Long,
        nomeLoja: String,
        emailLoja: String
    ) {
        dados[CHAVE_TOKEN] = token
        dados[CHAVE_TIPO_TOKEN] = tipoToken
        dados[CHAVE_LOJA_ID] = lojaId
        dados[CHAVE_LOJA_NOME] = nomeLoja
        dados[CHAVE_LOJA_EMAIL] = emailLoja
    }

    fun obterToken(): String? = dados[CHAVE_TOKEN] as? String
    fun obterTipoToken(): String = (dados[CHAVE_TIPO_TOKEN] as? String) ?: "Bearer"
    fun obterLojaId(): Long? = dados[CHAVE_LOJA_ID] as? Long
    fun obterNomeLoja(): String? = dados[CHAVE_LOJA_NOME] as? String
    fun obterEmailLoja(): String? = dados[CHAVE_LOJA_EMAIL] as? String

    fun estaLogado(): Boolean = !obterToken().isNullOrBlank()

    fun limparSessao() {
        dados.remove(CHAVE_TOKEN)
        dados.remove(CHAVE_TIPO_TOKEN)
        dados.remove(CHAVE_LOJA_ID)
        dados.remove(CHAVE_LOJA_NOME)
        dados.remove(CHAVE_LOJA_EMAIL)
    }
}
