package sch.aut.muer.controller

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import sch.aut.muer.dto.UserResponse
import sch.aut.muer.modell.User
import sch.aut.muer.repository.UserRepository
import sch.aut.muer.security.ClaimChecker


@RestController
@RequestMapping("api/users")
class UserRestController (
    val checker: ClaimChecker,
    val userRepository: UserRepository
){


    @GetMapping("/coaches")
    fun getCoaches(): ResponseEntity<List<User>>{
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin"))

        if(currentUser != null) {
            val coaches = userRepository.findAllByRole("Coach")
            return ResponseEntity.status(HttpStatus.OK).body(coaches)
        }
        else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }


    @GetMapping("/all")
    fun getUsers(
    ): ResponseEntity<List<User>>{
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach"))
        if(currentUser != null) {
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll())
        }
        else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @GetMapping("")
    fun getAllUsers(
        @RequestParam(defaultValue = "1") page: Int
    ): ResponseEntity<UserResponse>{
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach"))

        if(currentUser != null) {
            val users = userRepository
                .findAll(PageRequest.of(page - 1, 10, Sort.by("name").descending()))
                .let{
                    UserResponse(
                        data = it.toList(),
                        page = page,
                        total =  it.size
                    )
                }
            return ResponseEntity.status(HttpStatus.OK).body(users)
        }
        else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

}