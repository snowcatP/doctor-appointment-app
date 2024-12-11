package com.hhh.doctor_appointment_app.tests.AutomationTest.Seach;

import com.hhh.doctor_appointment_app.BaseSetup;
import com.hhh.doctor_appointment_app.poms.SearchDoctorPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SeachDoctorPageTest extends BaseSetup {
    private WebDriver driver;

    @Value("${origins.host}")
    private String host;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        options.addArguments("--disable-gpu"); // Disable GPU for better compatibility
        options.addArguments("--window-size=1920,1080"); // Optional: Set a virtual window size
        options.addArguments("--no-sandbox"); // Optional: Improves security for CI environments
        options.addArguments("--disable-dev-shm-usage");
//        driver = new ChromeDriver(options);
        driver = new ChromeDriver();
    }

    @Test
    public void searchDoctorSuccess() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(host+ "/search-doctor");
            SearchDoctorPage searchDoctorPage = new SearchDoctorPage(driver);
            searchDoctorPage.enterDoctorName("Hao");
            searchDoctorPage.clickSeachDoctorButton();

            WebElement doctorList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("doctorList")));
            assertTrue(doctorList.getText().contains("Hao"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            driver.quit();
        }
    }

    @Test
    public void searchDoctorFail() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(host+ "/search-doctor");
            SearchDoctorPage searchDoctorPage = new SearchDoctorPage(driver);
            searchDoctorPage.enterDoctorName("NonExistingDoctor");
            searchDoctorPage.clickSeachDoctorButton();

            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noDoctors")));
            assertEquals("No doctors available", errorMessage.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            driver.quit();
        }
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        Thread.sleep(1000);
        driver.quit();
    }
}
