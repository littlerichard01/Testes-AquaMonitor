package com.example.backend.repository

import com.example.backend.entity.DispositivoIoT
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface DispositivoIoTRepository : JpaRepository<DispositivoIoT, Long> {

    fun existsByIdentificacao(identificacao: String): Boolean

    @Query("SELECT COUNT(d) > 0 FROM DispositivoIoT d WHERE d.identificacao = :identificacao AND d.id <> :id")
    fun existsByIdentificacaoAndIdNot(
        @Param("identificacao") identificacao: String,
        @Param("id") id: Long
    ): Boolean

    @Query("SELECT d FROM DispositivoIoT d JOIN d.aquario a WHERE a.loja.id = :lojaId ORDER BY d.nome ASC")
    fun findAllByLojaId(@Param("lojaId") lojaId: Long): List<DispositivoIoT>

    @Query("SELECT d FROM DispositivoIoT d JOIN d.aquario a WHERE d.id = :id AND a.loja.id = :lojaId")
    fun findByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): DispositivoIoT?

    @Query("SELECT COUNT(d) > 0 FROM DispositivoIoT d JOIN d.aquario a WHERE d.id = :id AND a.loja.id = :lojaId")
    fun existsByIdAndLojaId(@Param("id") id: Long, @Param("lojaId") lojaId: Long): Boolean

    @Query("SELECT d FROM DispositivoIoT d JOIN d.aquario a WHERE a.loja.id = :lojaId AND LOWER(d.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY d.nome ASC")
    fun buscarPorNomeParcial(
        @Param("nome") nome: String,
        @Param("lojaId") lojaId: Long
    ): List<DispositivoIoT>

}
