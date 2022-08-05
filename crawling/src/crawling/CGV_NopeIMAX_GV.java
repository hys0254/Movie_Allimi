package crawling;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CGV_NopeIMAX_GV {
	// WebDriver
	private WebDriver driver;
	private WebElement element;
	private String url;
	
	// Properties
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:/chromedriver.exe";
	public static int peopleCnt=0;
	public Date today = new Date();
	public static int alarmCnt_ref = 0;

	public CGV_NopeIMAX_GV() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

		// Driver SetUp 창 띄우기 안띄우기 headless 면 창 안띄우기
		ChromeOptions options = new ChromeOptions();
//		options.addArguments("headless");
		driver = new ChromeDriver(options);
		// driver = new ChromeDriver();
		// 확인할 url
		url = "http://www.cgv.co.kr/ticket/?MOVIE_CD=20030316&MOVIE_CD_GROUP=20029139&PLAY_YMD=20220812&THEATER_CD=0013&PLAY_START_TM=1930&AREA_CD=13&SCREEN_CD=018";

	}

	public static void main(String[] args) {
		System.out.println("놉 가즈아");
		while (true) {

			try {
				new CGV_NopeIMAX_GV().operate();
			} catch (Exception e) {
				telegramAlert(" main 메서드 예외 발생 확인 요망");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void operate() {

		try {

			//관객수 달라지면 알람 on

			searchMovie();

		} catch (Exception e) {
			telegramAlert("operate메서드 예외 발생 확인 요망");
			System.out.println(e);
		} finally {

			driver.close();
			driver.quit();
		}

	}

	public void waitforit() {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			telegramAlert("threadsleep 오류 발생 확인 요망");
		}
	}

	public static void telegramAlert(String msg) {
		// 텔레그램 메세지 전숭 부분
		// 토큰
		// chat_id = https://api.telegram.org/bot+[토큰]+/getUpdates 으로 확인

		BufferedReader in = null;

		try {

			URL obj = new URL(
					"https://api.telegram.org/bot" + Token + "/sendmessage?chat_id=" + chat_id + "&text=" + msg);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");

			in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String line;

			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}

		} catch (Exception e5) {
			e5.printStackTrace();
			telegramAlert("텔레그램 메세지 전송 오류 발생 확인 요망");
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

		driver.get(url);

		
		// element가 iframe 안에 있을 경우 xpath로 확인 불가
		// iframe으로 focus를 옮겨야됨.
		driver.switchTo().frame("ticket_iframe");
		waitforit();
		// 원하는 영화관 선택
		WebElement theaterSelector = driver.findElement(
				By.xpath("//*[@id=\"ticket\"]/div[2]/div[1]/div[4]/div[2]/div[3]/div[1]/div/ul/li/a/span[2]"));
		waitforit();
		String status =theaterSelector.getText();
		if (status.contains("매진")) {
			peopleCnt=0;
		}else {
			status=status.substring(0, status.length()-1);
			try {
				int integerStatus = Integer.parseInt(status);
				if(integerStatus>peopleCnt) {
					telegramAlert("잔여좌석 발생 %28"+peopleCnt+
							" -%3E "+integerStatus+"%29 확인 요망!!");
					System.out.println("잔여좌석 발생 !");
					System.out.println(peopleCnt+" -> "+integerStatus);
					peopleCnt=integerStatus;
				}
			} catch (NumberFormatException e) {
				System.out.println("잔여좌석 숫자로 변환 불가 확인 요망!");
			}
		}
		

	}

}
