package com.library.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
class ApiExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "customerror";

    @ExceptionHandler(value = ApiRequestException.class)
    public ModelAndView
    customErrorHandler(ApiRequestException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                e,
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", apiException);
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }


}
