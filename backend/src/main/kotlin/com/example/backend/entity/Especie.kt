package com.example.backend.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "especies")
class Especie(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "nome_popular", nullable = false, length = 255)
    var nomePopular: String,

    @Column(name = "nome_cientifico", length = 255)
    var nomeCientifico: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    var tipo: TipoEspecie,

    @Column(name = "descricao", columnDefinition = "TEXT")
    var descricao: String? = null,

    @Column(name = "temperatura_minima", precision = 5, scale = 2)
    var temperaturaMinima: BigDecimal? = null,

    @Column(name = "temperatura_maxima", precision = 5, scale = 2)
    var temperaturaMaxima: BigDecimal? = null,

    @Column(name = "criado_em", nullable = false, updatable = false)
    var criadoEm: LocalDateTime = LocalDateTime.now(),

    @Column(name = "atualizado_em", nullable = false)
    var atualizadoEm: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "especie", cascade = [CascadeType.ALL], orphanRemoval = false)
    var especimes: MutableList<Especime> = mutableListOf()
) {

    @PreUpdate
    fun preAtualizar() {
        atualizadoEm = LocalDateTime.now()
    }
}

enum class TipoEspecie {
    PEIXE,
    CRUSTACEO,
    MOLUSCO,
    CNIDARIO,
    OUTRO
}
