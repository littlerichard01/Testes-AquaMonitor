package com.example.backend.repository

import com.example.backend.entity.ProgramacaoAlimentacao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProgramacaoAlimentacaoRepository : JpaRepository<ProgramacaoAlimentacao, Long> {

    @Query("SELECT p FROM ProgramacaoAlimentacao p JOIN p.aquario a WHERE a.loja.id = :lojaId ORDER BY p.horario ASC, a.nome ASC")
    fun findAllByLojaId(@Param("lojaId") lojaId: Long): List<ProgramacaoAlimentacao>

    @Query("SELECT p FROM ProgramacaoAlimentacao p JOIN p.aquario a WHERE p.id = :id AND a.loja.id = :lojaId")
    fun findByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): ProgramacaoAlimentacao?

    @Query("SELECT COUNT(p) > 0 FROM ProgramacaoAlimentacao p JOIN p.aquario a WHERE p.id = :id AND a.loja.id = :lojaId")
    fun existsByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): Boolean

    @Query("SELECT p FROM ProgramacaoAlimentacao p JOIN p.aquario a WHERE a.loja.id = :lojaId AND LOWER(p.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')) ORDER BY p.horario ASC, a.nome ASC")
    fun buscarPorDescricaoParcial(
        @Param("descricao") descricao: String,
        @Param("lojaId") lojaId: Long
    ): List<ProgramacaoAlimentacao>
}
