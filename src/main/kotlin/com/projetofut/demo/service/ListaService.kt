package com.projetofut.demo.service

import com.google.firebase.database.*
import com.projetofut.demo.model.JogadorDto
import com.projetofut.demo.model.ListaDto
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch

@Service
class ListaService(private val firebaseDatabase: FirebaseDatabase) {

    private val listasRef = firebaseDatabase.getReference("listas")

    fun salvarLista(id: String, lista: ListaDto): Boolean {
        return try {
            val latch = CountDownLatch(1)
            var sucesso = false
            listasRef.child(id).setValue(lista) { error, _ ->
                sucesso = (error == null)
                latch.countDown()
            }
            latch.await()
            sucesso
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun atualizarLista(id: String, lista: ListaDto): Boolean {
        return salvarLista(id, lista)
    }

    fun deletarLista(id: String): Boolean {
        return try {
            val latch = CountDownLatch(1)
            var sucesso = false
            listasRef.child(id).removeValue { error, _ ->
                sucesso = (error == null)
                latch.countDown()
            }
            latch.await()
            sucesso
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun buscarLista(id: String, callback: (ListaDto?) -> Unit) {
        listasRef.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = snapshot.getValue(ListaDto::class.java)
                callback(lista)
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
                callback(null)
            }
        })
    }

    fun listarTodas(callback: (Map<String, ListaDto>?) -> Unit) {
        listasRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val resultado = mutableMapOf<String, ListaDto>()
                for (child in snapshot.children) {
                    val lista = child.getValue(ListaDto::class.java)
                    val key = child.key
                    if (lista != null && key != null) {
                        resultado[key] = lista
                    }
                }
                callback(resultado)
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
                callback(null)
            }
        })
    }

    fun deletarJogador(listaId: String, jogadorId: String): Boolean {
        return try {
            val latch = CountDownLatch(1)
            var sucesso = false
            val jogadorRef = listasRef.child(listaId).child("jogadores").child(jogadorId)
            jogadorRef.removeValue { error, _ ->
                sucesso = (error == null)
                latch.countDown()
            }
            latch.await()
            sucesso
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun listarTodosJogadores(idLista: String, callback: (Map<String, JogadorDto>?) -> Unit) {
        val publicosRef = firebaseDatabase.getReference("listas/$idLista/jogadoresPublicos")
        val privadosRef = firebaseDatabase.getReference("listas/$idLista/jogadoresPrivados")

        val resultado = mutableMapOf<String, JogadorDto>()

        // Contador simples para saber quando os dois callbacks terminaram
        var callbacksCompletos = 0

        fun tentarFinalizar() {
            callbacksCompletos++
            if (callbacksCompletos == 2) {
                callback(resultado)
            }
        }

        publicosRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    val jogador = child.getValue(JogadorDto::class.java)
                    val key = child.key
                    if (jogador != null && key != null) {
                        jogador.id = key
                        resultado[key] = jogador
                    }
                }
                tentarFinalizar()
            }
            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
                tentarFinalizar()
            }
        })

        privadosRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    val jogador = child.getValue(JogadorDto::class.java)
                    val key = child.key
                    if (jogador != null && key != null) {
                        jogador.id = key
                        // Atenção: se o jogador já estiver no resultado (em publicos), pode substituir ou não
                        resultado[key] = jogador
                    }
                }
                tentarFinalizar()
            }
            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
                tentarFinalizar()
            }
        })
    }



}
