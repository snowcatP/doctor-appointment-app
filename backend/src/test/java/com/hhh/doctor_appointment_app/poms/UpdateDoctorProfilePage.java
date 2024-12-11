package com.hhh.doctor_appointment_app.poms;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Date;

public class UpdateDoctorProfilePage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By updateProfileSide = By.id("updateProfileBtn");
    private By firstNameTextBox = By.id("firstName");
    private By lastNameTextBox = By.id("lastName");
    private By phoneNumberTextBox = By.id("phone");
    private By addressTextBox = By.id("address");
    private By submitButton = By.id("submitBtn");

    private By errorMessage = By.id("errorMessage");
    @Getter
    private By toast = By.id("toast");

    public UpdateDoctorProfilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickProfileSettings() {
        WebElement profileSettingsButton = wait.until(ExpectedConditions.elementToBeClickable(updateProfileSide));
        profileSettingsButton.click();
    }
    public void enterFirstName(String firstName) {
        driver.findElement(firstNameTextBox).clear();
        driver.findElement(firstNameTextBox).sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        driver.findElement(lastNameTextBox).clear();
        driver.findElement(lastNameTextBox).sendKeys(lastName);
    }
    public void enterPhone(String phone) {
        driver.findElement(phoneNumberTextBox).clear();
        driver.findElement(phoneNumberTextBox).sendKeys(phone);
    }
    public void enterAddress(String address) {
        driver.findElement(addressTextBox).clear();
        driver.findElement(addressTextBox).sendKeys(address);
    }
    public void clickSaveChanges() {
        driver.findElement(submitButton).click();
    }
}
