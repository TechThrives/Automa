package com.automa.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.automa.dto.MessageResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errors = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        MessageResponse messageResponse = new MessageResponse("Validation Error", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<MessageResponse> handleIOException(IOException ex) {
        MessageResponse messageResponse = new MessageResponse("IO Error", Collections.singletonList(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<MessageResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {
        String message = "HTTP Method Not Allowed";
        List<String> details = List
                .of("Supported Methods: " + Objects.requireNonNull(ex.getSupportedHttpMethods()).toString());
        MessageResponse messageResponse = new MessageResponse(message, details);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(messageResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MessageResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
        MessageResponse messageResponse = new MessageResponse("Not Found", Collections.singletonList(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<MessageResponse> handleBadCredentialException(BadCredentialsException ex,
            WebRequest request) {
        MessageResponse messageResponse = new MessageResponse("Bad Credentials",
                Arrays.asList("Invalid Email or Password!!!"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<MessageResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex,
            WebRequest request) {
        MessageResponse messageResponse = new MessageResponse("Resource Already Exists",
                Collections.singletonList(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        MessageResponse messageResponse = new MessageResponse("An Error Occurred",
                Collections.singletonList(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<MessageResponse> handleAuthenticationException(AuthenticationException ex,
            WebRequest request) {
        MessageResponse messageResponse = new MessageResponse("Authentication Error",
                Collections.singletonList("You need to authenticate to access this resource."));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MessageResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        MessageResponse messageResponse = new MessageResponse("Access Denied",
                Collections.singletonList("You do not have permission to access this resource."));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<MessageResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        MessageResponse messageResponse = new MessageResponse("Resource Not Found",
                Collections.singletonList(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponse);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleAllExceptions(Exception ex, WebRequest request) {
        System.out.println(ex.getMessage());
        MessageResponse messageResponse = new MessageResponse("Internal Server Error",
                Arrays.asList(
                        "Our server encountered some technical issues. We're working to resolve it as quickly as possible. Please try again later."));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<MessageResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        MessageResponse messageResponse = new MessageResponse("Invalid Argument Type",
                Collections.singletonList(ex.getMessage()));
        if (ex.getRequiredType() == UUID.class) {
            messageResponse.setMessage("Invalid UUID");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
    }
}

class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}