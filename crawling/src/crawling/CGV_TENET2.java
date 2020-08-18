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

public class CGV_TENET2 {

	// WebDriver
	private WebDriver driver;
	private WebElement element;
	private String url;

	// Properties
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:\\chromedriver.exe";
	public Date today = new Date();
	public static int alarmCnt_ref = 0;
	public static int alarmCnt = 0;
	

	public CGV_TENET2() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

		// Driver SetUp
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
		driver = new ChromeDriver(options);
		// driver = new ChromeDriver();

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

	public static void sendMessage(String text) {
		String Token = "토큰 입력";
		 String chat_id = "챗 아이디 입력";
		// 테스트용 chat_id
		//String chat_id = "테스트 챗 아이디";

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

	}

	public void searchMovie() {

		for (int i = 0; i < 8; i++) {
			/* Scanner sc = new Scanner(System.in); */
			url = "http://www.cgv.co.kr/common/showtimes/iframeTheater.aspx?"
					+ "areacode=01&theatercode=0013&date=2020082" + i
					+ "&screencodes=&screenratingcode=02&regioncode=07";
			driver.get(url);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (driver.getPageSource().contains("테넷") || driver.getPageSource().contains("TENET")) {
				System.out.println("테넷 예매 오픈(상영표 참조버전 2" + i + "일)");
				alarmCnt++;
				
				// 텔레그램 메시지 전송 부분
				sendMessage("테넷 예매 오픈(상영표 참조 버전 결과 2" + i + "일)");

			} else {
				
				System.out.println("테넷 오픈 안함(상영표 참조버전 2" + i + "일)");
				System.out.println(today);
			}
		
		}
		if(alarmCnt>0) {
			alarmCnt_ref++;
			alarmCnt =0;
		}
//	        

		url = "http://www.cgv.co.kr/";

		try {
			driver.get(url);
//         header_keyword 검색 (메인 페이지 검색창)
			element = driver.findElement(By.name("header_keyword"));

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
				System.out.println("테넷 예매 오픈(검색 버전)");
				System.out.println(today);
				alarmCnt_ref++;

				// 텔레그램 메시지 전송 부분
				sendMessage("테넷 예매 오픈(검색 버전 결과)");
			} else {
				System.out.println("테넷 오픈 안함(검색 버전)");
				System.out.println(today);
			}
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
