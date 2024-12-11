package com.hhh.doctor_appointment_app.tests.Update;

import com.hhh.doctor_appointment_app.poms.LoginPage;
import com.hhh.doctor_appointment_app.poms.UpdateDoctorProfilePage;
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

@SpringBootTest
public class UpdateDoctorProfilePageTest {
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
    public void updateSuccess() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(host + "/auth/login");
            LoginPage loginPage = new LoginPage(driver);
            loginPage.enterEmail("haonguyen1231@gmail.com");
            loginPage.enterPassword("hao2112003");
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
            updateDoctorProfilePage.clickSaveChanges();

            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            assertEquals("Success\nSUCCESS", toast.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            driver.quit();
        }
    }

    @Test
    public void updateFail() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(host + "/auth/login");
            LoginPage loginPage = new LoginPage(driver);
            loginPage.enterEmail("haonguyen1231@gmail.com");
            loginPage.enterPassword("hao2112003");
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
            updateDoctorProfilePage.clickSaveChanges();

            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            assertEquals("Error\nPhone number contains invalid characters", toast.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            driver.quit();
        }
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        Thread.sleep(2000);
        driver.quit();
    }
}
