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

import io.netty.util.concurrent.ThreadPerTaskExecutor;

public class CGV_MI7IMAX_GV {
	// WebDriver
	private WebDriver driver;
	private WebElement element;
	private String url;

	// Properties
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:/chromedriver.exe";
	public static int peopleCnt = 0;
	public Date today = new Date();
	public static int alarmCnt_ref = 0;
	public static boolean openCheck = false;

	public CGV_MI7IMAX_GV() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

		// Driver SetUp 창 띄우기 안띄우기 headless 면 창 안띄우기
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("headless");
//		driver = new ChromeDriver(options);
		driver = new ChromeDriver();
		// 확인할 url
		url = "http://www.cgv.co.kr/ticket/?MOVIE_CD=20033171&MOVIE_CD_GROUP=20033088&PLAY_YMD=20230722&THEATER_CD=0013";

	}

	public static void main(String[] args) {
		System.out.println("MI7 가즈아");
		while (true) {

			try {
				new CGV_MI7IMAX_GV().operate();
			} catch (Exception e) {
				telegramAlert(" main 메서드 예외 발생 확인 요망");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void operate() {

		try {

			// 관객수 달라지면 알람 on

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
//		// 텔레그램 메세지 전숭 부분
//		// 토큰
//		// chat_id = https://api.telegram.org/bot+[토큰]+/getUpdates 으로 확인
//
//		BufferedReader in = null;
//
//		try {
//
//			URL obj = new URL(
//					"https://api.telegram.org/bot" + Token + "/sendmessage?chat_id=" + chat_id + "&text=" + msg);
//
//			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//			con.setRequestMethod("GET");
//
//			in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
//			String line;
//
//			while ((line = in.readLine()) != null) {
//				System.out.println(line);
//			}
//
//		} catch (Exception e5) {
//			e5.printStackTrace();
//			telegramAlert("텔레그램 메세지 전송 오류 발생 확인 요망");
//		} finally {
//			if (in != null)
//				try {
//					in.close();
//				} catch (Exception e5) {
//					e5.printStackTrace();
//				}
//		}

	}

	public void searchMovie() {

		driver.get(url);

		// element가 iframe 안에 있을 경우 xpath로 확인 불가
		// iframe으로 focus를 옮겨야됨.
		driver.switchTo().frame("ticket_iframe");
		waitforit();
		// 22일 오픈 확인
		if (!openCheck) {
			WebElement dateSelector = driver
					.findElement(By.xpath("//*[@id=\"date_list\"]/ul/div/li[@date=\"20230722\"]/a/span[3]"));
			waitforit();
			// getText()로 내부 문자 반환이 안될때 getAttribute("innerHTML") 혹은
			// getAttribute("innerText")로 반환 가능
			String status = dateSelector.getAttribute("innerHTML");
			if (status.equals("선택불가")) {
				System.out.println("선택불가");
			} else {
				System.out.println("22일 예매 오픈");
				telegramAlert("MI7:데드레코딩 22일 예매 오픈 확인 요망");
				openCheck = true;
			}
		}else {
			WebElement dateSelector = driver
					.findElement(By.xpath("//*[@id=\"date_list\"]/ul/div/li[@date=\"20230722\"]/a"));
			dateSelector.click();
			waitforit();
			WebElement theaterSelector = driver.
					findElement(By.xpath("//*[@id=\"ticket\"]/div[2]/div[1]/div[4]/div[2]/div[3]/div[1]"));
			waitforit();
			waitforit();
			List<WebElement> theaterList = theaterSelector.findElements(By.className("theater"));
			for(WebElement e : theaterList) {
				String theaterType = e.findElement(By.xpath("./span[1]/span[2]")).getText();
				System.out.println(theaterType);
				if(theaterType.equals("IMAX관")) {
					System.out.println("IMAX 예매 오픈");
					telegramAlert("MI7:데드레코딩 22일 IMAX관 오픈 확인 요망");
				}
			}
		}


	}

}
