package sch.aut.muer.controller

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import sch.aut.muer.dto.CreatePost
import sch.aut.muer.dto.CreateTeam
import sch.aut.muer.dto.PostResponse
import sch.aut.muer.dto.TeamResponse
import sch.aut.muer.modell.AutoMapper
import sch.aut.muer.modell.Post
import sch.aut.muer.modell.Team
import sch.aut.muer.repository.PostRepository
import sch.aut.muer.security.ClaimChecker

@RequestMapping("api/posts")
@RestController
class PostController(
        val checker:ClaimChecker,
        val postRepository: PostRepository,
        val mapper:AutoMapper
) {
    @GetMapping
    fun posts(
            @RequestParam(defaultValue = "1") page: Int,
            @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PostResponse> {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach", "Player"))
        if (currentUser != null) {
            val posts = postRepository
                    .findAll(PageRequest.of(page - 1, size, Sort.by("createdAt").descending()))
                    .let {
                        PostResponse(
                                data = it.toList(),
                                total = it.totalPages
                        )
                    }
            return ResponseEntity.status(HttpStatus.OK).body(posts)
        } else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @GetMapping("/{id}")
    fun post(
            @PathVariable id: Long,
    ): ResponseEntity<Post> {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach", "Player"))
        if (currentUser != null) {
            val post = postRepository.findByIdOrNull(id)
            if(post != null) {
                return ResponseEntity.status(HttpStatus.OK).body(post)
            }else
                throw ResponseStatusException(HttpStatus.NOT_FOUND)
        } else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }
    @PostMapping
    fun createPost(
            @RequestBody post: CreatePost
    ): ResponseEntity<Post> {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach"))

        if(currentUser != null){
            post.createdBy = currentUser
            val createdPost = postRepository.save(mapper.createPostToPost(post))
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost)
        } else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @PutMapping("/{id}")
    fun updatePost(
            @PathVariable id: Long,
            @RequestBody post: Post
    ): ResponseEntity<Post> {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach"))
        if(currentUser != null) {
            if(postRepository.existsById(id)) {
                post.id = id
                post.createdBy = currentUser
                val updatedPost = postRepository.save(post)
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedPost)
            } else
                throw  ResponseStatusException(HttpStatus.NOT_FOUND)
        } else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePost(
            @PathVariable id: Long
    ) {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach"))

        if(currentUser != null) {
            if(postRepository.existsById(id)) {
                postRepository.deleteById(id)
            }
        } else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }
}