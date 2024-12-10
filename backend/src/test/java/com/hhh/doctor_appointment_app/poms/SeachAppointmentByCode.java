package com.hhh.doctor_appointment_app.poms;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class SeachAppointmentByCode {
    private WebDriver driver;
    private By appointmentCodeTextBox = By.id("appointmentCode");
    private By errorMessage = By.id("errorMessage");
    @Getter
    private By toast = By.id("toast");

    public SeachAppointmentByCode(WebDriver driver) {
        this.driver = driver;
    }

    public void enterAppointmentCode(String appointmentCode) {
        driver.findElement(appointmentCodeTextBox).clear();
        driver.findElement(appointmentCodeTextBox).sendKeys(appointmentCode, Keys.ENTER);
    }

}
