package live.databo3.account.advice;

import live.databo3.account.error.ErrorBody;
import live.databo3.account.error.ErrorCode;
import live.databo3.account.error.ErrorHeader;
import live.databo3.account.error.ErrorResponse;
import live.databo3.account.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse<ErrorHeader, ErrorBody>> handleCustomException(CustomException ex) {
        log.info("Adviser(CustomException) run code : {}", ex.getErrorCode());

        ErrorCode errorCode = ex.getErrorCode();
        ErrorHeader errorHeader = ErrorHeader.builder()
                                                    .resultCode(errorCode.getCode())
                                                    .resultMessage(errorCode.getMessage())
                                                    .localDateTime(LocalDateTime.now())
                                                    .build();

        ErrorBody errorBody = ErrorBody.builder()
                                .message(errorCode.getMessage())
                                .build();

        ErrorResponse<ErrorHeader, ErrorBody> error = new ErrorResponse<>();
        error.setHeaders(errorHeader);
        error.setBody(errorBody);

        return ResponseEntity.status(errorCode.getCode()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse<ErrorHeader, ErrorBody>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.info("Advisor(CustomException) run code : {}", ex.getBindingResult());

        StringBuilder message = new StringBuilder();

        for(FieldError error : ex.getFieldErrors()) {
            message.append(error.getField()).append(": (").append(error.getDefaultMessage()).append(") ");
        }

        ErrorHeader errorHeader = ErrorHeader.builder()
                .resultCode(ErrorCode.METHOD_ARGUMENT_ERROR.getCode())
                .resultMessage(message.toString())
                .localDateTime(LocalDateTime.now())
                .build();

        ErrorResponse<ErrorHeader, ErrorBody> error = new ErrorResponse<>();
        error.setHeaders(errorHeader);
        error.setBody(null);

        return ResponseEntity.status(errorHeader.getResultCode()).body(error);
    }

}
