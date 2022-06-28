package com.example.demo.book

import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

//@SpringBootTest
@WebFluxTest(BookController::class)
@AutoConfigureWebTestClient
class BookControllerTest {

    //  We can use WebClient to create a client to retrieve data from the endpoints provided by the BookController

    @Autowired
    lateinit var client : WebTestClient

    @Autowired
    lateinit var bookService : BookService

    @BeforeEach
    fun setup(){
         bookService = mockk<BookService>() {
            every {
                findAll()
            } returns Flux.just(
                Book("abdc" , "image.png" , "def" , "ghi" , 100 , 2) ,
                Book("abd" , "image.png" , "de" , "gh" , 100 , 3)
            )
            every {
                createBooks(Book("abdc" , "image.png" , "def" , "ghi" , 100 , 2))
            } returns Mono.just(
                Book("abdc" , "image.png" , "def" , "ghi" , 100 , 2)
            )
            every {
                updatingBook(Book("abdc" , "image.png" , "def" , "ghi" , 100 , 2))
            } returns Mono.just(
                Book("abdc" , "image.png" , "def" , "ghi" , 100 , 2)
            )
            every {
                findBookByTitle("probability")
            } returns Mono.just(
                Book("probability" , "image.png" , "def" , "ghi" , 100 , 2)
            )
            every {
                findBookByAuthor("Michael")
            } returns Flux.just(
                Book("probability" , "image.png" , "def" , "ghi" , 100 , 2) ,
                Book("abdc" , "image.png" , "def" , "ghi" , 100 , 2)
            )
        }
    }

    @Test
    fun `should return list of all the books and to verify that book service is internally called once`() {

        val response = client.get()
            .uri("/api/v1/books/list")
            .accept(MediaType.APPLICATION_JSON)
            .exchange() //invoking the end point
            .expectStatus().is2xxSuccessful
            .returnResult<Any>()
            .responseBody

        println(response)

        val resultBook1 = Book("abdc" , "image.png" , "def" , "ghi" , 100 , 2)
        val resultBook2 = Book("abd" , "image.png" , "de" , "gh" , 100 , 3)

//        response.blockFirst() shouldBe resultBook1
//        response.blockLast() shouldBe resultBook2

        verify(exactly = 1) {
            bookService.findAll()
        }
    }

    @Test
    fun `should create book when create api is being called`() {

        val book = Book("abdc" , "image.png" , "def" , "ghi" , 100 , 2)

//        every {
//            bookService.createBooks(any())
//        } returns Mono.just(
//            book
//        )

        client.post()
            .uri("/api/v1/books/create")
            .bodyValue(book)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(Book::class.java)

        verify(exactly = 1) {
            bookService.createBooks(book)
        }
    }

    @Test
    fun `should return book on the basis of the book title`() {

        val book = Book("probability" , "image.png" , "def" , "ghi" , 100 , 2)

        val response = client.get()
            .uri("/api/v1/books/list/title/probability")
            .exchange()
            .expectStatus().is2xxSuccessful
            .returnResult<Any>()
            .responseBody.blockFirst()

        response shouldBe book

        verify(exactly = 1) {
            bookService.findBookByTitle("probability")
        }
    }

    @Test
    fun `should return book on the basis of the book author`() {

        val book1 = Book("probability" , "image.png" , "def" , "ghi" , 100 , 2)
        val book2 = Book("abdc" , "image.png" , "def" , "ghi" , 100 , 2)

        val response = client.get()
            .uri("/api/v1/books/list/author/Michael")
            .exchange()
            .expectStatus().is2xxSuccessful
            .returnResult<Any>()
            .responseBody

        response.blockFirst() shouldBe book1
        response.blockLast() shouldBe book2

        verify(exactly = 1) {
            bookService.findBookByAuthor("Michael")
        }
    }

    @Test
    fun `should be able to update the book present in the online book store`() {

        val book = Book("abdc" , "image.png" , "def" , "ghi" , 100 , 2)

        val response = client.put()
            .uri("/api/v1/books/update")
            .bodyValue(book)
            .exchange()
            .expectStatus().is2xxSuccessful
            .returnResult<Any>()
            .responseBody

        response.blockFirst() shouldBe book
    }

//    @Test
//    fun `should be able to delete all the books`() {
//
//        val result = null
//
//        val response = client.delete()
//            .uri("/api/v1/books/deleteAll")
//            .exchange()
//            .expectStatus().is2xxSuccessful
//            .returnResult<Any>()
//            .responseBody.blockFirst()
//
//        response shouldBe result
//
//    }
//
//    @Test
//    fun `should be able to delete the books on the basis of the title`() {
//
//        val result = null
//
//        val response = client.delete()
//            .uri("/api/v1/books/delete/probab")
//            .exchange()
//            .expectStatus().is2xxSuccessful
//            .returnResult<Any>()
//            .responseBody.blockFirst()
//
//        response shouldBe result
//    }
}
