package com.linkcutter.linkcutter.service;

import java.math.BigInteger;
import java.net.URI;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.linkcutter.linkcutter.entity.ShortLinkResponse;
import com.linkcutter.linkcutter.exception.InsufficientSystemStateException;
import com.linkcutter.linkcutter.model.Link;
import com.linkcutter.linkcutter.repository.LinkRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LinkService {

    @Value("${SHORT_LINK_ID_LENGTH}")
    private Integer shortLinkIdLength;

    @Value("${spring.application.name}")
    private String appDomainName;

    private final static String DATA_FOR_RANDOM_STRING = "abcdefghijklmnopqrstuvwxuz1234567890";
    private final static SecureRandom random = new SecureRandom();

    private final LinkRepository linkRepository;

    public ShortLinkResponse createShortLink(String link) {

        if (!isValidLink(link)) {
            throw new IllegalArgumentException("Not valid link: " + link);
        }

        String shortLinkId;

        Optional<Link> existingLinkOptional = linkRepository.findByOriginalLink(link);
        if (existingLinkOptional.isPresent()) {
            Link existingLink = existingLinkOptional.get();
            shortLinkId = existingLink.getShortLinkId();
        } else {
            List<Link> existingLinks = linkRepository.findAll();
            if (!isSystemStateInsufficient(existingLinks)) {
                throw new InsufficientSystemStateException("System state insufficient to generate new links due improper configuration");
            }

            shortLinkId = generateRandomUniqueString(shortLinkIdLength, existingLinks);

            Link newLink = new Link(link, shortLinkId);
            linkRepository.save(newLink);
        }

        String shortLink = "http://" + appDomainName + "/" + shortLinkId;
        
        return ShortLinkResponse.builder()
                .shortLink(shortLink)
                .build();
    }

    private String generateRandomUniqueString(Integer stringLength, List<Link> existingLinks) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            int randCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char charAt = DATA_FOR_RANDOM_STRING.charAt(randCharAt);
            sb.append(charAt);
        }

        String generatedString = sb.toString();
        for (Link existingLink : existingLinks) {
            if (generatedString.equals(existingLink.getShortLinkId())) {
                return generateRandomUniqueString(stringLength, existingLinks);
            }
        }
        return generatedString;
    }

    private BigInteger factorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    private BigInteger permutations(int n, int k) {
        return factorial(n).divide(factorial(n - k).multiply(factorial(k)));
    }

    private Boolean isSystemStateInsufficient(List<Link> existingLinks) {
        int linksWithShortLinkIdLengthCount = 0;
        for (Link existingLink : existingLinks) {
            if (existingLink.getShortLinkId().length() == shortLinkIdLength) {
                linksWithShortLinkIdLengthCount += 1;
            }
        }

        if (shortLinkIdLength < 1 || DATA_FOR_RANDOM_STRING.length() < 1)
            return false;

        if (permutations(DATA_FOR_RANDOM_STRING.length(), shortLinkIdLength).compareTo(BigInteger.valueOf(linksWithShortLinkIdLengthCount)) <= 0)
            return false;
        return true;
    }

    private Boolean isValidLink(String link) {
        try {
            URI uri = new URI(link);
            return uri.getScheme() != null && uri.getHost() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
