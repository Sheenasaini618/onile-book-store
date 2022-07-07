package com.example.demo.book

import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface BookRepository : ReactiveMongoRepository<Book , String> {

    fun findByTitle(title: String?) : Flux<Book>
    fun findByAuthors(authors: String?) : Flux<Book>

}