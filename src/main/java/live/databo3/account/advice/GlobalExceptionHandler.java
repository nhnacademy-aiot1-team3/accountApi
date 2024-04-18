package live.databo3.account.advice;

import live.databo3.account.error.ErrorCode;
import live.databo3.account.error.ErrorResponse;
import live.databo3.account.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        log.info("Adviser(CustomException) run code : {}", ex.getErrorCode());

        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                    .code(errorCode.getCode())
                                                    .message(errorCode.getMessage())
                                                    .localDateTime(LocalDateTime.now())
                                                    .build();

        return ResponseEntity.status(errorCode.getCode()).body(errorResponse);
    }

}
