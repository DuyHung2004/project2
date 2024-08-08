package com.example.spring2.exception;

import com.example.spring2.dto.request.ApiResponse;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalException {

    private static final String MIN_ATTRIBUTE="min";

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
        ApiResponse apiResponse= new ApiResponse();
        apiResponse.setCode(1001);
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception){
        ErrorCode errorCode= exception.getErrorCode();
        ApiResponse apiResponse= new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation( MethodArgumentNotValidException e){
        String enumkey= e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String,Object> attributes =null;
        try {
            errorCode = ErrorCode.valueOf(enumkey);
            var constraintViolation =
                    e.getBindingResult().getAllErrors()
                            .get(0).unwrap(ConstraintViolation.class);
           attributes = constraintViolation.getConstraintDescriptor().getAttributes();

        } catch (IllegalArgumentException e1){}
        ApiResponse apiResponse= new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(Objects.nonNull(attributes) ?
                mapAttribute(errorCode.getMessage(),attributes)
                :errorCode.getMessage());
       return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception){
        ErrorCode errorCode= ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }
    private String mapAttribute(String message,Map<String, Object> attributes){
        String minValue=String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{"+MIN_ATTRIBUTE +"}", minValue);
    }
}
