package com.demoBlaze.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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

public class LogoutTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private void login(String username, String password) {
        driver.get("https://demoblaze.com/");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login2"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername"))).sendKeys(username);
        driver.findElement(By.id("loginpassword")).sendKeys(password);
        driver.findElement(By.xpath("//button[contains(text(),'Log in')]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2"))); // Ensure login was successful
    }

    private void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout2"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login2"))); // Ensure logout was successful
    }

    @Test(priority = 1)
    public void testSuccessfulLogout() {
        login("Mariam4", "romaislam29");
        logout();

        // Verify logout by ensuring the login button is visible
        Assert.assertTrue(driver.findElement(By.id("login2")).isDisplayed(), "❌ Logout failed, session still active!");
        System.out.println("✅ Logout successful.");
    }

    @Test(priority = 2)
    public void testBrowserBackButtonAfterLogout() throws InterruptedException {
        login("Mariam4", "romaislam29");
        logout();

        // Use the browser back button
        driver.navigate().back();
        Thread.sleep(2000);

        // Verify user is still logged out
        Assert.assertTrue(driver.findElement(By.id("login2")).isDisplayed(), "❌ User re-logged in after pressing back button!");
        System.out.println("✅ User remains logged out after using the browser back button.");
    }


    @Test(priority = 3)
    public void testCartIsClearedAfterLogout() throws InterruptedException {
        login("Mariam4", "romaislam29");

        // Add an item to the cart
        driver.get("https://demoblaze.com/");
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Samsung galaxy s7"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Add to cart')]"))).click();
        Thread.sleep(2000);
        driver.switchTo().alert().accept(); // Accept alert

        logout();

        // Log back in and check if cart is empty
        login("Mariam4", "romaislam29");
        driver.findElement(By.id("cartur")).click();
        Thread.sleep(2000);

        Assert.assertTrue(driver.findElements(By.xpath("//tr[@class='success']")).isEmpty(), "❌ Cart still has items after logout!");
        System.out.println("✅ Cart is cleared after logout.");
    }



    @Test(priority = 5)
    public void testMultipleLoginsInvalidatedAfterLogout() {
        login("Mariam4", "romaislam29");

        // Open a new browser window (simulated by opening a new tab)
        ((ChromeDriver) driver).executeScript("window.open()");
        String originalTab = driver.getWindowHandle();
        for (String tab : driver.getWindowHandles()) {
            if (!tab.equals(originalTab)) {
                driver.switchTo().window(tab);
                break;
            }
        }

        // Try accessing the user dashboard
        driver.get("https://demoblaze.com/");
        logout();

        // Switch back to original tab and verify session is terminated
        driver.switchTo().window(originalTab);
        driver.navigate().refresh();

        Assert.assertTrue(driver.findElement(By.id("login2")).isDisplayed(), "❌ Multiple sessions still active after logout!");
        System.out.println("✅ All sessions invalidated after logout.");
    }

    @Test(priority = 6)
    public void testLogoutWithoutActiveSession() {
        driver.get("https://demoblaze.com/");

        // Try clicking logout button without logging in
        try {
            driver.findElement(By.id("logout2")).click();
            Assert.fail("❌ Logout button should not be clickable without an active session!");
        } catch (NoSuchElementException e) {
            System.out.println("✅ Logout button is not available without an active session.");
        }
    }

    @AfterClass
    public void teardown() {
        driver.quit();
        System.out.println("🔴 Test execution completed, browser closed.");
    }
}
