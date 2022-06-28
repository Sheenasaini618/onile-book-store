package com.example.demo.book

import io.kotlintest.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult

@SpringBootTest
@AutoConfigureWebTestClient
class IntegrationTests {

    @Autowired
    lateinit var client : WebTestClient

    @Autowired
    lateinit var bookRepository : BookRepository


    @BeforeEach
    fun setUp() {
        bookRepository.deleteAll().subscribe()
    }

    @Test
    fun `should get the lists of book from database`() {

        val result = mapOf(
            "title" to "ab" ,
            "image" to "image.png" ,
            "author" to "def" ,
            "description" to "ghi" ,
            "price" to 100 ,
            "quantity" to 2
        )

        val book = Book("ab" , "image.png" , "def" , "ghi" , 100 , 2)

        bookRepository.insert(book).block()

        val response = client.get()
            .uri("/api/v1/books/list")
            .exchange()
            .expectStatus().is2xxSuccessful
            .returnResult<Any>()
            .responseBody.blockFirst()

        response shouldBe result
    }

    @Test
    fun `should get the book from the database on the basis of the title`() {

        val result = mapOf(
            "title" to "probab" ,
            "image" to "image.png" ,
            "author" to "Michael" ,
            "description" to "mathematics" ,
            "price" to 1000 ,
            "quantity" to 4
        )

        val book = Book("probab" , "image.png" , "Michael" , "mathematics" , 1000 , 4)

        bookRepository.insert(book).block()

        val response = client.get()
            .uri("/api/v1/books/list/title/${book.title}")
            .exchange()
            .expectStatus().is2xxSuccessful
            .returnResult<Any>()
            .responseBody.blockFirst()

        response shouldBe result
    }

    @Test
    fun `should get the book from the database on the basis of the author`() {

        val result = mapOf(
            "title" to "probab" ,
            "image" to "image.png" ,
            "author" to "Michael" ,
            "description" to "mathematics" ,
            "price" to 1000 ,
            "quantity" to 4
        )

        val book = Book("probab" , "image.png" , "Michael" , "mathematics" , 1000 , 4)

        bookRepository.insert(book).block()

        val response = client.get()
            .uri("/api/v1/books/list/author/${book.author}")
            .exchange()
            .expectStatus().is2xxSuccessful
            .returnResult<Any>()
            .responseBody.blockFirst()

        response shouldBe result
    }

    @Test
    fun `should create book in the database on the basis`() {

        val result = mapOf(
            "title" to "probab" ,
            "image" to "image.png" ,
            "author" to "Michael" ,
            "description" to "mathematics" ,
            "price" to 1000 ,
            "quantity" to 4
        )

        val book = Book("probab" , "image.png" , "Michael" , "mathematics" , 1000 , 4)

        val response = client.post()
            .uri("/api/v1/books/create")
            .bodyValue(book)
            .exchange()
            .expectStatus().is2xxSuccessful
            .returnResult<Any>()
            .responseBody.blockFirst()

        response shouldBe result
    }

    @Test
    fun `should be able to update the book present in the online book store`() {

        val result = mapOf(
            "title" to "probab" ,
            "image" to "image.png" ,
            "author" to "Michael" ,
            "description" to "mathematics" ,
            "price" to 1000 ,
            "quantity" to 5
        )

        val book1 = Book("probab" , "image.png" , "Michael" , "mathematics" , 1000 , 4)
        val book2 = Book("probab" , "image.png" , "Michael" , "mathematics" , 1000 , 5)


        bookRepository.insert(book1)

        val response = client.put()
            .uri("/api/v1/books/update")
            .bodyValue(book2)
            .exchange()
            .expectStatus().is2xxSuccessful
            .returnResult<Any>()
            .responseBody.blockFirst()

        response shouldBe result
    }
}
