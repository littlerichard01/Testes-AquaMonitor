package com.example.backend.repository

import com.example.backend.entity.Loja
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface LojaRepository : JpaRepository<Loja, Long> {

    fun findByEmail(email: String): Loja?

    fun existsByEmail(email: String): Boolean

    @Query("SELECT COUNT(l) > 0 FROM Loja l WHERE l.email = :email AND l.id <> :id")
    fun existsByEmailAndIdNot(@Param("email") email: String, @Param("id") id: Long): Boolean

    @Query("SELECT l FROM Loja l WHERE LOWER(l.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY l.nome ASC")
    fun buscarPorNomeParcial(@Param("nome") nome: String): List<Loja>
}
