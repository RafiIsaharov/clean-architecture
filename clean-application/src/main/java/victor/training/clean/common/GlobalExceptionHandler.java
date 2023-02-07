package victor.training.clean.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import victor.training.clean.CleanException;
import victor.training.clean.CleanException.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
   private final MessageSource messageSource;

   @ResponseStatus(NOT_FOUND)
   @ExceptionHandler(NoSuchElementException.class)
   public String handleGeneralException() {
      return "Not found";
   }

   @ExceptionHandler(CleanException.class)
   public ResponseEntity<String> onCleanException(HttpServletRequest request, CleanException cleanException) {
      String userMessage = translateError(cleanException, cleanException.getErrorCode(), cleanException.getParameters(), request);
      String httpStatusCodeStr = messageSource.getMessage("error." + cleanException.getErrorCode() + ".code", null, "500", Locale.ENGLISH);
      int httpStatusCode = Integer.parseInt(httpStatusCodeStr);
      return status(httpStatusCode).body(userMessage);
   }

   @ResponseStatus(INTERNAL_SERVER_ERROR)
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public List<String> onJavaxValidationException(HttpServletRequest request, MethodArgumentNotValidException e) {
      return e.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity<String> onAnyException(HttpServletRequest request, Exception exception) {
      String userMessage = translateError(exception, ErrorCode.GENERAL, null, request);
      return internalServerError().body(userMessage);
   }

   private String translateError(Throwable throwable, ErrorCode errorCode, String[] parameters, HttpServletRequest request) {
      String messageKey = "error." + errorCode + ".message";
      String userMessage = messageSource.getMessage(messageKey, parameters, "Internal Error", request.getLocale());
      log.error(String.format("Error occurred [%s]: %s", errorCode, userMessage), throwable);
      return userMessage;
   }



}