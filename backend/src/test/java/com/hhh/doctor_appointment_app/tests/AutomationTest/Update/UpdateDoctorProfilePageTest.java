package com.hhh.doctor_appointment_app.tests.AutomationTest.Update;

import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.poms.LoginPage;
import com.hhh.doctor_appointment_app.poms.UpdateDoctorProfilePage;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UpdateDoctorProfilePageTest {
    private WebDriver driver;

    @Value("${origins.host}")
    private String host;

    @Autowired
    private DoctorRepository doctorRepository;

    private Doctor doctor;

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
        driver.manage().window().maximize();
        List<Doctor> doctors = doctorRepository.findAll();
        doctor = doctors.get(doctors.size() - 1);
    }

    @Test
    public void testUpdateProfileSuccess() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(host + "/auth/login");
            LoginPage loginPage = new LoginPage(driver);
            loginPage.enterEmail(doctor.getProfile().getEmail());
            loginPage.enterPassword("Hello@123");
            loginPage.clickLogin();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            Thread.sleep(2000);

            wait.until(ExpectedConditions.elementToBeClickable(By.id("updateProfileBtn")));
            UpdateDoctorProfilePage updateDoctorProfilePage = new UpdateDoctorProfilePage(driver);
            updateDoctorProfilePage.clickProfileSettings();
            Thread.sleep(2000);
            updateDoctorProfilePage.enterFirstName("NH");
            updateDoctorProfilePage.enterLastName("Hao");
            updateDoctorProfilePage.enterPhone("0827894561");
            updateDoctorProfilePage.enterAddress("Vo Van Ngan St");

            WebElement element = driver.findElement(By.id("submit-button"));

            Actions actions = new Actions(driver);

            actions.moveToElement(element).click().perform();

            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            assertEquals("Success\nSUCCESS", toast.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testUpdateProfileFail() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(host + "/auth/login");
            LoginPage loginPage = new LoginPage(driver);
            loginPage.enterEmail(doctor.getProfile().getEmail());
            loginPage.enterPassword("Hello@123");
            loginPage.clickLogin();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            Thread.sleep(2000);

            wait.until(ExpectedConditions.elementToBeClickable(By.id("updateProfileBtn")));
            UpdateDoctorProfilePage updateDoctorProfilePage = new UpdateDoctorProfilePage(driver);
            updateDoctorProfilePage.clickProfileSettings();
            Thread.sleep(2000);
            updateDoctorProfilePage.enterFirstName("NH");
            updateDoctorProfilePage.enterLastName("Hao");
            updateDoctorProfilePage.enterPhone("082@!->?94561");
            updateDoctorProfilePage.enterAddress("Vo Van Ngan St");
            WebElement element = driver.findElement(By.id("submit-button"));

            Actions actions = new Actions(driver);

            actions.moveToElement(element).click().perform();

            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            assertEquals("Error\nPhone number contains invalid characters", toast.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        Thread.sleep(2000);
        driver.quit();
    }
}
