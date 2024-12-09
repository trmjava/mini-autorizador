package org.rogs.app.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.rogs.app.dto.TransacaoRequestDTO;
import org.rogs.app.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TransacaoController.class)
public class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransacaoService transacaoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testProcessarTransacao_OK() throws Exception {
        TransacaoRequestDTO requestDTO = new TransacaoRequestDTO();

        doNothing().when(transacaoService).processarTransacao(any(TransacaoRequestDTO.class));

        mockMvc.perform(post("/transacoes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("OK")));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testProcessarTransacao_CARTAO_INEXISTENTE() throws Exception {
        TransacaoRequestDTO requestDTO = new TransacaoRequestDTO();

        doThrow(new EntityNotFoundException("CARTAO_INEXISTENTE")).when(transacaoService).processarTransacao(any(TransacaoRequestDTO.class));

        mockMvc.perform(post("/transacoes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("CARTAO_INEXISTENTE")));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testProcessarTransacao_SENHA_INVALIDA() throws Exception {
        TransacaoRequestDTO requestDTO = new TransacaoRequestDTO();

        doThrow(new IllegalArgumentException("SENHA_INVALIDA")).when(transacaoService).processarTransacao(any(TransacaoRequestDTO.class));

        mockMvc.perform(post("/transacoes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("SENHA_INVALIDA")));
    }
}

