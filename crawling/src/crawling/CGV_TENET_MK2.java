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

public class CGV_TENET2_MK2 {

	// WebDriver
	private WebDriver driver;
	private WebElement element;
	private String url;

	// Properties
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:/chromedriver.exe";
	public Date today = new Date();
	public static int alarmCnt_ref = 0;

	public CGV_TENET2_MK2() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

		// Driver SetUp
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
		driver = new ChromeDriver(options);
		// driver = new ChromeDriver();
		url = "http://www.cgv.co.kr/common/showtimes/iframeTheater.aspx?"
				+ "areacode=01&theatercode=0013&date=20200826&screencodes=&screenratingcode=02&regioncode=07";
	}

	public static void main(String[] args) {
		while (alarmCnt_ref < 10) {
			new CGV_TENET2().operate();
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

		/* Scanner sc = new Scanner(System.in); */

		driver.get(url);
		if (driver.getPageSource().contains("테넷") || driver.getPageSource().contains("TENET")) {
			System.out.println("TENET 오픈(상영표 참조버전)");
			alarmCnt_ref++;

			// 텔레그램 메시지 전송 부분
			String Token = "토큰 입력";
			String chat_id = "챗 아이디 입력";
			// 테스트용 chat_id
			 //String chat_id = "테스트 챗 아이디";
			String text = "테넷 예매 오픈!(상영표 참조버전)";

			BufferedReader in = null;

			try {

				URL obj = new URL(
						"https://api.telegram.org/bot" + Token + "/sendmessage?chat_id=" + chat_id + "&text=" + text); // 호출할
																														// url

				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				String line;

				while ((line = in.readLine()) != null) { // response를 차례대로 출력
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
			System.out.println("TENET 오픈안함(상영표 참조버전)");
			System.out.println(today);
		}
//	        

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
