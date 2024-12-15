package com.hhh.doctor_appointment_app.tests.AutomationTest.Seach;

import com.hhh.doctor_appointment_app.BaseSetup;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.poms.SeachAppointmentByCode;
import com.hhh.doctor_appointment_app.poms.SearchDoctorPage;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SearchAppointmentCodeTest extends BaseSetup {
    private WebDriver driver;

    @Value("${origins.host}")
    private String host;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Appointment appointment;

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
        List<Appointment> apps = appointmentRepository.findAll();
        appointment = apps.get(apps.size() - 1);
    }

    @Test
    public void testSearchAppointmentSuccess() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(host);
            SeachAppointmentByCode seachAppointmentByCode = new SeachAppointmentByCode(driver);
            seachAppointmentByCode.enterAppointmentCode(appointment.getReferenceCode());

            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            assertEquals("Success", toast.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSearchAppointmentFail() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(host);
            SeachAppointmentByCode seachAppointmentByCode = new SeachAppointmentByCode(driver);
            seachAppointmentByCode.enterAppointmentCode("IN_VALID");

            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            assertEquals("Failed\nAppointment Not Found", toast.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        Thread.sleep(3000);
        driver.quit();
    }
}
