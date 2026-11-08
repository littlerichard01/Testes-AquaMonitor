package com.example.backend.repository

import com.example.backend.entity.ConfiguracaoTemperatura
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ConfiguracaoTemperaturaRepository : JpaRepository<ConfiguracaoTemperatura, Long> {

    fun existsByAquarioId(aquarioId: Long): Boolean

    fun findByAquarioId(aquarioId: Long): ConfiguracaoTemperatura?

    @Query("SELECT c FROM ConfiguracaoTemperatura c JOIN c.aquario a WHERE a.loja.id = :lojaId ORDER BY a.nome ASC")
    fun findAllByLojaId(@Param("lojaId") lojaId: Long): List<ConfiguracaoTemperatura>

    @Query("SELECT c FROM ConfiguracaoTemperatura c JOIN c.aquario a WHERE c.id = :id AND a.loja.id = :lojaId")
    fun findByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): ConfiguracaoTemperatura?

    @Query("SELECT COUNT(c) > 0 FROM ConfiguracaoTemperatura c JOIN c.aquario a WHERE c.id = :id AND a.loja.id = :lojaId")
    fun existsByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): Boolean

    @Query("SELECT COUNT(c) > 0 FROM ConfiguracaoTemperatura c WHERE c.aquario.id = :aquarioId AND c.id <> :id")
    fun existsByAquarioIdAndIdNot(@Param("aquarioId") aquarioId: Long, @Param("id") id: Long): Boolean

    @Query("SELECT c FROM ConfiguracaoTemperatura c JOIN c.aquario a WHERE a.loja.id = :lojaId AND LOWER(a.nome) LIKE LOWER(CONCAT('%', :nomeAquario, '%')) ORDER BY a.nome ASC")
    fun buscarPorNomeAquarioParcial(@Param("nomeAquario") nomeAquario: String, @Param("lojaId") lojaId: Long): List<ConfiguracaoTemperatura>
}
