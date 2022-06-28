package com.example.demo.book

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface BookRepository : ReactiveMongoRepository<Book , String> {

    fun findByAuthor(author : String?) : Flux<Book>

}