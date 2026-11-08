package com.example.backend.repository

import com.example.backend.entity.Alerta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AlertaRepository : JpaRepository<Alerta, Long> {

    @Query("SELECT a FROM Alerta a JOIN a.aquario aq WHERE aq.loja.id = :lojaId ORDER BY a.severidade DESC, aq.nome ASC")
    fun findAllByLojaId(@Param("lojaId") lojaId: Long): List<Alerta>

    @Query("SELECT a FROM Alerta a JOIN a.aquario aq WHERE a.id = :id AND aq.loja.id = :lojaId")
    fun findByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): Alerta?

    @Query("SELECT COUNT(a) > 0 FROM Alerta a JOIN a.aquario aq WHERE a.id = :id AND aq.loja.id = :lojaId")
    fun existsByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): Boolean

    @Query("SELECT a FROM Alerta a JOIN a.aquario aq WHERE aq.loja.id = :lojaId AND LOWER(aq.nome) LIKE LOWER(CONCAT('%', :nomeAquario, '%')) ORDER BY a.id DESC")
    fun buscarPorNomeAquarioParcial(@Param("nomeAquario") nomeAquario: String, @Param("lojaId") lojaId: Long
    ): List<Alerta>
}
