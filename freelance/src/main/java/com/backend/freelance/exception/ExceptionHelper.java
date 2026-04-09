package com.backend.freelance.exception;


import jdk.jfr.Description;
import org.springframework.http.HttpStatus;

public enum ExceptionHelper {

    @Description("Required Parameters: N/A")
    INTERNAL_SERVER_ERROR(),
    @Description("Required Parameters: N/A")
    BAD_REQUEST(),
    @Description("Required Parameters: N/A")
    UNAUTHORIZED(),
    @Description("Required Parameters: N/A")
    FORBIDDEN(),
    @Description("Required Parameters: N/A")
    NOT_FOUND();

    public final BaseException throwException(Throwable cause, String... params) {
        return throwCustomException(this.name(), cause, params);
    }

    public final BaseException throwCustomException(String translationKey, Throwable cause, String... params) {
        throw new BaseException(HttpStatus.valueOf(this.name()), translationKey, cause, params);
    }


}
