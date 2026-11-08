package com.example.backend.mapper

import com.example.backend.dto.EspecimeRequest
import com.example.backend.dto.EspecimeResponse
import com.example.backend.entity.Aquario
import com.example.backend.entity.Especie
import com.example.backend.entity.Especime
import com.example.backend.entity.Loja
import com.example.backend.entity.StatusEspecime
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class EspecimeMapper {

    fun paraEntidade(request: EspecimeRequest, especie: Especie, aquario: Aquario?, loja: Loja): Especime {
        return Especime(
            identificacao = request.identificacao,
            preco = request.preco,
            status = request.status ?: StatusEspecime.DISPONIVEL,
            observacoes = request.observacoes,
            dataEntrada = request.dataEntrada ?: LocalDate.now(),
            especie = especie,
            aquario = aquario,
            loja = loja
        )
    }

    fun atualizarEntidade(request: EspecimeRequest, entidade: Especime, especie: Especie, aquario: Aquario?) {
        entidade.identificacao = request.identificacao
        entidade.preco = request.preco
        entidade.status = request.status ?: StatusEspecime.DISPONIVEL
        entidade.observacoes = request.observacoes
        entidade.dataEntrada = request.dataEntrada ?: LocalDate.now()
        entidade.especie = especie
        entidade.aquario = aquario
    }

    fun paraResponse(entidade: Especime): EspecimeResponse {
        return EspecimeResponse(
            id = entidade.id!!,
            identificacao = entidade.identificacao,
            preco = entidade.preco,
            status = entidade.status,
            observacoes = entidade.observacoes,
            dataEntrada = entidade.dataEntrada,
            especieId = entidade.especie.id!!,
            aquarioId = entidade.aquario?.id
        )
    }
}