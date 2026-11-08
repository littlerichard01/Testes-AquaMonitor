package com.example.backend.repository

import com.example.backend.entity.Comprador
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CompradorRepository : JpaRepository<Comprador, Long> {

    @Query("SELECT DISTINCT c FROM Comprador c JOIN c.lojas l WHERE l.id = :lojaId ORDER BY c.nome ASC")
    fun findAllByLojaId(@Param("lojaId") lojaId: Long): List<Comprador>

    @Query("SELECT c FROM Comprador c JOIN c.lojas l WHERE c.id = :id AND l.id = :lojaId")
    fun findByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): Comprador?

    @Query("SELECT COUNT(c) > 0 FROM Comprador c JOIN c.lojas l WHERE c.id = :id AND l.id = :lojaId")
    fun existsByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): Boolean

    @Query("SELECT DISTINCT c FROM Comprador c JOIN c.lojas l WHERE l.id = :lojaId AND LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY c.nome ASC")
    fun buscarPorNomeParcial(@Param("nome") nome: String, @Param("lojaId") lojaId: Long): List<Comprador>

}
