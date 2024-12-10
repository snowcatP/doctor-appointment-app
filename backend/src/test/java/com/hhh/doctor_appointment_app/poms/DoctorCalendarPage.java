package com.hhh.doctor_appointment_app.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class DoctorCalendarPage {
    private WebDriver driver;
    public DoctorCalendarPage(WebDriver driver) {
        this.driver = driver;
    }
    private By closeBtn = By.id("close");
    private By rescheduleBtn = By.id("reschedule");
    private By closeRescheduleBtn = By.id("closeReschedule");
    public void clickCloseBtn() {
        driver.findElement(closeBtn).click();
    }
    public void clickRescheduleBtn() {
        driver.findElement(rescheduleBtn).click();
    }
    public void clickCloseRescheduleBtn() {
        driver.findElement(closeRescheduleBtn).click();
    }
}
