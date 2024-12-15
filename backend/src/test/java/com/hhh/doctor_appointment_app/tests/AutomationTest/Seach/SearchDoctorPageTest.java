package com.hhh.doctor_appointment_app.tests.AutomationTest.Seach;

import com.hhh.doctor_appointment_app.BaseSetup;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.poms.SearchDoctorPage;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SearchDoctorPageTest extends BaseSetup {
    private WebDriver driver;

    @Value("${origins.host}")
    private String host;

    Doctor doctor;

    @Autowired
    private DoctorRepository doctorRepository;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        options.addArguments("--disable-gpu"); // Disable GPU for better compatibility
        options.addArguments("--no-sandbox"); // Optional: Improves security for CI environments
        options.addArguments("--disable-dev-shm-usage");
//        driver = new ChromeDriver(options);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        doctor = doctorRepository.findAll().get(0);
    }

    @Test
    public void searchDoctorSuccess() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(host+ "/search-doctor");
            SearchDoctorPage searchDoctorPage = new SearchDoctorPage(driver);
            searchDoctorPage.enterDoctorName(doctor.getProfile().getFirstName());
            WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("search-btn")));
            searchBtn.click();

            WebElement doctorList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("doctorList")));
            assertTrue(doctorList.getText().contains(doctor.getProfile().getFirstName()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void searchDoctorFail() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(host+ "/search-doctor");
            SearchDoctorPage searchDoctorPage = new SearchDoctorPage(driver);
            searchDoctorPage.enterDoctorName("NonExistingDoctor");
            WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("search-btn")));
            searchBtn.click();

            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noDoctors")));
            assertEquals("No doctors available", errorMessage.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        Thread.sleep(5000);
        driver.quit();
    }
}
