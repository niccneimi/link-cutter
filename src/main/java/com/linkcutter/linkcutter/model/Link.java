package com.linkcutter.linkcutter.model;

import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "links")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_link", unique = true, nullable = false, columnDefinition = "text")
    private String originalLink;

    @Column(name = "short_link_id", unique = true, nullable = false)
    private String shortLinkId;

    @Column(name = "created_at")
    @CreationTimestamp
    private OffsetDateTime createdAt;

    public Link() {}

    public Link(String originalLink, String shortLinkId) {
        this.originalLink = originalLink;
        this.shortLinkId = shortLinkId;
    }
}
