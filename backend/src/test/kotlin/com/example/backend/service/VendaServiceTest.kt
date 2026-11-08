package com.example.backend.service

import com.example.backend.dto.VendaRequest
import com.example.backend.dto.VendaResponse
import com.example.backend.entity.*
import com.example.backend.exception.ErroValidacaoException
import com.example.backend.exception.RegraNaoEncontradoException
import com.example.backend.mapper.VendaMapper
import com.example.backend.repository.AquarioRepository
import com.example.backend.repository.EspecimeRepository
import com.example.backend.repository.VendaRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

@TestMethodOrder(MethodOrderer.DisplayName::class)
class VendaServiceTest {

    // OBJETOS SIMULADOS (MOCKS): Usados para fiscalizar se as ações de salvamento ocorreram corretamente
    private val repository: VendaRepository = mockk()
    private val aquarioRepository: AquarioRepository = mockk(relaxed = true)
    private val especimeRepository: EspecimeRepository = mockk(relaxed = true)
    private val compradorService: CompradorService = mockk()
    private val aquarioService: AquarioService = mockk()
    private val especimeService: EspecimeService = mockk()
    private val lojaService: LojaService = mockk()
    private val mapper: VendaMapper = mockk()

    // DRIVER: O VendaService é o componente alvo acionado diretamente pelas funções de teste
    private lateinit var service: VendaService

    private val lojaPadrao: Loja = mockk()
    private val compradorPadrao: Comprador = mockk()

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        every { lojaPadrao.id } returns 1L

        service = VendaService(
            repository,
            aquarioRepository,
            especimeRepository,
            compradorService,
            aquarioService,
            especimeService,
            lojaService,
            mapper
        )

        // STUB: Fornece um retorno fixo simulando a loja autenticada no sistema
        every { lojaService.buscarLojaAutenticada() } returns lojaPadrao
    }
    
    // RN01: REGRA XOR (EXATAMENTE UM ITEM POR VENDA)
    @Test
    @DisplayName("RN01 - A: Deve dar erro ao tentar vender espécime e aquário juntos")
    fun deveFalharQuandoInformarAmbosItens() {
        // DRIVER: Aciona a rotina de criação passando os dados de entrada
        val request = VendaRequest(
            valor = BigDecimal("150.00"),
            especimeId = 1L,
            aquarioId = 2L,
            compradorId = 10L
        )

        val ex = assertThrows<ErroValidacaoException> {
            service.criar(request)
        }
        assertTrue(ex.message!!.contains("Regra XOR"))
    }

    @Test
    @DisplayName("RN01 - B: Deve dar erro se não informar nenhum item na venda")
    fun deveFalharQuandoNenhumItemForInformado() {
        // DRIVER: Aciona a rotina com dados incompletos
        val request = VendaRequest(
            valor = BigDecimal("150.00"),
            especimeId = null,
            aquarioId = null,
            compradorId = 10L
        )

        val ex = assertThrows<ErroValidacaoException> {
            service.criar(request)
        }
        assertTrue(ex.message!!.contains("Regra XOR"))
    }
    
    // RN02: VALIDAÇÃO DE VALOR E DATA
    @Test
    @DisplayName("RN02 - A: Deve dar erro se o valor da venda for zero ou negativo")
    fun deveFalharQuandoValorForMenorOuIgualAZero() {
        // DRIVER: Aciona o serviço com valor inválido
        val request = VendaRequest(
            valor = BigDecimal.ZERO,
            especimeId = 1L,
            aquarioId = null,
            compradorId = 10L
        )

        val ex = assertThrows<ErroValidacaoException> {
            service.criar(request)
        }
        assertEquals("Valor da venda deve ser maior que zero.", ex.message)
    }

    @Test
    @DisplayName("RN02 - B: Deve dar erro se a data da venda for no futuro")
    fun deveFalharQuandoDataVendaForNoFuturo() {
        // DRIVER: Aciona o serviço com data futura
        val request = VendaRequest(
            valor = BigDecimal("150.00"),
            especimeId = 1L,
            aquarioId = null,
            compradorId = 10L,
            dataVenda = LocalDateTime.now().plusDays(2)
        )

        val ex = assertThrows<ErroValidacaoException> {
            service.criar(request)
        }
        assertEquals("Data da venda nao pode ser no futuro.", ex.message)
    }

    // RN03: STATUS DO ITEM
    @Test
    @DisplayName("RN03 - A: Deve dar erro se o espécime já foi vendido antes")
    fun deveFalharSeEspecimeJaEstiverVendido() {
        val request = VendaRequest(
            valor = BigDecimal("200.00"),
            especimeId = 5L,
            aquarioId = null,
            compradorId = 10L
        )
        val especimeVendido: Especime = mockk {
            every { status } returns StatusEspecime.VENDIDO
        }

        // STUB: Configura dados simulados de um espécime com status indisponível
        every { compradorService.buscarEntidadeDaLojaOuFalhar(10L) } returns compradorPadrao
        every { especimeService.buscarEntidadeDaLojaOuFalhar(5L) } returns especimeVendido

        // DRIVER: Executa a criação da venda
        val ex = assertThrows<ErroValidacaoException> {
            service.criar(request)
        }
        assertTrue(ex.message!!.contains("ja esta vendido"))
        
        // OBJETO SIMULADO (MOCK): Confere que o repositório NÃO persistiu nenhuma venda devido à falha
        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    @DisplayName("RN03 - B: Deve dar erro se o aquário já foi vendido antes")
    fun deveFalharSeAquarioJaEstiverVendido() {
        val request = VendaRequest(
            valor = BigDecimal("500.00"),
            especimeId = null,
            aquarioId = 8L,
            compradorId = 10L
        )
        val aquarioVendido: Aquario = mockk {
            every { status } returns StatusAquario.VENDIDO
        }

        // STUB: Simula dados de um aquário já vendido
        every { compradorService.buscarEntidadeDaLojaOuFalhar(10L) } returns compradorPadrao
        every { aquarioService.buscarEntidadeDaLojaOuFalhar(8L) } returns aquarioVendido

        // DRIVER
        val ex = assertThrows<ErroValidacaoException> {
            service.criar(request)
        }
        assertTrue(ex.message!!.contains("ja esta vendido"))
        
        // OBJETO SIMULADO (MOCK): Garante que a gravação foi evitada
        verify(exactly = 0) { repository.save(any()) }
    }

    // RN04: ISOLAMENTO DE LOJA
    @Test
    @DisplayName("RN04 - A: Deve dar erro se o comprador for de outra loja")
    fun deveFalharQuandoCompradorNaoPertenceALoja() {
        val request = VendaRequest(
            valor = BigDecimal("200.00"),
            especimeId = 1L,
            aquarioId = null,
            compradorId = 99L
        )

        // STUB: Simula uma exceção de segurança por violação de multi-tenant no comprador
        every {
            compradorService.buscarEntidadeDaLojaOuFalhar(99L)
        } throws RegraNaoEncontradoException("Comprador não encontrado na loja.")

        // DRIVER
        assertThrows<RegraNaoEncontradoException> {
            service.criar(request)
        }
        
        // OBJETO SIMULADO (MOCK): Assegura que o salvamento não ocorreu
        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    @DisplayName("RN04 - B: Deve dar erro se o item for de outra loja")
    fun deveFalharQuandoItemNaoPertenceALoja() {
        val request = VendaRequest(
            valor = BigDecimal("200.00"),
            especimeId = 50L,
            aquarioId = null,
            compradorId = 10L
        )

        // STUB: Simula item pertencente a outra loja
        every { compradorService.buscarEntidadeDaLojaOuFalhar(10L) } returns compradorPadrao
        every {
            especimeService.buscarEntidadeDaLojaOuFalhar(50L)
        } throws RegraNaoEncontradoException("Espécime não encontrado na loja.")

        // DRIVER
        assertThrows<RegraNaoEncontradoException> {
            service.criar(request)
        }
        
        // OBJETO SIMULADO (MOCK)
        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    @DisplayName("RN04 - C: Deve dar erro ao buscar venda de outra loja por ID")
    fun deveFalharAoBuscarVendaDeOutraLoja() {
        // STUB: Simula busca nula para garantir que a venda não pertence à loja autenticada
        every { repository.findByIdAndLojaId(99L, 1L) } returns null

        // DRIVER
        assertThrows<RegraNaoEncontradoException> {
            service.buscarPorIdDaLoja(99L)
        }
    }

    // RN05: FLUXO DE SUCESSO
    @Test
    @DisplayName("RN05: Deve registrar a venda com sucesso e atualizar o status do item")
    fun deveCriarVendaDeEspecimeComSucesso() {
        val request = VendaRequest(
            valor = BigDecimal("300.00"),
            especimeId = 1L,
            aquarioId = null,
            compradorId = 10L
        )

        val especimeDisponivel: Especime = mockk(relaxed = true)
        val vendaSalva: Venda = mockk(relaxed = true)
        val responseEsperada: VendaResponse = mockk(relaxed = true)

        // STUB: Configura todas as respostas simuladas necessárias para o fluxo completo
        every { compradorService.buscarEntidadeDaLojaOuFalhar(10L) } returns compradorPadrao
        every { especimeService.buscarEntidadeDaLojaOuFalhar(1L) } returns especimeDisponivel
        every { especimeDisponivel.status } returns StatusEspecime.DISPONIVEL
        every { especimeRepository.save(any()) } returns especimeDisponivel
        every { repository.save(any()) } returns vendaSalva
        every { mapper.paraEntidade(request, compradorPadrao, especimeDisponivel, null, lojaPadrao) } returns vendaSalva
        every { mapper.paraResponse(vendaSalva) } returns responseEsperada

        // DRIVER: Aciona a execução real da regra de negócio
        val resultado = service.criar(request)

        // Conferências do teste
        assertNotNull(resultado)
        assertEquals(responseEsperada, resultado)

        // OBJETOS SIMULADOS (MOCKS): Fiscaliza se os repositórios foram chamados exatamente uma vez
        verify(exactly = 1) { especimeRepository.save(especimeDisponivel) }
        verify(exactly = 1) { repository.save(vendaSalva) }
    }
}