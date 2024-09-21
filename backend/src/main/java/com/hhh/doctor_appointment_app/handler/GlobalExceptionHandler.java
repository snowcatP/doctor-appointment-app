package com.hhh.doctor_appointment_app.handler;

import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.exception.UserException;
import com.hhh.doctor_appointment_app.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponse<?> handleException(Exception ex) {
        return createApiResponse("500", "INTERNAL_SERVER_ERROR: " + ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiResponse<?> handleNotFoundException(NotFoundException ex) {
        return createApiResponse("404","The requested resource was not found."+ ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse<?> handleValidationException(ValidationException ex) {
        return createApiResponse("400","Validation errors" + ex.getMessage());
    }

    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponse<?> handleApplicationException(ApplicationException ex) {
        return createApiResponse("500", "INTERNAL_SERVER_ERROR: " + ex.getMessage());
    }

    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ApiResponse<?> handleUserException(UserException ex) {
        return createApiResponse("201", "CREATED_ERROR: " + ex.getMessage());
    }

    private ApiResponse<?> createApiResponse(String statusCode, String message) {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setStatusCode(statusCode);
        apiResponse.setMessage(message);
        return apiResponse;
    }
}
