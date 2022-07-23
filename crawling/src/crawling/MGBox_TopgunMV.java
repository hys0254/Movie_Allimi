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

public class MGBox_TopgunMV {
	// WebDriver
	private WebDriver driver;
	private WebElement element;
	private String url;

	// Properties
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:/chromedriver.exe";
	public static int peopleCnt_19 = 73;
	public static int peopleCnt_22 = 178;
	public Date today = new Date();
	public static int alarmCnt_ref = 0;

	public MGBox_TopgunMV() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

		// Driver SetUp 창 띄우기 안띄우기 headless 면 창 안띄우기
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
		driver = new ChromeDriver(options);
		// driver = new ChromeDriver();
		// 확인할 url
		url = "https://www.megabox.co.kr/specialtheater/dolby/time";

	}

	public static void main(String[] args) {
		while (true) {
			
			try {
				new MGBox_TopgunMV().operate();
			} catch (Exception e) {
				telegramAlert("오류 발생 확인 요망");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void operate() {

		try {

			// 매개변수 관객수 관객수 달라지면 알람 on

			searchMovie();

		} catch (Exception e) {
			telegramAlert("오류 발생 확인 요망");
			System.out.println(e);
		} finally {
			
			driver.close();
			driver.quit();
		}

	}

	public void waitforit() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			telegramAlert("오류 발생 확인 요망");
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
			telegramAlert("오류 발생 확인 요망");
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

		boolean check = false;

		// 원하는 영화관 선택
		WebElement theaterSelector = driver
				.findElement(By.xpath("/html/body/div[2]/div[3]/div[2]/div[2]/div[5]/div/div/div[1]/button[5]"));
		theaterSelector.click();
		waitforit();

		// 1. 특정 요일 선택 열림 확인
		WebElement daySelector = driver
				.findElement(By.xpath("/html/body/div[2]/div[3]/div[2]/div[2]/div[5]/div/div/div[4]/div/div[1]"));
		daySelector = daySelector.findElement(By.className("date-area"));
		daySelector = daySelector.findElement(By.className("wrap"));
		List<WebElement> dayList = daySelector.findElements(By.tagName("button"));
		for (WebElement web : dayList) {
			if (web.getAttribute("date-data").equals("2022.07.27")) {

				daySelector = web;
				break;
			}
		}
		// 예매 오픈 탑건 존재 확인
		if (!daySelector.getAttribute("class").equals("disabled") && alarmCnt_ref < 10) {
			daySelector.click();
			waitforit();
			WebElement movieListBox = driver
					.findElement(By.xpath("/html/body/div[2]/div[3]/div[2]/div[2]/div[5]/div/div/div[6]"));
			// 영화 리스트
			List<WebElement> movieList = movieListBox.findElements(By.className("theater-list"));
			// 해당 리스트 중에 두번째 p태그에 특정 영화가 있는지 확인
			for (WebElement movie : movieList) {
				String movieText = movie.findElement(By.className("theater-tit")).findElements(By.tagName("p")).get(1)
						.getText();
				if (movieText.contains("탑건")) {
					telegramAlert("[1] 27일 탑건 예매 열림");
					alarmCnt_ref++;
				}
			}
		}

		// 2. 특정 날짜 자리수 변환 확인
		// 화요일 예매 자리 확인
		daySelector = driver
				.findElement(By.xpath("/html/body/div[2]/div[3]/div[2]/div[2]/div[5]/div/div/div[4]/div/div[1]"));
		daySelector = daySelector.findElement(By.className("date-area"));
		daySelector = daySelector.findElement(By.className("wrap"));
		dayList = daySelector.findElements(By.tagName("button"));
		for (WebElement web : dayList) {
			if (web.getAttribute("date-data").equals("2022.07.26")) {

				daySelector = web;
				break;
			}
		}
		daySelector.click();
		waitforit();
		WebElement movieListBox = driver
				.findElement(By.xpath("/html/body/div[2]/div[3]/div[2]/div[2]/div[5]/div/div/div[6]"));
		// 영화 리스트
		List<WebElement> movieList = movieListBox.findElements(By.className("theater-list"));
		// 해당 리스트 중에 두번째 p태그에 특정 영화가 있는지 확인
		for (WebElement movie : movieList) {
			String movieText = movie.findElement(By.className("theater-tit")).findElements(By.tagName("p")).get(1)
					.getText();
			if (movieText.contains("탑건")) {
				WebElement timeFinder = movie.findElement(By.className("theater-type-box"));
				timeFinder = timeFinder.findElement(By.className("theater-time"));
				timeFinder = timeFinder.findElement(By.className("theater-time-box"));
				timeFinder = timeFinder.findElement(By.className("time-list-table"));
				List<WebElement> timeList = timeFinder.findElements(By.tagName("td"));
				boolean scanOver = false;
				for (WebElement time : timeList) {

					if (time.findElement(By.className("time")).getText().equals("19:20")) {
						int peopleCnt = Integer
								.parseInt(time.findElement(By.className("chair")).getText().replaceAll("[^0-9]", ""));
						System.out.println(peopleCnt);
						if (peopleCnt != peopleCnt_19) {
							telegramAlert("[2.1] 26일 화요일 %0a 19시 인원 달라짐 확인 요망!!");
							peopleCnt_19 = peopleCnt;
						}

					}
					waitforit();
					if (time.findElement(By.className("time")).getText().equals("22:10")) {
						scanOver = true;
						int peopleCnt = Integer
								.parseInt(time.findElement(By.className("chair")).getText().replaceAll("[^0-9]", ""));
						System.out.println(peopleCnt);
						if (peopleCnt != peopleCnt_22) {
							telegramAlert("[2.2] 26일 화요일 %0a 22시 인원 달라짐 확인 요망!!");
							peopleCnt_22 = peopleCnt;
						}
					}
					if (scanOver)
						break;
				}

			}
		}
	}

}
