package com.linkcutter.linkcutter.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkcutter.linkcutter.model.LinkRequest;
import com.linkcutter.linkcutter.model.ShortLinkResponse;
import com.linkcutter.linkcutter.service.LinkService;

public class LinkControllerTest {

    private MockMvc mockMvc;
    private LinkController linkController;
    private LinkService linkService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        linkService = mock(LinkService.class);
        linkController = new LinkController(linkService);
        mockMvc = MockMvcBuilders.standaloneSetup(linkController).build();
    }

    @Test
    void shouldReturnCreatedWithShortLinkResponse() throws Exception {
        String shortLinkId = "abcde12345";
        String appDomainName = "clck.ru";
        String shortLink = "https://" + appDomainName + "/" + shortLinkId;
        ShortLinkResponse response = ShortLinkResponse.builder().shortLink(shortLink).build();

        String originalLink = "https://example.com";
        LinkRequest request = new LinkRequest();
        request.setLink(originalLink);

        when(linkService.createShortLink(originalLink)).thenReturn(response);

        mockMvc.perform(post("/api/link")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.shortLink").value(response.getShortLink()));
    }
}
