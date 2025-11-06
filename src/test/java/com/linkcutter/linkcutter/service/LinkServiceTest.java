package com.linkcutter.linkcutter.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.linkcutter.linkcutter.config.LinkServiceProperties;
import com.linkcutter.linkcutter.entity.Link;
import com.linkcutter.linkcutter.model.ShortLinkResponse;
import com.linkcutter.linkcutter.repository.LinkRepository;

public class LinkServiceTest {

    private LinkRepository linkRepository;
    private LinkService linkService;

    private final String originalLink = "https://example.com";
    private final int shortLinkIdLength = 8;
    private final String appDomainName = "clck.ru";

    @BeforeEach
    void setUp() {
        linkRepository = mock(LinkRepository.class);
        LinkServiceProperties properties = new LinkServiceProperties(shortLinkIdLength, appDomainName);
        linkService = new LinkService(linkRepository, properties);
    }

    @Test
    void shouldCreateShortLink() {

        when(linkRepository.findByOriginalLink(originalLink)).thenReturn(Optional.empty());
        when(linkRepository.existsByShortLinkId(anyString())).thenReturn(false);

        ShortLinkResponse response = linkService.createShortLink(originalLink);

        assertTrue(response.getShortLink().startsWith("https://" + appDomainName + "/"));
        verify(linkRepository).save(any(Link.class));
        verify(linkRepository).findByOriginalLink(originalLink);
        verify(linkRepository).existsByShortLinkId(anyString());
    }

    @Test
    void shouldThrowIllegalArgumentException_WrongLinkFormat() {
        String invalidLink = "htp:/invalid";

        assertThrows(IllegalArgumentException.class,() -> {
            linkService.createShortLink(invalidLink);
        });
    }
}
