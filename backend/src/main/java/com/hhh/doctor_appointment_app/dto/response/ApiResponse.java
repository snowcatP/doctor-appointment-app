package com.hhh.doctor_appointment_app.dto.response;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private String statusCode;
    private String message;
    private T data;

    public void ok() {
        this.statusCode = "200";
        this.message="SUCCESS";
    }
    public void duplicatedCode(){
        this.statusCode = "500";
        this.message = "The code of training program is duplicated!";
    }

    public void ok(T data) {
        this.statusCode = "200";
        this.message="SUCCESS";
        this.data = data;
    }

    public void notFound(){
        this.statusCode = "404";
        this.message="NOT FOUND";
    }

    public void validationError(Map<String, String> errors) {
        this.statusCode = "400";
        this.message = "Validation failed";
        this.data = (T) errors; // Sử dụng cast an toàn, hoặc thay đổi kiểu dữ liệu của `data` để phù hợp
    }
    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
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
