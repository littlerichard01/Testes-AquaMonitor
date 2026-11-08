package com.example.backend.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(
    name = "configuracoes_temperatura",
    uniqueConstraints = [UniqueConstraint(columnNames = ["aquario_id"])]
)
class ConfiguracaoTemperatura(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "temperatura_minima", nullable = false, precision = 5, scale = 2)
    var temperaturaMinima: BigDecimal,

    @Column(name = "temperatura_maxima", nullable = false, precision = 5, scale = 2)
    var temperaturaMaxima: BigDecimal,

    @Column(name = "ativo", nullable = false)
    var ativo: Boolean = true,

    @Column(name = "criado_em", nullable = false, updatable = false)
    var criadoEm: LocalDateTime = LocalDateTime.now(),

    @Column(name = "atualizado_em", nullable = false)
    var atualizadoEm: LocalDateTime = LocalDateTime.now(),

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aquario_id", nullable = false, unique = true)
    var aquario: Aquario
) {

    @PreUpdate
    fun preAtualizar() {
        atualizadoEm = LocalDateTime.now()
    }
}
