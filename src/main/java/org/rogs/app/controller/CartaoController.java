package org.rogs.app.controller;

import jakarta.persistence.EntityNotFoundException;
import org.rogs.app.dto.CartaoRequestDTO;
import org.rogs.app.dto.CartaoResponseDTO;
import org.rogs.app.service.CartaoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {


    private final CartaoService service;

    public CartaoController(CartaoService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<CartaoResponseDTO> criarCartao(@RequestBody CartaoRequestDTO request  ) {
        var responseDTO = CartaoResponseDTO.builder().numeroCartao(request.getNumeroCartao()).senha(request.getSenha()).build();

        try {
            service.criar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (DataIntegrityViolationException e) { // Em caso de duplicidade
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(responseDTO);
        }
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<String> getCartao(@PathVariable String numeroCartao) {
       try {
           BigDecimal saldo = service.getSaldo(numeroCartao);
           return ResponseEntity.ok(String.format(Locale.US,"%.2f", saldo));
       } catch (EntityNotFoundException e) {
           return ResponseEntity.notFound().build();
       }
    }


}
