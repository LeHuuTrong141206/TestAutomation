package poly.edu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.time.Duration;

public class TestDK {
    WebDriver driver;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    // ================= TEST 1: ĐĂNG KÝ VỚI TÀI KHOẢN HỢP LỆ =================
    @Test(groups = {"auth", "register"}, priority = 1, enabled = false)
    public void testRegister_1() throws InterruptedException{
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=register");

        driver.findElement(By.xpath("/html/body/div/div/div/form/div[1]/input")).sendKeys("Lê Hữu Trọng");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[2]/input")).sendKeys("lehuutrong@gmail.com");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[3]/input")).sendKeys("zZ0987939607Zz");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[4]/input")).sendKeys("zZ0987939607Zz");
        driver.findElement(By.xpath("/html/body/div/div/div/form/button")).click();

        Thread.sleep(2000);

        String urlDangKy = driver.getCurrentUrl();
        Assert.assertTrue(urlDangKy.contains("/cinevo/user"), "LỖI: Đăng Ký thất bại");

        Thread.sleep(2000);

        WebElement thongbao = driver.findElement(By.xpath("//*[contains(., 'Đăng ký thành công')]"));
        Assert.assertTrue(thongbao.isDisplayed(), "Lỗi: Không có thông báo hiển lên");
        Thread.sleep(2000);
    }

    // ================= TEST 2: ĐĂNG KÝ VỚI HỌ VÀ TÊN CÓ KÝ TỰ ĐẶC BIÊT =================
    // Lỗi đầu tiên tạo thành công với họ và tên có ký tự đặc biệt
    @Test(groups = {"auth", "register_fail"}, priority = 2, enabled = false)
    public void testRegisterFail_1() throws InterruptedException{
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=register");

        driver.findElement(By.xpath("/html/body/div/div/div/form/div[1]/input")).sendKeys("Trường @n");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[2]/input")).sendKeys("an@gmail.com");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[3]/input")).sendKeys("123456789");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[4]/input")).sendKeys("123456789");
        driver.findElement(By.xpath("/html/body/div/div/div/form/button")).click();

        Thread.sleep(2000);

        String urlDangKy = driver.getCurrentUrl();
        Assert.assertTrue(urlDangKy.contains("/auth/register"), "LỖI: Đăng Ký sai");

        Thread.sleep(2000);

        WebElement thongbao = driver.findElement(By.xpath("//*[contains(., 'Sai họ và tên')]"));
        Assert.assertTrue(thongbao.isDisplayed(), "Lỗi: Không có thông báo hiển lên");
        Thread.sleep(2000);
    }

    // ================= TEST 3: ĐĂNG KÝ VỚI EMAIL CÓ 2 KÝ TỰ @ =================
    // Vẫn ở trang đăng ký và không tạo thành công nhưng không có thông báo liên quan đến lỗi email
    @Test(groups = {"auth", "register_fail"}, priority = 3, enabled = false)
    public void testRegisterFail_2() throws InterruptedException{
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=register");

        driver.findElement(By.xpath("/html/body/div/div/div/form/div[1]/input")).sendKeys("Trường An");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[2]/input")).sendKeys("@n@gmail.com");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[3]/input")).sendKeys("123456789");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[4]/input")).sendKeys("123456789");
        driver.findElement(By.xpath("/html/body/div/div/div/form/button")).click();

        Thread.sleep(2000);

        String urlDangKy = driver.getCurrentUrl();
        Assert.assertTrue(urlDangKy.contains("/auth/register"), "LỖI: Đăng Ký sai");

        Thread.sleep(2000);

        WebElement thongbao = driver.findElement(By.xpath("//*[contains(., 'Sai Email')]"));
        Assert.assertTrue(thongbao.isDisplayed(), "Lỗi: Không có thông báo hiển lên");
        Thread.sleep(2000);
    }

    // ================= TEST 4: ĐĂNG KÝ VỚI MẬT KHẨU DƯỚI 6 KÝ TỰ =================
    // Test vẫn pass nhưng lại mất css
    @Test(groups = {"auth", "register_fail"}, priority = 3, enabled = false)
    public void testRegisterFail_3() throws InterruptedException{
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=register");

        driver.findElement(By.xpath("/html/body/div/div/div/form/div[1]/input")).sendKeys("Trường An");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[2]/input")).sendKeys("an1@gmail.com");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[3]/input")).sendKeys("113");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[4]/input")).sendKeys("113");
        driver.findElement(By.xpath("/html/body/div/div/div/form/button")).click();

        Thread.sleep(2000);

        String urlDangKy = driver.getCurrentUrl();
        Assert.assertTrue(urlDangKy.contains("/auth/register"), "LỖI: Đăng Ký sai");

        Thread.sleep(2000);

        WebElement thongbao = driver.findElement(By.xpath("//*[contains(., 'ít nhất 6 ký tự')]"));
        Assert.assertTrue(thongbao.isDisplayed(), "Lỗi: Không có thông báo hiển lên");
        Thread.sleep(2000);
    }

    // ================= TEST 5: ĐĂNG KÝ VỚI MẬT KHẨU CÓ CHỮ TRONG ĐÓ =================
    // pass ok
    @Test(groups = {"auth", "register"}, priority = 4, enabled = false)
    public void testRegister_2() throws InterruptedException{
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=register");

        driver.findElement(By.xpath("/html/body/div/div/div/form/div[1]/input")).sendKeys("Trường An");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[2]/input")).sendKeys("an1@gmail.com");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[3]/input")).sendKeys("zZ123456");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[4]/input")).sendKeys("zZ123456");
        driver.findElement(By.xpath("/html/body/div/div/div/form/button")).click();

        Thread.sleep(2000);

        String urlDangKy = driver.getCurrentUrl();
        Assert.assertTrue(urlDangKy.contains("/cinevo/user"), "LỖI: Đăng Ký sai");

        Thread.sleep(2000);

        WebElement thongbao = driver.findElement(By.xpath("//*[contains(., 'Đăng ký thành công')]"));
        Assert.assertTrue(thongbao.isDisplayed(), "Lỗi: Không có thông báo hiển lên");
        Thread.sleep(2000);
    }

    // ================= TEST 6: ĐĂNG KÝ NHƯNG MẬT KHẨU NHẬP LẠI KHÔNG TRÙNG VỚI MẬT KHẨU =================
    // pass nhưng lại mất css
    @Test(groups = {"auth", "register_fail"}, priority = 5, enabled = false)
    public void testRegisterFail_5() throws InterruptedException{
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=register");

        driver.findElement(By.xpath("/html/body/div/div/div/form/div[1]/input")).sendKeys("Trường An");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[2]/input")).sendKeys("an1@gmail.com");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[3]/input")).sendKeys("zZ123456");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[4]/input")).sendKeys("zZ123456789");
        driver.findElement(By.xpath("/html/body/div/div/div/form/button")).click();

        Thread.sleep(2000);

        String urlDangKy = driver.getCurrentUrl();
        Assert.assertTrue(urlDangKy.contains("/auth/register"), "LỖI: Đăng Ký sai");

        Thread.sleep(2000);

        WebElement thongbao = driver.findElement(By.xpath("//*[contains(., 'không khớp')]"));
        Assert.assertTrue(thongbao.isDisplayed(), "Lỗi: Không có thông báo hiển lên");
        Thread.sleep(2000);
    }


    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        System.out.println("====== TEST XONG ======");
    }
}
