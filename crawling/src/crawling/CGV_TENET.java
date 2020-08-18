package crawling;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CGV_TENET {

	// WebDriver
	private WebDriver driver;
	private WebElement element;
	private String url;

	// Properties
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:/chromedriver.exe";
	public Date today = new Date();
	public static int alarmCnt = 0;

	public CGV_TENET() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

		// Driver SetUp
		ChromeOptions options = new ChromeOptions();
		options.addArguments("enable-automation");
		options.addArguments("--headless");
		options.addArguments("--window-size=1920,1080");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-extensions");
		options.addArguments("--dns-prefetch-disable");
		options.addArguments("--disable-gpu");
		options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		driver = new ChromeDriver(options);
		// driver = new ChromeDriver();
		url = "http://www.cgv.co.kr/";
	}

	public static void main(String[] args) {
		while (alarmCnt < 10) {
			new CGV_TENET().operate();
		}
	}

	public void operate() {
		try {
			searchMovie();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			System.out.println("드라이버 닫음");
			driver.close();
			driver.quit();
		}
	}

	public void searchMovie() {

		/* Scanner sc = new Scanner(System.in); */
		try {
			driver.get(url);
//         header_keyword 검색 (메인 페이지 검색창)
			element = driver.findElement(By.name("header_keyword"));
			System.out.print("영화 제목 : 테넷");

			// 입력받은 문자열 검색창에 입력
			element.sendKeys("테넷");

			// 검색 영역에서 엔터
			element.sendKeys(Keys.RETURN);
			Thread.sleep(1000);
//            sect-noresult : 검색결과 없음
			element = driver.findElement(By.className("sect-noresult"));
			System.out.println("검색된 결과가 없습니다.");

		} catch (Exception e) {
			// 검색된 결과가 있다면 오는 영역
//           sect-chart		: 검색결과중 영화 sect
//        	 box-contents	: 영화sect 영화 상세정보 sect
//			 link-reservation : 예매가능하면 생기는 버튼
			element = driver.findElement(By.className("sect-chart"));
			element = element.findElement(By.className("box-contents"));
			// 예매 가능여부 판단 boolean
			boolean isReserv = false;
			// 예매버튼 유무 판단
			isReserv = element.findElements(By.className("link-reservation")).size() > 0;
			if (isReserv) {
				System.out.println("예매 오픈 달려라!~~~");
				System.out.println(today);
				alarmCnt++;

				// 텔레그램 메시지 전송 부분
				String Token = "토큰 입력";
				String chat_id = "챗 아이디 입력";
				// 테스트용 chat_id
				 //String chat_id = "테스트 챗 아이디";
				String text = "테넷 예매 오픈!";

				BufferedReader in = null;

				try {

					URL obj = new URL("https://api.telegram.org/bot" + Token + "/sendmessage?chat_id=" + chat_id
							+ "&text=" + text); // 호출할 url

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
				System.out.println("아직 멀었다.");
				System.out.println(today);
			}
		}
		
	}
}
