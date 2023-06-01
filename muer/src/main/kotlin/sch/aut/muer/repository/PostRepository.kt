package sch.aut.muer.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sch.aut.muer.modell.Post

@Repository
interface PostRepository: JpaRepository<Post, Long> {}