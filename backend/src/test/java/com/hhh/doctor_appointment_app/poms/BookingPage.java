package com.hhh.doctor_appointment_app.poms;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BookingPage {
    private final WebDriver driver;
    public BookingPage(WebDriver driver) {
        this.driver = driver;
    }

    private final By specialty = By.id("specialty");
    private final By doctor = By.id("doctor");
    private final By firstName = By.id("firstName");
    private final By lastName = By.id("lastName");
    private final By email = By.id("email");
    private final By phone = By.id("phone");
    private final By reason = By.id("reason");
    private final By nextStep2 = By.id("nextStep2");
    private final By nextStep3 = By.id("nextStep3");

    private final By specialtyResult = By.id("specialtyResult");
    private final By doctorResult = By.id("doctorResult");
    private final By fullNameResult = By.id("fullNameResult");

    private final By timeBooking = By.id("timeBooking");
    private By bookingSlot;

    public void enterSpecialty(String specialty) {
        this.driver.findElement(this.specialty).sendKeys(specialty);
    }
    public void enterDoctor(String doctor) {
        this.driver.findElement(this.doctor).sendKeys(doctor);
    }
    public void enterFirstName(String firstName) {
        this.driver.findElement(this.firstName).sendKeys(firstName);
    }
    public void enterLastName(String lastName) {
        this.driver.findElement(this.lastName).sendKeys(lastName);
    }
    public void enterEmail(String email) {
        this.driver.findElement(this.email).sendKeys(email);
    }
    public void enterPhone(String phone) {
        this.driver.findElement(this.phone).sendKeys(phone);
    }
    public void enterReason(String reason) {
        this.driver.findElement(this.reason).sendKeys(reason);
    }
    public void clickNextStep2() {
        this.driver.findElement(this.nextStep2).click();
    }
    public void clickNextStep3() {
        this.driver.findElement(this.nextStep3).click();
    }
    public void clickBookingSlot() {
        this.driver.findElement(this.bookingSlot).click();
    }

    public String getTimeBooking() {
        return this.driver.findElement(this.timeBooking).getText();
    }
    public String getDoctorResult() {
        return this.driver.findElement(this.doctorResult).getText();
    }
    public String getSpecialtyResult() {
        return this.driver.findElement(this.specialtyResult).getText();
    }
}
