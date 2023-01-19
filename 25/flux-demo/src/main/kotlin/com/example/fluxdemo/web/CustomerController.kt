package com.example.fluxdemo.web

import com.example.fluxdemo.domain.Customer
import com.example.fluxdemo.domain.CustomerRepository
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many
import java.time.Duration

@RestController
class CustomerController(
    private val customerRepository: CustomerRepository,
) {

    /**
     * A 요청 -> Flux -> Stream
     * B 요청 -> Flux -> Stream
     * => Flux.merge -> sink
     * sink 는 모든 클라이언트가 접근할 수 있음
     */
    private val sink: Many<Customer> = Sinks
        .many()
        .multicast()
        .onBackpressureBuffer()

    @GetMapping("/flux")
    fun flux(): Flux<Int> = Flux.just(1, 2, 3, 4, 5)
        .delayElements(Duration.ofSeconds(1)).log()

    // APPLICATION_STREAM_JSON => Deprecated
    @GetMapping("/fluxstream", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun fluxStream(): Flux<Int> = Flux.just(1, 2, 3, 4, 5)
        .delayElements(Duration.ofSeconds(1)).log()

    @GetMapping("/customers", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun findAll(): Flux<Customer> = customerRepository.findAll()
        .delayElements(Duration.ofSeconds(1))
        .log()

    @GetMapping("/customers/{id}")
    fun findById(@PathVariable id: Long): Mono<Customer> = customerRepository.findById(id).log()


    /**
     * APPLICATION_NDJSON_VALUE (APPLICATION_STREAM_JSON) 는 데이터를 다 던져주면 연결 끊음
     * SSE 는 연결이 끊기지 않음
     */
    @GetMapping("/customers/sse") // produces = [MediaType.TEXT_EVENT_STREAM_VALUE] 생략 가능
    fun findAllSse(): Flux<ServerSentEvent<Customer>> = sink.asFlux().map { c ->
        ServerSentEvent.builder(c).build()
    }.doOnCancel {
        /**
         * 클라이언트에서 요청을 끊어도 서버에서 모름 -> 알려주기 위해
         * 강제로 마지막 데이터를 날려줌으로써 complete 시킴
         */
        sink.asFlux().blockLast()
    }

    @PostMapping("/customers")
    fun save() = customerRepository.save(Customer("Gildong", "Hong"))
        .doOnNext {customer -> sink.tryEmitNext(customer)}
}