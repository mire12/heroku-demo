package com.itradix.ehealth;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import com.itradix.ehealth.exception.ContentNotAllowedException;
import com.itradix.ehealth.exception.JwtAuthorizationException;
import com.itradix.ehealth.exception.PatientNotFoundException;
import com.itradix.ehealth.exception.UserNotFoundException;
import com.itradix.ehealth.model.ApiError;

@ControllerAdvice
public class GlobalExceptionHandler {
	/** Provides handling for exceptions throughout this service. */
	@ExceptionHandler({ PatientNotFoundException.class, ContentNotAllowedException.class, UserNotFoundException.class, JwtAuthorizationException.class })
	public final ResponseEntity<ApiError> handleException(Exception ex, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();

		if (ex instanceof PatientNotFoundException) {
			HttpStatus status = HttpStatus.NOT_FOUND;
			PatientNotFoundException unfe = (PatientNotFoundException) ex;
			return handlePatientNotFoundException(unfe, headers, status, request);
		} else if (ex instanceof ContentNotAllowedException) {
			HttpStatus status = HttpStatus.BAD_REQUEST;
			ContentNotAllowedException cnae = (ContentNotAllowedException) ex;
			return handleContentNotAllowedException(cnae, headers, status, request);
		} else if (ex instanceof JwtAuthorizationException) {
			HttpStatus status = HttpStatus.UNAUTHORIZED;
			JwtAuthorizationException jae = (JwtAuthorizationException) ex;
			return handleJwtAuthException(jae, headers, status, request);
		} else if (ex instanceof UserNotFoundException) {
			HttpStatus status = HttpStatus.UNAUTHORIZED;
			UserNotFoundException unae = (UserNotFoundException) ex;
			return handleUnauthorizedException(unae, headers, status, request);
		} else {
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
			return handleExceptionInternal(ex, null, headers, status, request);
		}
	}

	protected ResponseEntity<ApiError> handlePatientNotFoundException(PatientNotFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		List<String> errors = Collections.singletonList(ex.getMessage());
		return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
	}

	protected ResponseEntity<ApiError> handleContentNotAllowedException(ContentNotAllowedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errorMessages = ex.getErrors().stream()
				.map(contentError -> contentError.getObjectName() + " " + contentError.getDefaultMessage())
				.collect(Collectors.toList());

		return handleExceptionInternal(ex, new ApiError(errorMessages), headers, status, request);
	}

	protected ResponseEntity<ApiError> handleUnauthorizedException(UserNotFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		List<String> errorMessages = Collections.singletonList(ex.getMessage());
		return handleExceptionInternal(ex, new ApiError(errorMessages), headers, status, request);
	}
	
	protected ResponseEntity<ApiError> handleJwtAuthException(JwtAuthorizationException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		List<String> errorMessages = Collections.singletonList(ex.getMessage());
		return handleExceptionInternal(ex, new ApiError(errorMessages), headers, status, request);
	}

	protected ResponseEntity<ApiError> handleExceptionInternal(Exception ex, ApiError body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, RequestAttributes.SCOPE_REQUEST);
		}

		return new ResponseEntity<>(body, headers, status);
	}
}
