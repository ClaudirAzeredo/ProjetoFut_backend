package com.projetofut.demo.controller

import com.projetofut.demo.model.ListaDto
import com.projetofut.demo.service.ListaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/listas")
class ListaController(
    private val listaService: ListaService
) {

    @PostMapping("/{id}")
    fun criarLista(@PathVariable id: String, @RequestBody dto: ListaDto): ResponseEntity<String> {
        return if (listaService.salvarLista(id, dto)) {
            ResponseEntity.ok("✔ Lista '$id' criada com sucesso!")
        } else {
            ResponseEntity.internalServerError().body("❌ Erro ao criar lista '$id'")
        }
    }

    @PutMapping("/{id}")
    fun atualizarLista(@PathVariable id: String, @RequestBody dto: ListaDto): ResponseEntity<String> {
        return if (listaService.atualizarLista(id, dto)) {
            ResponseEntity.ok("✏ Lista '$id' atualizada com sucesso!")
        } else {
            ResponseEntity.internalServerError().body("❌ Erro ao atualizar lista '$id'")
        }
    }

    @DeleteMapping("/{id}")
    fun deletarLista(@PathVariable id: String): ResponseEntity<String> {
        return if (listaService.deletarLista(id)) {
            ResponseEntity.ok("🗑 Lista '$id' deletada com sucesso!")
        } else {
            ResponseEntity.internalServerError().body("❌ Erro ao deletar lista '$id'")
        }
    }

    @GetMapping("/{id}")
    fun buscarLista(@PathVariable id: String): CompletableFuture<ResponseEntity<Any>> {
        val future = CompletableFuture<ResponseEntity<Any>>()
        listaService.buscarLista(id) { lista ->
            if (lista != null) {
                future.complete(ResponseEntity.ok(lista))
            } else {
                future.complete(ResponseEntity.notFound().build())
            }
        }
        return future
    }

    @GetMapping
    fun listarTodas(): CompletableFuture<ResponseEntity<Any>> {
        val future = CompletableFuture<ResponseEntity<Any>>()
        println("🔍 Requisição recebida em /listas")
        try {
            listaService.listarTodas { listas ->
                println("📦 Callback chamado com listas = $listas")
                if (listas != null) {
                    future.complete(ResponseEntity.ok(listas))
                } else {
                    future.complete(ResponseEntity.internalServerError().body("Erro ao buscar listas"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            future.complete(ResponseEntity.internalServerError().body("Exceção: ${e.message}"))
        }
        return future
    }

    @DeleteMapping("/{listaId}/jogadores/{jogadorId}")
    fun deletarJogador(
        @PathVariable listaId: String,
        @PathVariable jogadorId: String
    ): ResponseEntity<String> {
        return if (listaService.deletarJogador(listaId, jogadorId)) {
            ResponseEntity.ok("🗑 Jogador '$jogadorId' removido da lista '$listaId' com sucesso!")
        } else {
            ResponseEntity.internalServerError().body("❌ Erro ao remover jogador '$jogadorId' da lista '$listaId'")
        }
    }

    @GetMapping("/{listaId}/jogadores/todos")
    fun listarTodosJogadores(@PathVariable listaId: String): CompletableFuture<ResponseEntity<Any>> {
        val future = CompletableFuture<ResponseEntity<Any>>()
        listaService.listarTodosJogadores(listaId) { jogadores ->
            if (jogadores != null) {
                future.complete(ResponseEntity.ok(jogadores))
            } else {
                future.complete(ResponseEntity.notFound().build())
            }
        }
        return future
    }
    @GetMapping("/teste")
    fun teste(): String = "ok"

}
