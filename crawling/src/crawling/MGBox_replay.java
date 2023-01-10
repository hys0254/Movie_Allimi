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

public class MGBox_replay {
	// WebDriver
	private WebDriver driver;
	private WebElement element;
	private String url;

	// Properties
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:/chromedriver.exe";
	public Date today = new Date();
	public static int[] alarmCnt_ref= new int[6];
	

	public MGBox_replay() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

		// Driver SetUp 창 띄우기 안띄우기 headless 면 창 안띄우기
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
		driver = new ChromeDriver(options);
		// driver = new ChromeDriver();
		// 확인할 url
		url = "https://www.megabox.co.kr/theater/time?brchNo=1351";

	}

	public static void main(String[] args) {
		while (true) {
			try {
				new MGBox_replay().operate();
			} catch (Exception e) {
				System.out.println("main 오류 발생");
				telegramAlert("오류 발생 확인 요망");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void operate() {

		try {

			
				searchMovie("2023.01.28","나이트",1);
				searchMovie("2023.01.29","드라이브",2);
				searchMovie("2023.02.03","파더",3);
				searchMovie("2023.02.12","에브리",4);
				searchMovie("2023.02.19","헤어질",5);
				
			

		} catch (Exception e) {
			System.out.println("operate 오류 발생");
			telegramAlert("오류 발생 확인 요망");
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
			System.out.println("waitforit 오류 발생");
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
					"https://api.telegram.org/bot" + Token + "/sendmessage?chat_id=" + chatID + "&text=" + msg);

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

	public void searchMovie(String date, String movieName, int cntNum) {

		driver.get(url);

		

		// 1. 특정 요일 선택 열림 확인
		WebElement daySelector = driver
				.findElement(By.xpath("/html/body/div[2]/div[3]/div[2]/div[2]/div[2]/div[2]/div[2]/div/div[1]"));

		daySelector = daySelector.findElement(By.className("date-area"));
		daySelector = daySelector.findElement(By.className("wrap"));
		List<WebElement> dayList = daySelector.findElements(By.tagName("button"));
		for (WebElement web : dayList) {
			if (web.getAttribute("date-data").equals(date)) {
				System.out.println(web.getAttribute("date-data"));
				System.out.println(date+" 예매 오픈");

				if (!web.getAttribute("class").equals("disabled") && alarmCnt_ref[cntNum] < 10) {
					web.click();
					waitforit();
					
					WebElement movieListBox = driver.findElement(By.xpath("//*[@id=\"tab02\"]/div[4]"));
					
					// 영화 리스트
					List<WebElement> movieList = movieListBox.findElements(By.className("theater-list"));
					// 해당 리스트 중에 두번째 p태그에 특정 영화가 있는지 확인
					for (WebElement movie : movieList) {
						String movieText = movie.findElement(By.className("theater-tit")).findElements(By.tagName("p"))
								.get(1).getText();
						if (movieText.contains(movieName)) {
							System.out.println(movieText);
							System.out.println(movieName+" 있음");
							telegramAlert("["+cntNum+"] "+date+" "+movieName+" "+"예매 오픈");
							alarmCnt_ref[cntNum]++;
						}
					}
				} else {
					System.out.println("활성화 안됨");
				}
				
				break;
			}
		}
	}
}
