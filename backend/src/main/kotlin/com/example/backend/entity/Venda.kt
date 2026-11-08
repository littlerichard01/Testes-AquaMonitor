package com.example.backend.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "vendas")
class Venda(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    var valor: BigDecimal,

    @Column(name = "data_venda", nullable = false)
    var dataVenda: LocalDateTime = LocalDateTime.now(),

    @Column(name = "observacoes", columnDefinition = "TEXT")
    var observacoes: String? = null,

    @Column(name = "criado_em", nullable = false, updatable = false)
    var criadoEm: LocalDateTime = LocalDateTime.now(),

    @Column(name = "atualizado_em", nullable = false)
    var atualizadoEm: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comprador_id", nullable = false)
    var comprador: Comprador,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especime_id")
    var especime: Especime? = null,

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
