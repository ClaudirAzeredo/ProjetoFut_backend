package com.projetofut.demo.model


data class ListaDto(
    var nome: String? = null,
    var dataCriacao: String? = null,
    var organizadorId: String? = null,
    var jogadores: Map<String, JogadorDto>? = null,
    var jogadoresPrivados: Map<String, JogadorDto>? = null,
    var jogadoresPublicos: Map<String, JogadorDto>? = null
)

data class JogadorDto(
    var id: String? = null,
    var nome: String? = null,
    var posicao: String? = null,
    var presente: Boolean? = null
)


