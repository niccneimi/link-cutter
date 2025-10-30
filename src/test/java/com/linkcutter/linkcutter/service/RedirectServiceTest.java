package com.linkcutter.linkcutter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import com.linkcutter.linkcutter.exception.ShortLinkNotFoundException;
import com.linkcutter.linkcutter.model.Link;
import com.linkcutter.linkcutter.repository.LinkRepository;

public class RedirectServiceTest {
    private LinkRepository linkRepository;
    private RedirectService redirectService;

    @BeforeEach
    void setUp() {
        linkRepository = mock(LinkRepository.class);
        redirectService = new RedirectService(linkRepository);
    }

    @Test
    void shouldThrowShortLinkNotFoundException_WhenUnknownShortLinkId() {

        String unknownShortLinkId = "unknownShortLinkId";

        when(linkRepository.findByShortLinkId(unknownShortLinkId)).thenReturn(Optional.empty());

        assertThrows(ShortLinkNotFoundException.class, () -> {
            redirectService.redirect(unknownShortLinkId);
        });
    }

    @Test
    void shouldReturnHttpHeadersWithLocation_WhenShortLinkExists() {

        String shortLinkId = "abcde12345";
        String originalLink = "https://example.com";

        Link link = new Link(originalLink, shortLinkId);

        when(linkRepository.findByShortLinkId(shortLinkId)).thenReturn(Optional.of(link));

        HttpHeaders headers = redirectService.redirect(shortLinkId);
        assertNotNull(headers);
        assertEquals(URI.create(originalLink), headers.getLocation());
        verify(linkRepository).findByShortLinkId(shortLinkId);
    }

}
