package com.linkcutter.linkcutter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linkcutter.linkcutter.model.Link;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long>{
    Optional<Link> findByOriginalLink(String originalLink);
    Optional<Link> findByShortLinkId(String shortLinkId);
    Boolean existsByShortLinkId(String shortLinkId);
}
