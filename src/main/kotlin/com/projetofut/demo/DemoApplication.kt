package com.projetofut.demo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication(scanBasePackages = ["com.projetofut.demo"])
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)

	@Bean
	fun runner(): CommandLineRunner {
		return CommandLineRunner {
			println("🚀 Aplicação iniciou com sucesso!")
		}
	}
}



