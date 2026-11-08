package com.example.backend.entity

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * TESTE DE UNIDADE - CAMADA DE DOMÍNIO (ENTIDADE)
 * Teste estrutural dos atributos e do ciclo de vida da Entidade Venda.
 */
class VendaTest {

    @Test
    @DisplayName("Deve instanciar a entidade Venda com todos os campos e verificar valores padrão")
    fun deveInstanciarVendaComSucesso() {
        // OBJETOS SIMULADOS (MOCKS): Evitam a necessidade de instanciar construtores complexos de Loja e Comprador
        val lojaMock = mockk<Loja>()
        val compradorMock = mockk<Comprador>()
        val valorEsperado = BigDecimal("250.50")
        val agora = LocalDateTime.now()

        // DRIVER: Aciona a criação da entidade em memória para testar suas propriedades
        val venda = Venda(
            valor = valorEsperado,
            dataVenda = agora,
            observacoes = "Venda à vista",
            comprador = compradorMock,
            loja = lojaMock
        )

        // Conferências dos valores iniciais e padrões
        assertNull(venda.id, "ID inicial deve ser nulo antes da persistência")
        assertEquals(valorEsperado, venda.valor)
        assertEquals("Venda à vista", venda.observacoes)
        assertEquals(compradorMock, venda.comprador)
        assertEquals(lojaMock, venda.loja)
        assertNull(venda.especime, "Espécime deve ser nulo se não fornecido")
        assertNull(venda.aquario, "Aquário deve ser nulo se não fornecido")
        assertNotNull(venda.criadoEm)
        assertNotNull(venda.atualizadoEm)
    }

    @Test
    @DisplayName("Deve atualizar data de modificação ao acionar o gancho preAtualizar()")
    fun deveAtualizarDataModificacaoNoPreAtualizar() {
        // OBJETOS SIMULADOS (MOCKS)
        val lojaMock = mockk<Loja>()
        val compradorMock = mockk<Comprador>()
        val dataPassada = LocalDateTime.now().minusDays(1)

        val venda = Venda(
            valor = BigDecimal("100.00"),
            comprador = compradorMock,
            loja = lojaMock,
            atualizadoEm = dataPassada
        )

        // DRIVER: Aciona manualmente o gatilho de ciclo de vida da JPA (@PreUpdate)
        venda.preAtualizar()

        // Confere se a data de atualização foi renovada com sucesso
        assertTrue(venda.atualizadoEm.isAfter(dataPassada))
    }
}