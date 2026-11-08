package com.example.backend.exception

class RegraNaoEncontradoException(
    mensagem: String
) : RuntimeException(mensagem)

class ErroValidacaoException(
    mensagem: String
) : RuntimeException(mensagem)

class EmailJaCadastradoException(
    mensagem: String
) : RuntimeException(mensagem)

class CredenciaisIncorretasException(
    mensagem: String
) : RuntimeException(mensagem)