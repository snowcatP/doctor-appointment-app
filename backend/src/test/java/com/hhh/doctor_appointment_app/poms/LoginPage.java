package com.hhh.doctor_appointment_app.poms;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private final WebDriver driver;

    private final By emailTextBox = By.id("email");
    private final By passwordTextBox = By.id("password");
    private final By loginButton = By.id("loginBtn");

    private final By errorMessage = By.id("errorMessage");
    @Getter
    private final By toast = By.id("toast");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterEmail(String email) {
        driver.findElement(emailTextBox).sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordTextBox).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }
}
