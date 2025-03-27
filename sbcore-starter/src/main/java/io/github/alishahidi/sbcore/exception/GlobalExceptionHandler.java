package io.github.alishahidi.sbcore.exception;

import io.github.alishahidi.sbcore.i18n.I18nUtil;
import io.github.alishahidi.sbcore.response.ApiResponse;
import io.github.alishahidi.sbcore.s3.exception.BucketPutException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {

    I18nUtil i18nUtil;

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<String>> handleAppException(AppException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(i18nUtil.getMessage(ex.getKey()), ex.getStatus()),
                ex.getStatus()
        );
    }

    @ExceptionHandler(BucketPutException.class)
    public ResponseEntity<ApiResponse<String>> handleBucketPutException(BucketPutException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(i18nUtil.getMessage("error.bucket.put"), HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(i18nUtil.getMessage("malformed.request"), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<String>> handleMissingParams(MissingServletRequestParameterException ex) {
        String message = String.format("%s parameter is missing", ex.getParameterName());
        return new ResponseEntity<>(
                ApiResponse.error(i18nUtil.getMessage("missing.parameter", message), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<String>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(i18nUtil.getMessage("argument.type.mismatch"), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(i18nUtil.getMessage("method.not.supported"), HttpStatus.METHOD_NOT_ALLOWED),
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(i18nUtil.getMessage("media.type.not.supported"), HttpStatus.UNSUPPORTED_MEDIA_TYPE),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE
        );
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiResponse<String>> handleMissingServletRequestPart(MissingServletRequestPartException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(i18nUtil.getMessage("missing.request.part"), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(i18nUtil.getMessage("access.denied"), HttpStatus.FORBIDDEN),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthenticationException(AuthenticationException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(
                        i18nUtil.getMessage("authentication.failed"),
                        HttpStatus.UNAUTHORIZED
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleInsufficientAuth(InsufficientAuthenticationException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(
                        i18nUtil.getMessage("auth.required"),
                        HttpStatus.UNAUTHORIZED
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<String>> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(i18nUtil.getMessage("status.exception"), (HttpStatus) ex.getStatusCode()),
                ex.getStatusCode()
        );
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<String>> handleBindException(BindException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(i18nUtil.getMessage("binding.error"), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNoResourceFoundException(NoResourceFoundException ex) {
        String resourcePath = ex.getResourcePath();
        return new ResponseEntity<>(
                ApiResponse.error(
                        i18nUtil.getMessage("resource.not.found", resourcePath),
                        HttpStatus.NOT_FOUND
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(
                ApiResponse.error(i18nUtil.getMessage("server.error"), HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}