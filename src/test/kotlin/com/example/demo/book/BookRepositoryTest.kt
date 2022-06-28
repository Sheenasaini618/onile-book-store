package com.example.demo.book

import io.kotlintest.shouldBe
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.dao.DuplicateKeyException
import org.springframework.test.context.ActiveProfiles

@DataMongoTest
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    lateinit var bookRepository : BookRepository

    @AfterEach
    fun tearDown() {
        bookRepository.deleteAll().block()
    }

    @Test
    fun `should find book for given author `() {

        val book = Book("abdc" , "image.png" , "def" , "ghi" , 100 , 2)

        bookRepository.save(book).block()

        val actualSaveApplication = bookRepository.findByAuthor(
            "def"
        ).blockFirst()

        actualSaveApplication shouldBe book
    }

    @Test
    fun `should find book for given title`() {

        val book = Book("abdc" , "image.png" , "def" , "ghi" , 100 , 2)

        bookRepository.save(book).block()

        val actualSaveApplication = bookRepository.findById(
            "abdc"
        ).block()

        println(actualSaveApplication)
        actualSaveApplication shouldBe book
    }

    @Test
    fun `should not store duplicate application with same title`() {

        val book1 = Book("abdc" , "image.png" , "robo" , "ghi" , 1000 , 2)
        val book2 = Book("abdc" , "image.png" , "def" , "ghi" , 100 , 2)

        bookRepository.insert(book1).block()

        assertThrows<DuplicateKeyException> { bookRepository.insert(book2).block() }

    }

}