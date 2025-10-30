package com.linkcutter.linkcutter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.linkcutter.linkcutter.service.RedirectService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RedirectController {

    private RedirectService redirectService;

    @GetMapping("/{id}")
    public ResponseEntity<Void> redirect(@PathVariable String id) {
        return new ResponseEntity<>(redirectService.redirect(id), HttpStatus.MOVED_PERMANENTLY);
    }
}
