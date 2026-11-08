package com.example.backend.repository

import com.example.backend.entity.Especime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EspecimeRepository : JpaRepository<Especime, Long> {

    fun existsByIdentificacao(identificacao: String): Boolean

    @Query("SELECT COUNT(e) > 0 FROM Especime e WHERE e.identificacao = :identificacao AND e.id <> :id")
    fun existsByIdentificacaoAndIdNot(@Param("identificacao") identificacao: String, @Param("id") id: Long): Boolean

    @Query("SELECT e FROM Especime e WHERE e.loja.id = :lojaId ORDER BY e.identificacao ASC")
    fun findAllByLojaId(@Param("lojaId") lojaId: Long): List<Especime>

    @Query("SELECT e FROM Especime e WHERE e.id = :id AND e.loja.id = :lojaId")
    fun findByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): Especime?

    @Query("SELECT COUNT(e) > 0 FROM Especime e WHERE e.id = :id AND e.loja.id = :lojaId")
    fun existsByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): Boolean

    @Query("SELECT e FROM Especime e WHERE e.loja.id = :lojaId AND LOWER(e.identificacao) LIKE LOWER(CONCAT('%', :identificacao, '%')) ORDER BY e.identificacao ASC")
    fun buscarPorIdentificacaoParcial(
        @Param("identificacao") identificacao: String,
        @Param("lojaId") lojaId: Long
    ): List<Especime>
}
