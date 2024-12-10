package com.hhh.doctor_appointment_app.tests.Update;

import com.hhh.doctor_appointment_app.poms.LoginPage;
import com.hhh.doctor_appointment_app.poms.UpdateDoctorProfilePage;
import com.hhh.doctor_appointment_app.poms.UpdatePasswordPage;
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
public class UpdatePasswordOfPatientTest {
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
            loginPage.enterEmail("hoanghao2112003@gmail.com");
            loginPage.enterPassword("hao2112003");
            loginPage.clickLogin();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            Thread.sleep(2000);
            driver.get(host + "/patient/dashboard");
            wait.until(ExpectedConditions.elementToBeClickable(By.id("changePasswordBtn")));
            UpdatePasswordPage updatePasswordPage = new UpdatePasswordPage(driver);
            updatePasswordPage.clickChangePassword();
            Thread.sleep(2000);
            updatePasswordPage.enterOldPassword("hao2112003");
            updatePasswordPage.enterNewPassword("NguyenHao2112003");
            updatePasswordPage.enterConfirmPassword("NguyenHao2112003");
            updatePasswordPage.clickSaveChanges();

            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            assertEquals("Success\nChange password successfully", toast.getText());
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
            loginPage.enterEmail("hoanghao2112003@gmail.com");
            loginPage.enterPassword("NguyenHao2112003");
            loginPage.clickLogin();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            Thread.sleep(2000);
            driver.get(host + "/patient/dashboard");
            wait.until(ExpectedConditions.elementToBeClickable(By.id("changePasswordBtn")));
            UpdatePasswordPage updatePasswordPage = new UpdatePasswordPage(driver);
            updatePasswordPage.clickChangePassword();
            Thread.sleep(2000);
            updatePasswordPage.enterOldPassword("abcxyz123");
            updatePasswordPage.enterNewPassword("hao2112003");
            updatePasswordPage.enterConfirmPassword("hao2112003");
            updatePasswordPage.clickSaveChanges();

            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            assertEquals("Error\nOld password is incorrect", toast.getText());
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
