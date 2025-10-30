package com.linkcutter.linkcutter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.linkcutter.linkcutter.entity.IllegalArgumentErrorResposnse;
import com.linkcutter.linkcutter.entity.InsufficientSystemErrorResponse;
import com.linkcutter.linkcutter.entity.NotFoundErrorResponse;
import com.linkcutter.linkcutter.exception.InsufficientSystemStateException;
import com.linkcutter.linkcutter.exception.ShortLinkNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ShortLinkNotFoundException.class)
    public ResponseEntity<NotFoundErrorResponse> shortLinkNotFoundExceptionHandler(ShortLinkNotFoundException ex) {
        NotFoundErrorResponse error = new NotFoundErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InsufficientSystemStateException.class)
    public ResponseEntity<InsufficientSystemErrorResponse> insufficientSystemStateExceptionHandler(InsufficientSystemStateException ex) {
        InsufficientSystemErrorResponse error = new InsufficientSystemErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<IllegalArgumentErrorResposnse> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        IllegalArgumentErrorResposnse error = new IllegalArgumentErrorResposnse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
