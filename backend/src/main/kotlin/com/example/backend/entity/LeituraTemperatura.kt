package com.example.backend.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "leituras_temperatura")
class LeituraTemperatura(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "temperatura", nullable = false, precision = 5, scale = 2)
    var temperatura: BigDecimal,

    @Column(name = "data_hora", nullable = false)
    var dataHora: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aquario_id", nullable = false)
    var aquario: Aquario,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dispositivo_iot_id", nullable = false)
    var dispositivoIoT: DispositivoIoT
)
