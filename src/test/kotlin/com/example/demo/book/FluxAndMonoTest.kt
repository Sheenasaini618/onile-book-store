package com.example.demo.book

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.error
import reactor.test.StepVerifier
import java.lang.System.err

class FluxAndMonoTest {

    @Test
    fun `fluxTest`(){
        val stringFlux : Flux<String> = Flux.just("Spring", "Springbbot","Reactive Spring").log()
        stringFlux.subscribe(System.out::println);
    }

    @Test
    fun `monoTest`(){
       val stringMono: Mono<String> = Mono.just("Spring").log();
        StepVerifier.create(stringMono).expectNext("Spring").verifyComplete()
    }
}