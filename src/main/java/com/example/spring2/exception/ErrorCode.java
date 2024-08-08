package com.example.spring2.exception;

import lombok.Getter;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErrorCode {
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002,"User da ton tai.", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003,"user toi thieu {min} ky tu.",HttpStatus.BAD_REQUEST),
    INVALID_PASSSWORD(1004,"Password co toi thieu {min} ky tu",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002,"USER not EXISTED",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006,"USER NOT existed ",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007,"You Do Not have permission",HttpStatus.FORBIDDEN),
    INVALID_DOB(1008,"phai tren {min} tuoi",HttpStatus.BAD_REQUEST);
            ;
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
    ErrorCode(int code, String message,HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode= statusCode;
    }

}
