package com.example.demo.book

import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMethod.*

import java.time.Duration

//We wrap a single Book resource in a Mono because we return at most one book.
//For the collection resource, we use a Flux of type Book since that's the publisher for 0..n elements.

@RestController
@RequestMapping("/api/v1")
class BookController(private val bookService : BookService) {

    // endpoint in our controller that publishes multiple Book resource

    @GetMapping("/books/list")
    fun getAllBooks(): Flux<Book> {
        return bookService.findAll()
    }

    // endpoint in our controller that publishes a single Book resource

    @GetMapping("books/list/title/{bookTitle}")
    fun getBookBasisTitle(@PathVariable bookTitle : String): Mono<Book>{
        return bookService.findBookByTitle(bookTitle)
    }

    @GetMapping("books/list/author/{bookAuthor}")
    fun getBookBasisAuthor(@PathVariable bookAuthor: String): Flux<Book> {
        return bookService.findBookByAuthor(bookAuthor)
    }

    @PostMapping("/books/create")
    fun createBook(@RequestBody book: Book):Mono<Book>{
        return bookService.createBooks(book)
    }

    @PutMapping("/books/update")
    fun updateBook(@RequestBody book : Book): Mono<Book>{
        return bookService.updatingBook(book)
    }

    @DeleteMapping("/books/delete/{bookTitle}")
    fun deleteAllBooksOfParticularTitle(@PathVariable("bookTitle") bookTitle : String) : String{
        bookService.deleteAllBooksOnTheBasisOfTheTitle(bookTitle)
        return "deleted successfully"
    }

    @DeleteMapping("books/deleteAll")
    fun deleteAllBooks() {
        println("Inside controller")
        bookService.remove()
    }

}