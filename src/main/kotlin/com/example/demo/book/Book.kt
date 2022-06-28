package com.example.demo.book

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

//our model by using Kotlin primary constructor concise syntax which allows to declare at the same time the properties and the constructor parameters.
@Document("books") // collection name (By default, the name of the collection will be the class name changed to start with a lower-case letter)
data class Book(
    @Id
    var title: String,
    var image: String,
    var author: String,
    var description : String,
    var price: Int,
    var quantity: Int)