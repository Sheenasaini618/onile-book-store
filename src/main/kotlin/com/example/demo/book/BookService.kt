
package com.example.demo.book

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BookService( @Autowired private val bookRepository : BookRepository,  @Autowired private var kafkaProducerService : KafkaProducerService) {

    // CRUD operations applied for books

    // function for retrieving all the books

    fun findAll() : Flux<Book> =
        bookRepository.findAll()

    // function for adding a book

    fun createBooks(book: Book){
       bookRepository.insert(book).subscribe(kafkaProducerService::messageWhenBookIsAdded)
    }

    // function for deleting all books of a particular title

    fun deleteBooksOfParticularId(id : String): Mono<Void>{
        bookRepository.findById(id).subscribe(kafkaProducerService::messageWhenBookIsDeleted)
        return bookRepository.deleteById(id)
    }

    // function for updating a book

    fun updatingBook(id: String, book: Book): Mono<Book>{

       var bookResult = bookRepository.findById(id)
           .flatMap {
               it.price = book.price
               it.quantity = book.quantity
               bookRepository.save(it)
           }
        bookResult.subscribe(kafkaProducerService::messageWhenBookIsUpdated)
        return bookResult
    }

    // function for finding the book on the basis of the title

    fun findByTitle(title : String) : Flux<Book>{
        return bookRepository.findByTitle(title)
    }

    fun findByAuthor(author : String) : Flux<Book>{
        return bookRepository.findByAuthors(author)
    }

    fun remove() : Mono<Void> {
         return bookRepository.deleteAll()
    }

    // Invoking external API (reactive Webclient)

    fun getBookfromApi(query: String): Flux<GooleBook> {

        return WebClient.create(buildUrl(query))
            .get()
            .retrieve()
            .bodyToFlux(GooleBook::class.java)
    }

    fun buildUrl(query: String): String {
        return UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/books/v1/volumes")
            .replaceQueryParam("q",query)
            .replaceQueryParam("key","AIzaSyBEz4HtafYUQVctBqG2PbgI7GzXwr4e1Yc")
            .encode().toUriString()
    }
}