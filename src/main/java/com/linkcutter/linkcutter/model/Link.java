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

    @Column(name = "original_link", unique = true, nullable = false)
    private String originalLink;

    @Column(name = "cutted_link_id", unique = true, nullable = false)
    private String cuttedLinkId;

    @Column(name = "created_at")
    @CreationTimestamp
    private OffsetDateTime createdAt;
}
