package poly.edu;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public class Bai2_lab6{
    @Test
    public void  testLogin() throws IOException{
        WebDriver driver = new ChromeDriver();
        String testResult = "";

        try {
            driver.get("https://the-internet.herokuapp.com/login");
            driver.findElement(By.id("username")).sendKeys("tomsmith");
            driver.findElement(By.name("password")).sendKeys("SuperSecretPassword!");
            driver.findElement(By.className("radius")).click();
            WebElement message = driver.findElement(By.cssSelector("#flash.success"));

            if (message.isDisplayed()) {
                testResult = "PASS";
                System.out.println("Đăng nhập thành công!");
            } else {
                testResult = "FAIL";
                System.out.println("Đăng nhập không thành công!");
            }

        }catch (Exception e){
            testResult = "ERROR: " + e.getMessage();

        }finally {
//            Xuất ra excel
            saveToExcel("Test Login theo 4 locating", testResult);
            driver.quit();
        }
    }

    public void saveToExcel(String caseName, String result) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Lab6_Result");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Tên Test Case");
        header.createCell(1).setCellValue("Kết Quả");

        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(caseName);
        dataRow.createCell(1).setCellValue(result);

        try (FileOutputStream fileOut = new FileOutputStream("ExecelTestCase_Lab6-bai2.xlsx")) {
            workbook.write(fileOut);
        }
        workbook.close();
        System.out.println("Đã xuất bảng Excel");
    }
}
