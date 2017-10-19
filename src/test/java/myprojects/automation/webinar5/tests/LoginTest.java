package myprojects.automation.webinar5.tests;


import myprojects.automation.webinar5.BaseTest;
import myprojects.automation.webinar5.utils.CustomReporter;
import myprojects.automation.webinar5.utils.Properties;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class LoginTest extends BaseTest {

    protected Random randomProduct;
    private String countProduct;
    private String productName;
    private String productPrice;
    private String productPriceOld;
    private String productNameOld;
    private SoftAssert softAssert;

    private String firstName;
    private String lastName;
    private String yourEmail;
    private String yourAddres;
    private String yourIndex;
    private String yourCity;


/*
    @Test//(dataProvider = "getLoginData")
    public void openMainPage() throws InterruptedException, IOException {
        CustomReporter.logAction("User is showed the web-site");
        driver.navigate().to(Properties.getBaseUrl());
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".logo.img-responsive")));
        Thread.sleep(5000);
        File screenshot = ((TakesScreenshot) driver).
                getScreenshotAs(OutputType.FILE);
        String path = "./target/screenshots/" + screenshot.getName();
        FileUtils.copyFile(screenshot, new File(path));
        CustomReporter.logAction("Screenshot is successful");
    }
    */
    @Test
    public void orderingInShop() throws InterruptedException {
        softAssert = new SoftAssert();
        firstName = "Vladimir";
        lastName = "Krutin";
        yourEmail = String.valueOf(System.currentTimeMillis()).substring(7,9) + "@krut.com";
        yourAddres = "Fredy str.";
        yourIndex = "69001";
        yourCity = "Dnepr";

        CustomReporter.logAction("User is showed the web-site");
        driver.navigate().to(Properties.getBaseUrl());
        randomProduct = new Random();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".logo.img-responsive")));
        driver.findElement(By.cssSelector(".all-product-link")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".logo.img-responsive")));
        List<WebElement> elements = driver.findElements(By.cssSelector(".product-description"));
        elements.get(randomProduct.nextInt(7)).click();

        Thread.sleep(5000);
        driver.findElement(By.cssSelector("button[data-button-action='add-to-cart']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.btn.btn-primary")));
        productPriceOld = driver.findElement(By.
                    cssSelector("#main > div.row > div:nth-child(2) > div.product-prices > div.product-price.h5 > div > span")).getText().substring(0,3);
        productNameOld = driver.findElement(By.
                cssSelector("#main > div.row > div:nth-child(2) > h1")).getText().substring(0,5);
        driver.findElement(By.cssSelector("a.btn.btn-primary")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.label.js-subtotal")));
        countProduct = driver.findElement(By.cssSelector("span.label.js-subtotal")).getText();
        List<WebElement> elementsName = driver.findElements(By.cssSelector("a.label"));
        productName = elementsName.get(0).getText().toUpperCase().substring(0,5);
        softAssert.assertEquals(productNameOld, productName);
        CustomReporter.logAction("Name is true");

        productPrice =  driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[1]/div[1]/div[2]/ul/li/div/div[2]/div[2]/span")).getText();
        System.out.println(countProduct.startsWith("1"));
        softAssert.assertTrue(countProduct.startsWith("1"),"1");
        CustomReporter.logAction("You buy 1");

        System.out.println("2. Old name: " +productNameOld + "-"+(driver.findElement(By.cssSelector(".product-line-info a[class='label']")).getText()).toUpperCase());
        CustomReporter.logAction("Name is true");
        List<WebElement> elementsPriceNew = driver.findElements(By.cssSelector("div.product-line-info span[class='value']"));

        softAssert.assertTrue(productPriceOld.contains(elementsPriceNew.get(0).getText().substring(0,2)));
        CustomReporter.logAction("Prise is right");
        System.out.println("3." + productPriceOld + productPrice.contains(elementsPriceNew.get(0).getText().substring(0,2)));
        driver.findElement(By.cssSelector("a.btn.btn-primary")).click();

        List<WebElement> elementsFirstName = driver.findElements(By.cssSelector("span input[name='id_gender']"));
        elementsFirstName.get(0).click();
        driver.findElement(By.cssSelector("input[name='firstname']")).sendKeys(firstName);
        driver.findElement(By.cssSelector("input[name='lastname']")).sendKeys(lastName);
        driver.findElement(By.cssSelector("input[name='email']")).sendKeys(yourEmail);
        driver.findElement(By.cssSelector("#customer-form > footer > button")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#delivery-address > div > footer > button")));
        driver.findElement(By.cssSelector("input[name='address1']")).sendKeys(yourAddres);
        driver.findElement(By.cssSelector("input[name='postcode']")).sendKeys(yourIndex);
        driver.findElement(By.cssSelector("input[name='city']")).sendKeys(yourCity);
        driver.findElement(By.cssSelector("#delivery-address > div > footer > button")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#js-delivery > button")));
        driver.findElement(By.cssSelector("#js-delivery > button")).click();

        driver.findElement(By.xpath(".//span[text()='Оплата чеком']")).click();
        driver.findElement(By.xpath("//*[@id='conditions_to_approve[terms-and-conditions]']")).click();
        driver.findElement(By.cssSelector("button.btn.btn-primary.center-block")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("i.material-icons.done")));
        System.out.println("4. " + driver.findElement(By.xpath("//*[@id=\"content-hook_order_confirmation\"]/div/div/div/h3")).
                getText().contains("ВАШ ЗАКАЗ ПОДТВЕРЖДЁН") + driver.findElement(By.cssSelector("i.material-icons.done")).
                getText() );
        CustomReporter.logAction("Label is true");
        softAssert.assertTrue(driver.findElement(By.xpath("//*[@id=\"content-hook_order_confirmation\"]/div/div/div/h3")).
                        getText().contains("ВАШ ЗАКАЗ ПОДТВЕРЖДЁН"));
        System.out.println("5. " + driver.findElement(By.cssSelector(".col-xs-2")).
                getText().contains("1") + driver.findElement(By.cssSelector(".col-xs-2")).
                getText());
        softAssert.assertTrue(driver.findElement(By.cssSelector(".col-xs-2")).
                getText().contains("1"));
        System.out.println("6. " + driver.findElement(By
                .cssSelector("#order-items > div > div > div.col-sm-4.col-xs-9.details > span"))
                .getText().contains(productNameOld) + driver.findElement(By
                .cssSelector("#order-items > div > div > div.col-sm-4.col-xs-9.details > span"))
                .getText());
        CustomReporter.logAction("Col is true");
        /*softAssert.assertTrue(driver.findElement(By
                .cssSelector("#order-items > div > div > div.col-sm-4.col-xs-9.details > span"))
                .getText().contains(productNameOld));*/
        System.out.println(productNameOld.toUpperCase());
        softAssert.assertEquals(driver.findElement(By
                .xpath("//*[@id=\"order-items\"]/div/div/div[2]/span"))
                .getText().substring(0,5).toUpperCase(), productName);
        CustomReporter.logAction("Finish name is true");
        softAssert.assertAll();
    }

}
