package com.modulink.security;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class isAccessibleModuloTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseUrl;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Esegui in modalità headless per CI/CD
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testUserLoginAndModulesAccessibility() {
        driver.get(baseUrl + "/login");

        WebElement usernameInput = driver.findElement(By.id("username"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        usernameInput.sendKeys("admin@techsolutions.com");
        passwordInput.sendKeys("password");
        loginButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        assertThat(driver.getCurrentUrl()).contains("/dashboard");

        driver.get(baseUrl + "/dashboard/gtm/");
        wait.until(ExpectedConditions.urlContains("/dashboard/gtm/"));
        assertThat(driver.getCurrentUrl()).contains("/dashboard/gtm/");

        driver.get(baseUrl + "/dashboard/gdm/");
        wait.until(ExpectedConditions.urlContains("/dashboard/gdm/"));
        assertThat(driver.getCurrentUrl()).contains("/dashboard/gdm/");

        driver.get(baseUrl + "/dashboard/calendar");
        wait.until(ExpectedConditions.urlContains("/dashboard/calendar"));
        assertThat(driver.getCurrentUrl()).contains("/dashboard/calendar");

        driver.get(baseUrl + "/dashboard/admin");
        wait.until(ExpectedConditions.urlContains("/dashboard"));
        assertThat(driver.getCurrentUrl()).contains("/dashboard");

        driver.get(baseUrl + "/dashboard/support");
        wait.until(ExpectedConditions.urlContains("/dashboard"));
        assertThat(driver.getCurrentUrl()).contains("/dashboard");

        driver.get(baseUrl + "/dashboard/news");
        wait.until(ExpectedConditions.urlContains("/dashboard"));
        assertThat(driver.getCurrentUrl()).contains("/dashboard");

        System.out.println("✅ Test accessibilità moduli completato con successo per admin@techsolutions.com");
    }
}
