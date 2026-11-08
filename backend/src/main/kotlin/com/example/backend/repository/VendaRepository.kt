package com.example.backend.repository

import com.example.backend.entity.Venda
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface VendaRepository : JpaRepository<Venda, Long> {

    @Query("SELECT v FROM Venda v WHERE v.loja.id = :lojaId ORDER BY v.dataVenda DESC")
    fun findAllByLojaId(@Param("lojaId") lojaId: Long): List<Venda>

    @Query("SELECT v FROM Venda v WHERE v.id = :id AND v.loja.id = :lojaId")
    fun findByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): Venda?

    @Query("SELECT COUNT(v) > 0 FROM Venda v WHERE v.id = :id AND v.loja.id = :lojaId")
    fun existsByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): Boolean

    @Query("SELECT v FROM Venda v JOIN v.comprador c WHERE v.loja.id = :lojaId AND LOWER(c.nome) LIKE LOWER(CONCAT('%', :nomeComprador, '%')) ORDER BY v.dataVenda DESC")
    fun buscarPorNomeCompradorParcial(
        @Param("nomeComprador") nomeComprador: String,
        @Param("lojaId") lojaId: Long
    ): List<Venda>
}
