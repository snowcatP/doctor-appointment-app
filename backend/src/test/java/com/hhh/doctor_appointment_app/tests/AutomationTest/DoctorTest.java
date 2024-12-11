package com.hhh.doctor_appointment_app.tests.AutomationTest;

import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.poms.DoctorCalendarPage;
import com.hhh.doctor_appointment_app.poms.LoginPage;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;


@SpringBootTest
public class DoctorTest {
    private WebDriver driver;
    private String email;
    private String password;

    @Value("${origins.host}")
    private String host;

    @Autowired
    private DoctorRepository doctorRepository;

    @BeforeEach
    public void setUp() {
        Doctor doctor = this.doctorRepository.findAll().get(0);
        email = doctor.getProfile().getEmail();
        password = "Hello@123";

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
    public void testDisplayAllAppointmentDetails() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            login(wait);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("fc")));
            DoctorCalendarPage doctorCalendarPage = new DoctorCalendarPage(driver);
            List<WebElement> appointments = driver.findElements(By.className("fc-event"));
            appointments.forEach(app -> {
                app.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("close")));
                try {
                    doctorCalendarPage.clickCloseBtn();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    fail();
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void testOpenRescheduleModal() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            login(wait);
            DoctorCalendarPage doctorCalendarPage = new DoctorCalendarPage(driver);
            List<WebElement> appointments = driver.findElements(By.className("fc-event"));
            appointments.forEach(app -> {
                app.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("close")));
                WebElement status = driver.findElement(By.id("status"));
                WebElement date = driver.findElement(By.id("dateBooking"));
                Date dateBooking = new Date(date.getText());
                if (    dateBooking.after(new Date()) &&
                        !Objects.equals(status.getText(), "Cancelled") &&
                        !Objects.equals(status.getText(), "Pending")) {
                    WebElement rescheduleBtn = driver.findElement(By.id("rescheduleBtn"));
                    if (rescheduleBtn == null) {
                        fail();
                    }
                }
                doctorCalendarPage.clickCloseBtn();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    fail();
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void testFilterAppointments() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            login(wait);
            driver.get(host + "/doctor/appointment");
            String[] statuses = {"ACCEPT", "PENDING", "RESCHEDULED", "CANCELLED"};
            WebElement dropdownTrigger =
                    wait.until(ExpectedConditions.elementToBeClickable(By.id("dropdown-filter")));

            for (int i = 1; i <= 4; i++) {
                dropdownTrigger.click();
                WebElement dropdownOption =
                        wait.until(ExpectedConditions.elementToBeClickable(By.id("dropdown-filter_" + i)));
                dropdownOption.click();
                Thread.sleep(500);
                List<WebElement> apps = driver.findElements(By.name("status"));
                int finalI = i;
                apps.forEach(app -> {
                    if (!app.getText().equals(statuses[finalI -1])) {
                        fail();
                    }
                });
                Thread.sleep(500);
            }
                assert true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    private void login(WebDriverWait wait) throws InterruptedException {
        driver.get(host + "/auth/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
        Thread.sleep(2000);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        Thread.sleep(1000);
        driver.quit();
    }
}
