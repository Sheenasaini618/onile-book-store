package com.example.demo.book

import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier


class BookServiceTest{

    // mocking the repository layer response

    val book1 = Book("1","probability" , Image("https://image.png","https://image.png") , listOf("Michael"), "abcd" , 2,4)
    val book2 = Book("2","complex Algebra" , Image("https://image.png","https://image.png"), listOf("Robert") , "abcd" , 100 , 3)


    private val bookRepository = mockk<BookRepository>(){

        every {
            findAll()
        } returns Flux.just(book1,book2)

        every {
            insert(book1)
        }  returns Mono.just(book1)

        every {
            findByTitle("probability")
        } returns Flux.just(book1)

        every {
            findByAuthors("Michael")
        } returns Flux.just(book1)
    }

    private val bookService = BookService(bookRepository)


    @Test
    fun `should return books when find all method is called`() {

        val firstBook =  bookService.findAll().blockFirst()
        val secondBook = bookService.findAll().blockLast()

        firstBook shouldBe book1
        secondBook shouldBe book2
    }

    @Test
    fun `should expect on complete call post all the books are retrieved`() {

        //StepVerifier takes care of subscribing

        StepVerifier.create( bookService.findAll()).expectSubscription().expectNext(book1).expectNext(book2).verifyComplete()
        StepVerifier.create( bookService.findAll()).expectNextCount(2).verifyComplete()
    }

//    @Test
//    fun `should create a book when create method is called`() {
//
//        val result = bookService.createBooks(book1)
//
//        result shouldBe book1
//    }

    @Test
    fun `should find the list of books on the basis of the title`() {

        val result = bookService.findByTitle("probability").blockFirst()

        result shouldBe book1
    }

    @Test
    fun `should find the list of books on the basis of the author`() {

        val bookResult = bookService.findByAuthor("Michael").blockFirst()

        bookResult shouldBe book1
    }
}