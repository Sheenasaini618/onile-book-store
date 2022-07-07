package com.example.demo.book

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

//We wrap a single Book.kt resource in a Mono because we return at most one book.
//For the collection resource, we use a Flux of type Book.kt since that's the publisher for 0..n elements.

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
class BookController(private val bookService : BookService) {

    // endpoint in our controller that publishes multiple Book.kt resource

    @GetMapping("/books/list")
    fun getAllBooks(): Flux<Book> {
        return bookService.findAll()
    }

    @GetMapping("/books/list/{query}")
    fun getAllBooksUsingGoogleBookApi(@PathVariable query : String): Flux<GooleBook> {
        return bookService.getBookfromApi(query)
    }

    @GetMapping("books/search/title/{title}")
    fun getBookBasisTitle(@PathVariable title : String): Flux<Book>{
        return bookService.findByTitle(title)
    }

    @GetMapping("books/search/author/{author}")
    fun getBookBasisAuthor(@PathVariable author : String): Flux<Book>{
        return bookService.findByAuthor(author)
    }

    @PostMapping("/books/create")
    fun createBook(@RequestBody book: Book){
        return bookService.createBooks(book)
    }

    @PutMapping("/books/update/{id}")
    fun updateBook(@PathVariable id : String, @RequestBody book : Book): Mono<Book>{
        return bookService.updatingBook(id, book)
    }

    @DeleteMapping("/books/delete/{id}")
    fun deleteBooksOfParticularId(@PathVariable id : String) : Mono<Void>{
        return bookService.deleteBooksOfParticularId(id)
    }

    @DeleteMapping("books/deleteAll")
    fun deleteAllBooks(): Mono<Void> {
        return bookService.remove()
    }

    @GetMapping("/books/audit")
    fun retrievingAudit() = KafkaConsumerService.audit

}