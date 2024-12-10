package com.hhh.doctor_appointment_app.poms;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchDoctorPage {
    private WebDriver driver;
    private By doctorNameTextBox = By.id("doctor-name");
    private By seachDoctorButton = By.id("search-doctor");

    private By errorMessage = By.id("errorMessage");
    @Getter
    private By toast = By.id("toast");

    public SearchDoctorPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterDoctorName(String name) {
        driver.findElement(doctorNameTextBox).sendKeys(name);
    }


    public void clickSeachDoctorButton() {
        driver.findElement(seachDoctorButton).click();
    }
}
