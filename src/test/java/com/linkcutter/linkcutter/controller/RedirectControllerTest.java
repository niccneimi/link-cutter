package com.linkcutter.linkcutter.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.linkcutter.linkcutter.service.RedirectService;

public class RedirectControllerTest {

    private MockMvc mockMvc;
    private RedirectService redirectService;
    private RedirectController redirectController;

    @BeforeEach
    void setUp() {
        redirectService = Mockito.mock(RedirectService.class);
        redirectController = new RedirectController(redirectService);
        mockMvc = MockMvcBuilders.standaloneSetup(redirectController).build();
    }

    @Test
    void shouldReturnMovedPermanentlyWithLocationHeader() throws Exception {
        String shortLinkId = "abcde12345";
        String originalLink = "https://example.com";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalLink));

        when(redirectService.redirect(shortLinkId)).thenReturn(headers);

        mockMvc.perform(get("/{id}", shortLinkId))
                .andExpect(status().isMovedPermanently())
                .andExpect(header().string("Location", originalLink));
    }
}
