package com.example.backend.repository

import com.example.backend.entity.Especie
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EspecieRepository : JpaRepository<Especie, Long> {

    @Query(
        """
        SELECT e FROM Especie e
        WHERE LOWER(e.nomePopular) LIKE LOWER(CONCAT(CONCAT('%', :nome), '%'))
        ORDER BY e.nomePopular ASC
        """
    )
    fun buscarPorNomePopularParcial(@Param("nome") nome: String): List<Especie>

    @Query(
        """
        SELECT e FROM Especie e
        WHERE LOWER(e.nomePopular) LIKE LOWER(CONCAT(CONCAT('%', :termo), '%'))
        OR LOWER(e.nomeCientifico) LIKE LOWER(CONCAT(CONCAT('%', :termo), '%'))
        ORDER BY e.nomePopular ASC
        """
    )
    fun buscarPorNomePopularOuCientifico(@Param("termo") termo: String): List<Especie>
}
