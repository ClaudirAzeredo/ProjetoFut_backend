package com.projetofut.demo.controller

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileNotFoundException
import java.io.IOException

@Configuration
class FirebaseConfig {

    init {
        println("⚙️ FirebaseConfig foi instanciado!")
    }

    @PostConstruct
    fun initFirebase() {

            println("🔧 Método initFirebase() chamado!")

        try {
            val serviceAccount = this::class.java.classLoader
                .getResourceAsStream("lista-fut-f01fb-firebase-adminsdk-fbsvc-c649162e30.json")
                ?: throw FileNotFoundException("Arquivo de credenciais não encontrado!")

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://lista-fut-f01fb-default-rtdb.firebaseio.com/")
                .build()

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
                println("✅ Firebase inicializado com sucesso!")
            }

        } catch (e: Exception) {
            println("❌ Erro ao inicializar Firebase: ${e.message}")
        }
    }

    @Bean
    fun firebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
}
