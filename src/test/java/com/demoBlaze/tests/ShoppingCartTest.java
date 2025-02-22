package com.demoBlaze.tests;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;

public class ShoppingCartTest {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    String product = "Samsung galaxy s7";  // Single test item

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    private void addItemToCart() throws InterruptedException {
        driver.get("https://demoblaze.com/");
        WebElement productElement = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(product)));
        js.executeScript("arguments[0].scrollIntoView(true);", productElement);
        productElement.click();
        Thread.sleep(2000);

        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Add to cart')]")));
        addToCartButton.click();
        Thread.sleep(2000);

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
        System.out.println("✅ Added product to cart: " + product);
    }

    private void removeItemFromCart() throws InterruptedException {
        driver.findElement(By.id("cartur")).click();
        Thread.sleep(2000);

        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Delete')]")));
        deleteButton.click();
        Thread.sleep(2000);

        List<WebElement> cartItems = driver.findElements(By.xpath("//tr[@class='success']"));
        Assert.assertTrue(cartItems.isEmpty(), "❌ Item is still in the cart after deletion!");
        System.out.println("✅ Item successfully removed from cart.");
    }

    @Test(priority = 1)
    public void testAddAndRemoveProduct() throws InterruptedException {
        addItemToCart();

        driver.findElement(By.id("cartur")).click();
        Thread.sleep(2000);

        WebElement cartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(),'" + product + "')]")));
        Assert.assertTrue(cartItem.isDisplayed(), "❌ Item not added to cart!");
        System.out.println("✅ Verified item in cart: " + product);

        removeItemFromCart();
    }

    @AfterClass
    public void teardown() throws InterruptedException {
        driver.get("https://demoblaze.com/cart.html");
        Thread.sleep(2000);

        List<WebElement> deleteButtons = driver.findElements(By.xpath("//a[contains(text(),'Delete')]"));
        for (WebElement deleteButton : deleteButtons) {
            deleteButton.click();
            Thread.sleep(1000);
        }

        driver.quit();
        System.out.println("🔴 Cart cleaned and browser closed.");
    }
}

