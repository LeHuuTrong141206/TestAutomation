package poly.edu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WebLoginTest {
    WebDriver driver;

    @BeforeMethod
    public void setUp(){
        //Vào trang trình duyệt trước zồi ms test case
        driver = new ChromeDriver();
        driver.get("https://the-internet.herokuapp.com/login");
    }

    @Test
    public void testLogin(){
    //user
        driver.findElement(By.id("username")).sendKeys("tomsmith");

    //Mk
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");

    //Nút
//        driver.findElement(By.cssSelector("button[type='submit']")).click(); kiểu sunmit
        driver.findElement(By.cssSelector("button.radius")).click();
    //Kiểm tra kết quả
        String expectedUrl = "https://the-internet.herokuapp.com/secure";
        String actualUrl = driver.getCurrentUrl();

        Assert.assertEquals(actualUrl, expectedUrl);
    }

    @AfterMethod
    public void tearDown() {
        // Đóng trình duyệt sau khi test xong
        if (driver != null) {
            driver.quit();
        }
    }
}
