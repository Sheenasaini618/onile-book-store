
package com.example.demo.book

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BookService( @Autowired private val bookRepository : BookRepository) {

    // CRUD operations applied for books

    // function for retrieving all the books

    fun findAll() : Flux<Book> =
        bookRepository.findAll()

    // function for adding a book

    fun createBooks(book: Book) : Mono<Book> {
        return bookRepository.insert(book)
    }

    // function for deleting all books of a particular title

    fun deleteAllBooksOnTheBasisOfTheTitle(bookTitle : String){
        bookRepository.deleteById(bookTitle).subscribe()
    }

    // function for updating a book

    fun updatingBook(book: Book):Mono<Book>{
        return bookRepository.save(book)
    }

    // function for finding the book on the basis of the title

    fun findBookByTitle(bookTitle : String) : Mono<Book>{
        return bookRepository.findById(bookTitle)
    }

    fun findBookByAuthor(bookAuthor : String) : Flux<Book> {
        return bookRepository.findByAuthor(bookAuthor)
    }

    fun remove() {
         bookRepository.deleteAll().subscribe()
    }

    // function for finding the book on the basis of the author

//    fun findBookByAuthor(bookAuthor : String) : Mono<Book>{
//        return bookRepository.findBy({"author" :"Michael" , })
//    }
}