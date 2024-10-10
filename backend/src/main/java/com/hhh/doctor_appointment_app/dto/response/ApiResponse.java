package com.hhh.doctor_appointment_app.dto.response;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public void ok() {
        this.statusCode = HttpStatus.OK.value();
        this.message="SUCCESS";
    }
    public void duplicatedCode(){
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = "Duplicated!";
    }

    public void ok(T data) {
        this.statusCode = HttpStatus.OK.value();
        this.message="SUCCESS";
        this.data = data;
    }

    public void notFound(){
        this.statusCode = HttpStatus.NOT_FOUND.value();
        this.message="NOT FOUND";
    }

    public void validationError(Map<String, String> errors) {
        this.statusCode = HttpStatus.BAD_REQUEST.value();
        this.message = "Validation failed";
        this.data = (T) errors; // Sử dụng cast an toàn, hoặc thay đổi kiểu dữ liệu của `data` để phù hợp
    }
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
