package com.projetofut.demo.controller

import com.google.firebase.database.FirebaseDatabase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/firebase")
class FirebaseTestController(
    private val firebaseDatabase: FirebaseDatabase // 🔧 Injeta a instância configurada
) {

    @GetMapping("/test")
    fun testFirebase(): String {
        println("🔥 Endpoint /firebase/test chamado!")

        val ref = firebaseDatabase.getReference("testeConexao")
        ref.setValueAsync("Funcionando!")

        return "✔ Requisição enviada para o Firebase!"
    }
}
