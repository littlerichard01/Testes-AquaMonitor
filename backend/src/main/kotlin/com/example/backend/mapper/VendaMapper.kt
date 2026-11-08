package com.example.backend.mapper

import com.example.backend.dto.AquarioResumo
import com.example.backend.dto.CompradorResumo
import com.example.backend.dto.EspecimeResumo
import com.example.backend.dto.VendaRequest
import com.example.backend.dto.VendaResponse
import com.example.backend.entity.Aquario
import com.example.backend.entity.Comprador
import com.example.backend.entity.Especime
import com.example.backend.entity.Loja
import com.example.backend.entity.Venda
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class VendaMapper {

    fun paraEntidade(
        request: VendaRequest,
        comprador: Comprador,
        especime: Especime?,
        aquario: Aquario?,
        loja: Loja
    ): Venda {
        return Venda(
            valor = request.valor,
            dataVenda = request.dataVenda ?: LocalDateTime.now(),
            observacoes = request.observacoes?.trim()?.ifBlank { null },
            comprador = comprador,
            especime = especime,
            aquario = aquario,
            loja = loja
        )
    }

    fun atualizarEntidade(entidade: Venda, request: VendaRequest) {
        entidade.valor = request.valor
        entidade.dataVenda = request.dataVenda ?: entidade.dataVenda
        entidade.observacoes = request.observacoes?.trim()?.ifBlank { null }
    }

    fun paraResponse(entidade: Venda): VendaResponse {
        return VendaResponse(
            id = entidade.id!!,
            valor = entidade.valor,
            dataVenda = entidade.dataVenda,
            observacoes = entidade.observacoes,
            lojaId = entidade.loja.id!!,
            comprador = CompradorResumo(
                id = entidade.comprador.id!!,
                nome = entidade.comprador.nome,
                cpf = entidade.comprador.cpf
            ),
            especime = entidade.especime?.let {
                EspecimeResumo(
                    id = it.id!!,
                    identificacao = it.identificacao,
                    status = it.status
                )
            },
            aquario = entidade.aquario?.let {
                AquarioResumo(
                    id = it.id!!,
                    nome = it.nome,
                    status = it.status
                )
            },
            criadoEm = entidade.criadoEm,
            atualizadoEm = entidade.atualizadoEm
        )
    }
}
