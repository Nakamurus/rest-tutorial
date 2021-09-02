package com.example.resttutorial

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@SpringBootApplication
class RestTutorialApplication

fun main(args: Array<String>) {
	runApplication<RestTutorialApplication>(*args)
}

interface TeaRepository: CrudRepository<Tea, Int>

@Entity
class Tea(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Int = 0,
	var name: String
	)

@RestController
@RequestMapping("api/tea")
class TeaController(private val teaRepository: TeaRepository) {

	@GetMapping()
	fun getTeaList(): Iterable<Tea> {
		return teaRepository.findAll()
	}

	@GetMapping("/{id}")
	fun getTeaById(@PathVariable id:Int): Tea? {
		return teaRepository.findByIdOrNull(id)
	}

	@PostMapping
	fun addTea(@RequestBody tea: Tea): Tea {
		return teaRepository.save(tea)
	}

	@PutMapping("/{id}")
	fun putTea(@PathVariable id:Int, @RequestBody tea: Tea): ResponseEntity<Tea> {
		tea.id = id
		return if (teaRepository.existsById(id)) {
			ResponseEntity(teaRepository.save(tea), HttpStatus.OK)
		} else {
			ResponseEntity(addTea(tea), HttpStatus.CREATED)
		}
	}

	@DeleteMapping("/{id}")
	fun deleteTea(@PathVariable id:Int) {
		teaRepository.deleteById(id)
	}
}
