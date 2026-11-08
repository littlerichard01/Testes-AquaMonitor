package com.example.backend.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "aquarios")
class Aquario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "nome", nullable = false, length = 100)
    var nome: String,

    @Column(name = "descricao", columnDefinition = "TEXT")
    var descricao: String? = null,

    @Column(name = "volume_litros", precision = 10, scale = 2)
    var volumeLitros: BigDecimal? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 15)
    var tipo: TipoAquario,

    @Column(name = "localizacao", length = 255)
    var localizacao: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 15)
    var status: StatusAquario = StatusAquario.ATIVO,

    @Column(name = "preco", precision = 12, scale = 2)
    var preco: BigDecimal? = null,

    @Column(name = "criado_em", nullable = false, updatable = false)
    var criadoEm: LocalDateTime = LocalDateTime.now(),

    @Column(name = "atualizado_em", nullable = false)
    var atualizadoEm: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loja_id", nullable = false)
    var loja: Loja,

    @OneToMany(mappedBy = "aquario", cascade = [CascadeType.ALL], orphanRemoval = false)
    var especimes: MutableList<Especime> = mutableListOf(),

    @OneToMany(mappedBy = "aquario", cascade = [CascadeType.ALL], orphanRemoval = false)
    var alertas: MutableList<Alerta> = mutableListOf(),

    @OneToMany(mappedBy = "aquario", cascade = [CascadeType.ALL], orphanRemoval = false)
    var dispositivosIoT: MutableList<DispositivoIoT> = mutableListOf(),

    @OneToMany(mappedBy = "aquario", cascade = [CascadeType.ALL], orphanRemoval = false)
    var programacoesAlimentacao: MutableList<ProgramacaoAlimentacao> = mutableListOf(),

    @OneToMany(mappedBy = "aquario", cascade = [CascadeType.ALL], orphanRemoval = false)
    var leiturasTemperatura: MutableList<LeituraTemperatura> = mutableListOf(),

    @OneToOne(mappedBy = "aquario", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var configuracaoTemperatura: ConfiguracaoTemperatura? = null
) {

    @PreUpdate
    fun preAtualizar() {
        atualizadoEm = LocalDateTime.now()
    }
}

enum class TipoAquario {
    AGUA_DOCE,
    AGUA_SALGADA
}

enum class StatusAquario {
    ATIVO,
    INATIVO,
    VENDIDO
}
