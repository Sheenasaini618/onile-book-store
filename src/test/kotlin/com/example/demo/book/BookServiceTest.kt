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

    private val bookRepository = mockk<BookRepository>(){

        every {
            findAll()
        } returns Flux.just(Book("abdc","image.png","def","ghi",100,2)
            ,Book("abd","image.png","de","gh",100,3))

        every {
           insert(Book("probability","image.png","Algebra","Modern Algebra",100,2))
        }  returns Mono.just(Book("probability","image.png","Algebra","Modern Algebra",100,2))

        every {
            findById("probability")
        } returns Mono.just(Book("probability","image.png","Algebra","Modern Algebra",100,2))

        every {
            findByAuthor("Algebra")
        } returns Flux.just(Book("probability","image.png","Algebra","Modern Algebra",100,2),
            Book("complex","image.png","Algebra","Modern Algebra",1000,3))

        every {
            save(Book("probability","image.png","Algebra","Modern Algebra",100,2))
        }  returns Mono.just(Book("probability","image.png","Algebra","Modern Algebra",100,2))

    }

    private val bookService = BookService(bookRepository)


    @Test
    fun `should return books when find all method is called`() {

        val resultBook1 = Book("abdc","image.png","def","ghi",100,2)
        val resultBook2 = Book("abd","image.png","de","gh",100,3)

        val firstBook =  bookService.findAll().blockFirst()
        val secondBook = bookService.findAll().blockLast()

        firstBook shouldBe resultBook1
        secondBook shouldBe resultBook2
    }

    @Test
    fun `should expect on complete call post all the books are retrieved`() {

        val book1 = Book("abdc","image.png","def","ghi",100,2)
        val book2 = Book("abd","image.png","de","gh",100,3)

        //StepVerifier takes care of subscribing
        StepVerifier.create( bookService.findAll()).expectSubscription().expectNext(book1).expectNext(book2).verifyComplete()
        StepVerifier.create( bookService.findAll()).expectNextCount(2).verifyComplete()
    }

    @Test
    fun `should create a book when create method is called`() {

        val book = Book("probability","image.png","Algebra","Modern Algebra",100,2)
        val result = bookService.createBooks(book).block()

        result shouldBe book
    }

    @Test
    fun `should find the list of books on the basis of the title`() {

        val book = Book("probability","image.png","Algebra","Modern Algebra",100,2)
        val result = bookService.findBookByTitle("probability").block()

        result shouldBe book
    }

    @Test
    fun `should find the list of books on the basis of the author`() {

        val book1 = Book("probability","image.png","Algebra","Modern Algebra",100,2)
        val book2 = Book("complex","image.png","Algebra","Modern Algebra",1000,3)

        val bookResult1 = bookService.findBookByAuthor("Algebra").blockFirst()
        val bookResult2 = bookService.findBookByAuthor("Algebra").blockLast()

        bookResult1 shouldBe book1
        bookResult2 shouldBe book2
    }

    @Test
    fun `should update a book when update method is called`() {

        val book = Book("probability","image.png","Algebra","Modern Algebra",100,2)
        val result = bookService.updatingBook(book).block()

        result shouldBe book
    }
}