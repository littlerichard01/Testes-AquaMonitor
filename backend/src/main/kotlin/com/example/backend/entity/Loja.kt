package com.example.backend.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "lojas")
class Loja(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "nome", nullable = false, length = 100 )
    var nome: String,

    @Column(name = "email", nullable = false, unique = true, length = 255)
    var email: String,

    @Column(name = "senha", nullable = false, length = 60 )
    var senha: String,

    @Column(name = "ativo", nullable = false)
    var ativo: Boolean = true,

    @Column(name = "criado_em", nullable = false, updatable = false)
    var criadoEm: LocalDateTime = LocalDateTime.now(),

    @Column(name = "atualizado_em", nullable = false)
    var atualizadoEm: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "loja", cascade = [CascadeType.ALL], orphanRemoval = false)
    var aquarios: MutableList<Aquario> = mutableListOf(),

    @OneToMany(mappedBy = "loja", cascade = [CascadeType.ALL], orphanRemoval = false)
    var especimes: MutableList<Especime> = mutableListOf(),

    @OneToMany(mappedBy = "loja", cascade = [CascadeType.ALL], orphanRemoval = false)
    var vendas: MutableList<Venda> = mutableListOf(),

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "lojas_compradores",
        joinColumns = [JoinColumn(name = "loja_id")],
        inverseJoinColumns = [JoinColumn(name = "comprador_id")]
    )
    var compradores: MutableList<Comprador> = mutableListOf()
) {

    @PreUpdate
    fun preAtualizar() {
        atualizadoEm = LocalDateTime.now()
    }
}
