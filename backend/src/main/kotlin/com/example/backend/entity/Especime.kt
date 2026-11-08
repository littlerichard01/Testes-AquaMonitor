package com.example.backend.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "especimes")
class Especime(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "identificacao", nullable = false, unique = true,  length = 150)
    var identificacao: String,

    @Column(name = "preco", nullable = false, precision = 12, scale = 2)
    var preco: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 15)
    var status: StatusEspecime = StatusEspecime.DISPONIVEL,

    @Column(name = "observacoes", columnDefinition = "TEXT")
    var observacoes: String? = null,

    @Column(name = "data_entrada", nullable = false)
    var dataEntrada: LocalDate = LocalDate.now(),

    @Column(name = "criado_em", nullable = false, updatable = false)
    var criadoEm: LocalDateTime = LocalDateTime.now(),

    @Column(name = "atualizado_em", nullable = false)
    var atualizadoEm: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especie_id", nullable = false)
    var especie: Especie,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aquario_id")
    var aquario: Aquario? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loja_id", nullable = false)
    var loja: Loja
) {

    @PreUpdate
    fun preAtualizar() {
        atualizadoEm = LocalDateTime.now()
    }
}

enum class StatusEspecime {
    DISPONIVEL,
    VENDIDO,
    INDISPONIVEL
}
