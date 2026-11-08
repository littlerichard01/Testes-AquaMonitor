package com.example.backend.repository

import com.example.backend.entity.LeituraTemperatura
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface LeituraTemperaturaRepository : JpaRepository<LeituraTemperatura, Long> {

    @Query(
        """
        SELECT lt FROM LeituraTemperatura lt
        JOIN lt.aquario a
        JOIN lt.dispositivoIoT d
        LEFT JOIN d.aquario da
        WHERE a.loja.id = :lojaId
        ORDER BY lt.dataHora DESC
        """
    )
    fun findAllByLojaId(@Param("lojaId") lojaId: Long, pageable: Pageable): List<LeituraTemperatura>

    @Query(
        """
        SELECT lt FROM LeituraTemperatura lt
        JOIN lt.aquario a
        WHERE lt.id = :id AND a.loja.id = :lojaId
        """
    )
    fun findByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): LeituraTemperatura?

    @Query(
        """
        SELECT lt FROM LeituraTemperatura lt
        JOIN lt.aquario a
        WHERE a.loja.id = :lojaId AND a.id = :aquarioId
        ORDER BY lt.dataHora DESC
        """
    )
    fun findAllByAquarioDaLoja(
        @Param("lojaId") lojaId: Long,
        @Param("aquarioId") aquarioId: Long,
        pageable: Pageable
    ): List<LeituraTemperatura>

    @Query(
        """
        SELECT lt FROM LeituraTemperatura lt
        JOIN lt.aquario a
        WHERE a.loja.id = :lojaId
          AND LOWER(a.nome) LIKE LOWER(CONCAT(CONCAT('%', :nomeAquario), '%'))
        ORDER BY lt.dataHora DESC
        """
    )
    fun buscarPorNomeAquarioParcial(
        @Param("lojaId") lojaId: Long,
        @Param("nomeAquario") nomeAquario: String,
        pageable: Pageable
    ): List<LeituraTemperatura>
}
