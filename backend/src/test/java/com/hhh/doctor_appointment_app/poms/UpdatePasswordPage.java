package com.hhh.doctor_appointment_app.poms;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UpdatePasswordPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By changePasswordSidebar = By.id("changePasswordBtn");
    private By oldPasswordTextBox = By.cssSelector("#oldPassword input");
    private By newPasswordTextBox = By.cssSelector("#newPassword input");
    private By confirmPasswordTextBox = By.cssSelector("#confirmPassword input");

    private By submitButton = By.id("submitBtn");

    private By errorMessage = By.id("errorMessage");
    @Getter
    private By toast = By.id("toast");

    public UpdatePasswordPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickChangePassword() {
        WebElement changePasswordButton = wait.until(ExpectedConditions.elementToBeClickable(changePasswordSidebar));
        changePasswordButton.click();
    }
    public void enterOldPassword(String oldPassword) {
        driver.findElement(oldPasswordTextBox).clear();
        driver.findElement(oldPasswordTextBox).sendKeys(oldPassword);
    }

    public void enterNewPassword(String newPassword) {
        driver.findElement(newPasswordTextBox).clear();
        driver.findElement(newPasswordTextBox).sendKeys(newPassword);
    }

    public void enterConfirmPassword(String confirmPassword) {
        driver.findElement(confirmPasswordTextBox).clear();
        driver.findElement(confirmPasswordTextBox).sendKeys(confirmPassword);
    }
    public void clickSaveChanges() {
//        driver.findElement(submitButton).click();
        WebElement button = driver.findElement(submitButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }
}
