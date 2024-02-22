package com.roberthj.musicmasterkotlin.client

import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.net.URI

@Component
class HttpWebClient(val webClient: WebClient) {

   fun getSyncronously(uri: URI, headers: HttpHeaders): String?{

       return webClient
           .get()
           .uri(uri)
           .headers { h -> h.addAll(headers) }
            .retrieve()
//           .onStatus {httpStatus -> !httpStatus.statusCode().is2xxSuccessful,
//          // .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
//           clientResponse -> handleErrorResponse(clientResponse.statusCode())}
            .bodyToMono(String::class.java)
           .block()

   }

    // TODO: What about error handling?

    fun postSyncronously(uri: URI?, headers: HttpHeaders?, body: String): String? {
        return webClient
            .post()
            .uri(uri!!)
            .headers { h: HttpHeaders ->
                h.addAll(
                    headers!!
                )
            }
            .body(Mono.just(body), String::class.java)
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        //TODO: What about error handling?
        // some examples here https://howtodoinjava.com/spring-webflux/webclient-get-post-example/
    }
}