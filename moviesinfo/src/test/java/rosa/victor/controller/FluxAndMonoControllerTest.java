package rosa.victor.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.test.StepVerifier;

@WebFluxTest(controllers = FluxAndMonoController.class)
@AutoConfigureWebFlux
public class FluxAndMonoControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @Test
  void testFlux() {
    webTestClient
        .get()
        .uri("/flux")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBodyList(Integer.class)
        .hasSize(3);
  }

  @Test
  void testFlux2() {
    var flux = webTestClient
        .get()
        .uri("/flux")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .returnResult(Integer.class)
        .getResponseBody();

    StepVerifier.create(flux)
        .expectNext(1, 2, 3)
        .verifyComplete();
  }

  @Test
  void testMonoHelloWorld() {
    webTestClient.get()
        .uri("/mono")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody(String.class)
        .consumeWith(exchangeResult -> {
          var responseBody = exchangeResult.getResponseBody();
          assertEquals("Hello World", responseBody);
        });
  }

  @Test
  void testStream() {
    var flux = webTestClient
        .get()
        .uri("/stream")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .returnResult(Long.class)
        .getResponseBody();

    StepVerifier.create(flux)
        .expectNext(0L, 1L, 2L, 3L)
        .thenCancel()
        .verify();

  }
}
