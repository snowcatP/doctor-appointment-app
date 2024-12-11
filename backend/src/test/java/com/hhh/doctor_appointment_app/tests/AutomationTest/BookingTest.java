package com.hhh.doctor_appointment_app.tests.AutomationTest;

import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.poms.BookingPage;
import com.hhh.doctor_appointment_app.poms.LoginPage;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class BookingTest {
    private WebDriver driver;
    private Patient patient;
    private Doctor doctor;

    @Value("${origins.host}")
    private String host;

    @Autowired
    private PatientRepository patientRepository;
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
        this.doctor = doctorRepository.findAll().get(0);
        this.patient = patientRepository.findAll().get(0);
    }


    @Test
    public void checkBookingSummaryWithPatient() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            String date = "2024-12-16";
            String time = "09:00 - 09:30";
            String email = patient.getProfile().getEmail();
            String password = "Hello@123";
            driver.get(host + "/auth/login");
            LoginPage loginPage = new LoginPage(driver);
            loginPage.enterEmail(email);
            loginPage.enterPassword(password);
            loginPage.clickLogin();

            JavascriptExecutor executor = (JavascriptExecutor) driver;
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            driver.get(host + "/booking");

            BookingPage bookingPage = new BookingPage(driver);
            bookingPage.enterDoctor(doctor.getProfile().getFullName());
            bookingPage.enterReason("Automation testing");

            WebElement nextStep2 = wait.until(ExpectedConditions.elementToBeClickable(By.id("nextStep2")));
            executor.executeScript("arguments[0].click();", nextStep2);

            WebElement bookingSlot = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("slot-mor-" + date + "/" + time)
            ));
            executor.executeScript("arguments[0].click();", bookingSlot);

            WebElement nextStep3 = wait.until(ExpectedConditions.elementToBeClickable(By.id("nextStep3")));
            executor.executeScript("arguments[0].click();", nextStep3);
            Thread.sleep(1000);
            assert bookingPage.getTimeBooking().equals(time);

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
