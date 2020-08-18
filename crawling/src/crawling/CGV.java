package crawling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CGV {
	
	private WebDriver driver;
	private WebElement el;
	
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:/chromedriver.exe";
	
	public static void main(String[] args) {
		CGV cgv = new CGV();
		//드라이버 설정
		System.setProperty(WEB_DRIVER_ID,WEB_DRIVER_PATH);
		
		//webDriver
		//옵션 줄때
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("headless");
//		cgv.driver=new ChromeDriver(options);
		//옵션이 없을 떄
		
		cgv.driver=new ChromeDriver();
		
		WebElement el;
		WebElement el2;
		
		String url = "http://www.cgv.co.kr/movies/pre-movies.aspx";
		
		try {
			cgv.driver.get(url);
			el=cgv.driver.findElement(By.className("btn-more-fontbold"));
			el.click();
			Thread.sleep(2000);
//	calss:	sect-movie-chart
			el2=cgv.driver.findElement(By.className("sect-movie-chart"));
	
			//class :title(반복)
			System.out.println("==============================================");
			for(WebElement data : el2.findElements(By.className("title"))) {
				System.out.println(data.getText());
				
			};
			System.out.println("==============================================");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			cgv.driver.close();
			cgv.driver.quit();
		}
	
	}
}
