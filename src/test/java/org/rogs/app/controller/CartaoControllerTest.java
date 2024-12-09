package org.rogs.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.rogs.app.dto.CartaoRequestDTO;
import org.rogs.app.model.Cartao;
import org.rogs.app.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(CartaoController.class)
public class CartaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartaoService cartaoService;

    @Test
    @WithMockUser(roles = "USER")
    public void testGetSaldo_OK() throws Exception {
        Cartao cartao = new Cartao();
        cartao.setId(1L);
        cartao.setNumeroCartao("6549873025634501");
        cartao.setSaldo(new BigDecimal("25.00"));
        when(cartaoService.getSaldo("6549873025634501")).thenReturn(new BigDecimal("25.00"));

        mockMvc.perform(MockMvcRequestBuilders.get("/cartoes/6549873025634501"))
                .andExpect(status().isOk())
                .andExpect(content().string("25.00"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetSaldo_NOK() throws Exception {

        when(cartaoService.getSaldo("6549873025634501")).thenThrow(new EntityNotFoundException(""));

        mockMvc.perform(MockMvcRequestBuilders.get("/cartoes/6549873025634501"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testcriarCartao_OK() throws Exception {
        CartaoRequestDTO cartaoRequest = new CartaoRequestDTO();
        cartaoRequest.setNumeroCartao("1234567890123456");
        cartaoRequest.setSenha("1234");

        doNothing().when(cartaoService).criar(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.senha", is("1234")))
                .andExpect(jsonPath("$.numeroCartao", is("1234567890123456")));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testcriarCartao_Duplicado() throws Exception {
        CartaoRequestDTO cartaoRequest = new CartaoRequestDTO();
        cartaoRequest.setNumeroCartao("1234567890123456");
        cartaoRequest.setSenha("1234");

        doThrow(DataIntegrityViolationException.class).when(cartaoService).criar(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.senha", is("1234")))
                .andExpect(jsonPath("$.numeroCartao", is("1234567890123456")));

    }

}