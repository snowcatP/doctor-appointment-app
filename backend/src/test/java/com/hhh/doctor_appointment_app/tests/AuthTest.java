package com.hhh.doctor_appointment_app.tests;

import com.hhh.doctor_appointment_app.BaseSetup;
import com.hhh.doctor_appointment_app.poms.LoginPage;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;

@SpringBootTest
public class AuthTest extends BaseSetup {
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
    public void loginSuccess() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(host + "/auth/login");
            LoginPage loginPage = new LoginPage(driver);
            loginPage.enterEmail("admin@gmail.com");
            loginPage.enterPassword("Hello@123");
            loginPage.clickLogin();

            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            assertEquals("Success\nLogin successfully", toast.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        } finally {
            driver.quit();
        }
    }

    @Test
    public void loginFail() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(host + "/auth/login");
            LoginPage loginPage = new LoginPage(driver);
            loginPage.enterEmail("admin@gmail.com");
            loginPage.enterPassword("Hello@12345");
            loginPage.clickLogin();

            WebElement errorMessageElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("errorMessage"))
            );
            assertEquals("Wrong email or password.", errorMessageElement.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        Thread.sleep(1000);
        driver.quit();
    }

}
