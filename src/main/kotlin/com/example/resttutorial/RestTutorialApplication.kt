package com.example.resttutorial

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@SpringBootApplication
class RestTutorialApplication

fun main(args: Array<String>) {
	runApplication<RestTutorialApplication>(*args)
}


data class Tea(val id: UUID = UUID.randomUUID(), var name: String)

@RestController
@RequestMapping("api/tea")
class TeaController(var teas: MutableList<Tea> = mutableListOf()) {

	@GetMapping("/")
	fun getTeaList(): Iterable<Tea> {
		return teas
	}

	@GetMapping("/{id}")
	fun getTeaById(@PathVariable id:UUID): Tea? {
		return teas.find{ it.id == id}
	}

	@PostMapping
	fun addTea(@RequestBody tea: Tea): Tea {
		teas.add(tea)
		return tea
	}

	@PutMapping("/{id}")
	fun putTea(@PathVariable id:UUID, @RequestBody tea: Tea): ResponseEntity<Tea> {
		val teaIndex = teas.indexOfFirst { it == tea }
		return if (teaIndex == -1) {
			ResponseEntity(addTea(tea), HttpStatus.CREATED)
		} else {
			ResponseEntity(tea, HttpStatus.OK)
		}
	}

	@DeleteMapping("/{id}")
	fun deleteTea(@PathVariable id:UUID) {
		teas.removeIf{it.id == id}
	}
}
