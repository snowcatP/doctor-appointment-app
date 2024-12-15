package com.hhh.doctor_appointment_app.tests.AutomationTest;

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
        options.addArguments("--no-sandbox"); // Optional: Improves security for CI environments
        options.addArguments("--disable-dev-shm-usage");
//        driver = new ChromeDriver(options);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void testLoginSuccess() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            login("admin@gmail.com", "Hello@123");

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
    public void testLoginFail() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            login("admin@gmail.com", "Hello@12345");

            WebElement errorMessageElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("errorMessage"))
            );
            assertEquals("Wrong email or password.", errorMessageElement.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void logout() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            login("admin@gmail.com", "Hello@123");
            Thread.sleep(4000);
            WebElement menuDropdown =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menu-dropdown")));
            menuDropdown.click();
            WebElement logoutButton =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout")));

            logoutButton.click();
            WebElement toast =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            assertEquals("Success\nLogout Success", toast.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    private void login(String email, String password) {
        driver.get(host + "/auth/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        Thread.sleep(1000);
        driver.quit();
    }

}
