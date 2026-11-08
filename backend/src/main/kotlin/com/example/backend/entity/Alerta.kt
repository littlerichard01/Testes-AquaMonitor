package com.example.backend.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "alertas")
class Alerta(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 15)
    var tipo: TipoAlerta,

    @Enumerated(EnumType.STRING)
    @Column(name = "severidade", nullable = false, length = 10)
    var severidade: SeveridadeAlerta,

    @Column(name = "ativo", nullable = false)
    var ativo: Boolean = true,

    @Column(name = "criado_em", nullable = false, updatable = false)
    var criadoEm: LocalDateTime = LocalDateTime.now(),

    @Column(name = "atualizado_em", nullable = false)
    var atualizadoEm: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aquario_id", nullable = false)
    var aquario: Aquario
) {

    @PreUpdate
    fun preAtualizar() {
        atualizadoEm = LocalDateTime.now()
    }
}

enum class TipoAlerta {
    TEMPERATURA,
    ALIMENTACAO
}

enum class SeveridadeAlerta {
    BAIXA,
    MEDIA,
    ALTA
}
