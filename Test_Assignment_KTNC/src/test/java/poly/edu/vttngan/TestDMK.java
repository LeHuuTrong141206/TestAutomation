package poly.edu.vttngan;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class TestDMK {

    WebDriver driver;
    WebDriverWait wait;

    String email = "minhanh@gmail.com";
    String currentPassword = "pass002";

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // ===================== LOGIN =====================
    @Test(priority = 1)
    public void testLogin() throws InterruptedException {
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=login");
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(currentPassword);
        driver.findElement(By.xpath("//button[text()='Đăng Nhập']")).click();
        Thread.sleep(2000);

        Assert.assertTrue(
                driver.getPageSource().toLowerCase().contains("user"),
                "Đăng nhập thất bại!"
        );

        System.out.println("Đăng nhập thành công");
    }

    // HÀM ĐỔI MẬT KHẨU
    public void doiMatKhau(String oldPass, String newPass, String confirmPass) {
        driver.get("http://localhost:8080/cinevo-web/user/change-password");

        driver.findElement(By.name("oldPassword")).clear();
        driver.findElement(By.name("oldPassword")).sendKeys(oldPass);

        driver.findElement(By.name("newPassword")).clear();
        driver.findElement(By.name("newPassword")).sendKeys(newPass);

        driver.findElement(By.name("confirmPassword")).clear();
        driver.findElement(By.name("confirmPassword")).sendKeys(confirmPass);

        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    // DMK_01
    @Test(priority = 2, dependsOnMethods = "testLogin")
    public void DMK_01() throws InterruptedException {
        String newPass = "123456789";

        doiMatKhau(currentPassword, newPass, newPass);
        Thread.sleep(1000);

        String page = driver.getPageSource().toLowerCase();

        Assert.assertTrue(
                page.contains("thành công")
                        || page.contains("đổi mật khẩu thành công")
                        || page.contains("đổi thành công")
                        || page.contains("cập nhật thành công"),
                "DMK_01: Không thấy thông báo thành công"
        );

        currentPassword = newPass;
        System.out.println("DMK_01: Đổi mật khẩu thành công");
    }

    //DMK_02
    @Test(priority = 3, dependsOnMethods = "DMK_01")
    public void DMK_02() throws InterruptedException {
        doiMatKhau("12345687", "012345678", "012345678");
        Thread.sleep(1000);

        try {
            WebElement alert = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector(".alert.alert-danger")));

            String text = alert.getText().toLowerCase();

            Assert.assertTrue(
                    text.contains("sai")
                            || text.contains("không đúng")
                            || text.contains("mật khẩu cũ"),
                    "DMK_02: Không báo lỗi mật khẩu cũ sai"
            );

        } catch (Exception e) {
            String page = driver.getPageSource().toLowerCase();

            Assert.assertTrue(
                    page.contains("sai")
                            || page.contains("không đúng")
                            || page.contains("mật khẩu cũ"),
                    "DMK_02: Không báo lỗi mật khẩu cũ sai"
            );
        }

        System.out.println("DMK_02: Mật khẩu cũ không đúng");
    }

    //DMK_03
    @Test(priority = 4, dependsOnMethods = "DMK_01")
    public void DMK_03() throws InterruptedException {
        String longPass = "";
        for (int i = 0; i < 100; i++) {
            longPass += "123";
        }
        doiMatKhau(currentPassword, longPass, longPass);
        Thread.sleep(1000);

        try {
            WebElement alert = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector(".alert.alert-danger")));

            String text = alert.getText().toLowerCase();

            Assert.assertTrue(
                    text.contains("50")
                            || text.contains("độ dài")
                            || text.contains("tối đa"),
                    "DMK_03: Không báo lỗi mật khẩu quá dài"
            );

        } catch (Exception e) {
            String page = driver.getPageSource().toLowerCase();

            Assert.assertTrue(
                    page.contains("50")
                            || page.contains("độ dài")
                            || page.contains("tối đa"),
                    "DMK_03: Không báo lỗi mật khẩu quá dài"
            );
        }

        System.out.println("DMK_03: Mật khẩu quá dài");
    }

    //DMK_04
    @Test(priority = 5, dependsOnMethods = "DMK_01")
    public void DMK_04() throws InterruptedException {
        String newPass = "@123456789@";

        doiMatKhau(currentPassword, newPass, newPass);
        Thread.sleep(1000);

        String page = driver.getPageSource().toLowerCase();

        Assert.assertTrue(
                page.contains("thành công")
                        || page.contains("đổi mật khẩu thành công")
                        || page.contains("đổi thành công")
                        || page.contains("cập nhật thành công"),
                "DMK_04: Không thấy thông báo thành công"
        );

        currentPassword = newPass;
        System.out.println("DMK_04: Đổi mật khẩu có ký tự đặc biệt");
    }

    // ===================== DMK_05 =====================
    @Test(priority = 6, dependsOnMethods = "DMK_01")
    public void DMK_05() throws InterruptedException {
        doiMatKhau(currentPassword, "123456789", "012345678");
        Thread.sleep(1000);

        try {
            WebElement alert = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector(".alert.alert-danger")));

            String text = alert.getText().toLowerCase();

            Assert.assertTrue(
                    text.contains("không khớp")
                            || text.contains("xác nhận")
                            || text.contains("nhập lại"),
                    "DMK_05: Không báo lỗi xác nhận không khớp"
            );

        } catch (Exception e) {
            String page = driver.getPageSource().toLowerCase();

            Assert.assertTrue(
                    page.contains("không khớp")
                            || page.contains("xác nhận")
                            || page.contains("nhập lại"),
                    "DMK_05: Không báo lỗi xác nhận không khớp"
            );
        }

        System.out.println("DMK_05: Xác nhận mật khẩu không khớp");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}