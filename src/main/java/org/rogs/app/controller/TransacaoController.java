package org.rogs.app.controller;

import org.rogs.app.dto.TransacaoRequestDTO;
import org.rogs.app.service.TransacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping
    public ResponseEntity<String> processarTransacao(@RequestBody TransacaoRequestDTO requestDTO) {
        try {
            transacaoService.processarTransacao(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("OK");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }
}
