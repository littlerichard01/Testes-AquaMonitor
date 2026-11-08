package com.example.backend.repository

import com.example.backend.entity.Aquario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AquarioRepository : JpaRepository<Aquario, Long> {

    @Query("SELECT a FROM Aquario a WHERE a.loja.id = :lojaId ORDER BY a.nome ASC")
    fun findAllByLojaId(@Param("lojaId") lojaId: Long): List<Aquario>

    @Query("SELECT a FROM Aquario a WHERE a.id = :id AND a.loja.id = :lojaId")
    fun findByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): Aquario?

    @Query("SELECT COUNT(a) > 0 FROM Aquario a WHERE a.id = :id AND a.loja.id = :lojaId")
    fun existsByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): Boolean

    @Query("SELECT a FROM Aquario a WHERE a.loja.id = :lojaId AND LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY a.nome ASC")
    fun buscarPorNomeParcial(
        @Param("nome") nome: String,
        @Param("lojaId") lojaId: Long
    ): List<Aquario>
}
