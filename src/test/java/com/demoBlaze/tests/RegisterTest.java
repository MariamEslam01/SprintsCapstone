package com.demoBlaze.tests;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RegisterTest {
    WebDriver driver;
    WebDriverWait wait;
    Random random = new Random();

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void openSignupModal() {
        driver.get("https://demoblaze.com/");
        driver.findElement(By.id("signin2")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username")));
    }

    private String generateUniqueUsername() {
        return "User" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + random.nextInt(100);
    }

    private String generateRandomPassword() {
        return "Pass" + random.nextInt(10000) + "!";
    }

    private void fillSignupForm(String username, String password) {
        driver.findElement(By.id("sign-username")).clear();
        driver.findElement(By.id("sign-username")).sendKeys(username);
        driver.findElement(By.id("sign-password")).clear();
        driver.findElement(By.id("sign-password")).sendKeys(password);
    }

    private void submitSignup() {
        driver.findElement(By.xpath("//button[contains(text(),'Sign up')]")).click();
    }

    private String handleAlert() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertMessage = alert.getText();
        alert.accept();
        return alertMessage;
    }

    @Test(priority = 1)
    public void testValidSignup() {
        String username = generateUniqueUsername();
        String password = generateRandomPassword();

        openSignupModal();
        fillSignupForm(username, password);
        submitSignup();
        Assert.assertEquals(handleAlert(), "Sign up successful.");
    }

    @Test(priority = 2)
    public void testEmptyName() {
        openSignupModal();
        fillSignupForm("", generateRandomPassword());
        submitSignup();
        Assert.assertEquals(handleAlert(), "Please fill out Username and Password.");
    }

    @Test(priority = 3)
    public void testSingleCharacterName() {
        String username = "U" + random.nextInt(9);
        openSignupModal();
        fillSignupForm(username, generateRandomPassword());
        submitSignup();
        Assert.assertEquals(handleAlert(), "Sign up successful.");
    }

    @Test(priority = 4)
    public void testNameWithNumbers() {
        String username = "User" + random.nextInt(9999);
        openSignupModal();
        fillSignupForm(username, generateRandomPassword());
        submitSignup();
        Assert.assertEquals(handleAlert(), "Sign up successful.");
    }

    @Test(priority = 5)
    public void testNameWithSpecialCharacters() {
        String username = "User" + random.nextInt(9999) + "-test";
        openSignupModal();
        fillSignupForm(username, generateRandomPassword());
        submitSignup();
        Assert.assertEquals(handleAlert(), "Sign up successful.");
    }

    @Test(priority = 6)
    public void testLongName() {
        String username = "LongUser_" + random.nextInt(9999) + "_".repeat(15);
        openSignupModal();
        fillSignupForm(username, generateRandomPassword());
        submitSignup();
        Assert.assertEquals(handleAlert(), "Sign up successful.");
    }

    @Test(priority = 7)
    public void testTrimmedSpacesInName() {
        String username = "  TrimUser" + random.nextInt(9999) + "  ";
        openSignupModal();
        fillSignupForm(username, generateRandomPassword());
        submitSignup();
        Assert.assertEquals(handleAlert(), "Sign up successful.");
    }

    @Test(priority = 8)
    public void testAutoLoginAfterSignup() {
        String username = generateUniqueUsername();
        String password = generateRandomPassword();

        openSignupModal();
        fillSignupForm(username, password);
        submitSignup();
        handleAlert();

        driver.findElement(By.id("login2")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername"))).sendKeys(username);
        driver.findElement(By.id("loginpassword")).sendKeys(password);
        driver.findElement(By.xpath("//button[contains(text(),'Log in')]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2")));
    }

    @Test(priority = 9)
    public void testLogoutAndLoginPersistence() {
        String username = generateUniqueUsername();
        String password = generateRandomPassword();

        openSignupModal();
        fillSignupForm(username, password);
        submitSignup();
        handleAlert();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout2"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login2")));

        driver.findElement(By.id("login2")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername"))).sendKeys(username);
        driver.findElement(By.id("loginpassword")).sendKeys(password);
        driver.findElement(By.xpath("//button[contains(text(),'Log in')]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2")));
    }

    @AfterClass
    public void teardown() {
        driver.quit();
    }
}
