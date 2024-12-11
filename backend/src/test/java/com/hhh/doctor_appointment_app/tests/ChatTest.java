package com.hhh.doctor_appointment_app.tests;

import com.hhh.doctor_appointment_app.entity.Conversation;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.poms.LoginPage;
import com.hhh.doctor_appointment_app.repository.ConversationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
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
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class ChatTest {
    private WebDriver driver_patient;
    private WebDriver driver_doctor;
    private Conversation conversation;
    private Doctor doctor;
    private Patient patient;
    @Autowired
    private ConversationRepository conversationRepository;

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
        driver_patient = new ChromeDriver();
        driver_doctor = new ChromeDriver();
        driver_doctor.manage().window().setPosition(new Point(800, 0));
        conversation = conversationRepository.findAll().get(0);
        doctor = conversation.getDoctor();
        patient = conversation.getPatient();
    }

    @Test
    public void testChat() {
        WebDriverWait wait_p = new WebDriverWait(driver_patient, Duration.ofSeconds(10));
        WebDriverWait wait_d = new WebDriverWait(driver_doctor, Duration.ofSeconds(10));

        try {
            login_d(doctor.getProfile().getEmail(), "Hello@123");
            login_p(patient.getProfile().getEmail(), "Hello@123");

            wait_p.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
            wait_d.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));

            WebElement chat_d = driver_doctor.findElement(By.id("chat"));
            WebElement chat_p = driver_patient.findElement(By.id("chat"));
            Thread.sleep(1000);
            chat_p.click();
            chat_d.click();

            wait_p.until(ExpectedConditions.elementToBeClickable(By.name("conversation")));
            wait_d.until(ExpectedConditions.elementToBeClickable(By.name("conversation")));

            List<WebElement> conversations_d = driver_doctor.findElements(By.name("conversation"));
            List<WebElement> conversations_p = driver_patient.findElements(By.name("conversation"));

            for (int i = 0; i < conversations_d.size(); i++) {
                WebElement conver = driver_doctor.findElement(By.id("conversation_" + i));
                if (conver.getText().equals(patient.getProfile().getFullName())) {
                    conversations_d.get(i).click();
                }
            }

            for (int i = 0; i < conversations_p.size(); i++) {
                WebElement conver = driver_patient.findElement(By.id("conversation_" + i));
                if (conver.getText().equals("Dr. " + doctor.getProfile().getFullName())) {
                    conversations_p.get(i).click();
                }
            }

            Thread.sleep(2000);
            WebElement messBox_p = wait_p.until(ExpectedConditions.elementToBeClickable(By.id("messageBox")));
            WebElement messBox_d = wait_d.until(ExpectedConditions.elementToBeClickable(By.id("messageBox")));

            String messageFromDoctor = "Send from doctor: " + new Date();
            String messageFromPatient = "Send from patient: " + new Date();

            messBox_d.sendKeys(messageFromDoctor);
            Thread.sleep(1000);
            messBox_p.sendKeys(messageFromPatient);
            Thread.sleep(1000);
            WebElement sendBtn_p = wait_p.until(ExpectedConditions.elementToBeClickable(By.id("sendBtn")));
            WebElement sendBtn_d = wait_d.until(ExpectedConditions.elementToBeClickable(By.id("sendBtn")));

            sendBtn_p.click();
            sendBtn_d.click();

            wait_p.until(ExpectedConditions.invisibilityOfElementLocated(By.className("msg-typing")));
            wait_d.until(ExpectedConditions.invisibilityOfElementLocated(By.className("msg-typing")));

            WebElement container_p = driver_patient.findElement(By.id("chat-body"));
            String containerText_p = container_p.getText();
            if (!containerText_p.contains(messageFromDoctor)) {
                fail();
            }

            WebElement container_d = driver_doctor.findElement(By.id("chat-body"));
            String containerText_d = container_d.getText();
            if (!containerText_d.contains(messageFromPatient)) {
                fail();
            }
            assert true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    private void login_p(String email, String password) {
        driver_patient.get(host + "/auth/login");
        LoginPage loginPage = new LoginPage(driver_patient);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
    }

    private void login_d(String email, String password) {
        driver_doctor.get(host + "/auth/login");
        LoginPage loginPage = new LoginPage(driver_doctor);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        Thread.sleep(1000);
        driver_doctor.quit();
        driver_patient.quit();

    }
}
