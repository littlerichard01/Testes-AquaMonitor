package com.example.backend.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "dispositivos_iot")
class DispositivoIoT(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "nome", nullable = false, length = 150)
    var nome: String,

    @Column(name = "identificacao", nullable = false, unique = true, length = 150)
    var identificacao: String,

    @Column(name = "mqtt_topic", nullable = false, length = 255)
    var mqttTopic: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    var status: StatusDispositivo = StatusDispositivo.OFFLINE,

    @Column(name = "ultima_comunicacao")
    var ultimaComunicacao: LocalDateTime? = null,

    @Column(name = "criado_em", nullable = false, updatable = false)
    var criadoEm: LocalDateTime = LocalDateTime.now(),

    @Column(name = "atualizado_em", nullable = false)
    var atualizadoEm: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aquario_id")
    var aquario: Aquario? = null,

    @OneToMany(mappedBy = "dispositivoIoT", cascade = [CascadeType.ALL], orphanRemoval = false)
    var leiturasTemperatura: MutableList<LeituraTemperatura> = mutableListOf()
) {

    @PreUpdate
    fun preAtualizar() {
        atualizadoEm = LocalDateTime.now()
    }
}

enum class StatusDispositivo {
    ONLINE,
    OFFLINE,
    INATIVO
}
