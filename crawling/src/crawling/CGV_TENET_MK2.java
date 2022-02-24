package crawling;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CGV_TENET_MK2{

	// WebDriver
	private WebDriver driver;
	private WebElement element;
	private String url;

	// Properties
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:/chromedriver.exe";
	public Date today = new Date();
	public static int alarmCnt_ref = 0;

	public CGV_TENET_MK2() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

		// Driver SetUp
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
		driver = new ChromeDriver(options);
		// driver = new ChromeDriver();
		//확인할 url
		url = "http://www.cgv.co.kr/reserve/show-times/?areacode=01&theaterCode=0013&date=20220303";
	}

	public static void main(String[] args) {
		while (alarmCnt_ref < 10) {
			new CGV_TENET_MK2().operate();
		}
	}

	public void operate() {
		try {
			searchMovie();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			driver.close();
			driver.quit();
		}
	}

	public void searchMovie() {

		

		driver.get(url);
		boolean check=false;
		//element가 iframe 안에 있을 경우 xpath로 확인 불가
		//iframe으로 focus를 옮겨야됨.
		driver.switchTo().frame("ifrm_movie_time_table");
		
		try {
			System.out.println(driver.findElement(By.xpath("html/body/div/div[3]/ul/li[1]/div/div[7]/div[2]/ul/li/a/span[1]")).getText());
			//확인할 element의 xpath값 확인
			check = driver.findElement(By.xpath("html/body/div/div[3]/ul/li[1]/div/div[7]/div[2]/ul/li/a/span[1]")).getText().equals("준비중");
			
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("안되~~");
		}
		
		if (!check) {
			System.out.println("언택트 톡 준비중");
			alarmCnt_ref++;

			// 텔레그램 메세지 전숭 부분
			// 토큰
			// chat_id = https://api.telegram.org/bot+[토큰]+/getUpdates 으로 확인
			String Token = "**************************************************";
			String chat_id = "*******************";
			
			String text = "언택트톡 오픈!";

			BufferedReader in = null;

			try {

				URL obj = new URL(
						"https://api.telegram.org/bot" + Token + "/sendmessage?chat_id=" + chat_id + "&text=" + text); // �샇異쒗븷
																														// url

				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				String line;

				while ((line = in.readLine()) != null) { 
					System.out.println(line);
				}

			} catch (Exception e5) {
				e5.printStackTrace();
			} finally {
				if (in != null)
					try {
						in.close();
					} catch (Exception e5) {
						e5.printStackTrace();
					}
			}

		} else {
			System.out.println("언택트톡 오픈 안됨");
			System.out.println(today);
		}
	        

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			
			e1.printStackTrace();
		}
	}
}
