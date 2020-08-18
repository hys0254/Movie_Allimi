package crawling;

import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Melon {
	
	//WebDriver
	private WebDriver driver;
	private WebElement element;
	private String url;
	
	//Properties
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:/chromedriver.exe";
	
	public Melon() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		
		//Driver SetUp
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("headless");
//		driver = new ChromeDriver(options);
		driver = new ChromeDriver();
		url = "https://www.melon.com/";
	}
	
	public static void main(String[] args) {
		System.out.println("시작");
		new Melon().operate();
	}
	
	public void operate() {
		try {
			searchSong();
			
		}catch(Exception e) {
			System.out.println(e);
		}finally {
			driver.close();
			driver.quit();
		}
	}
	
	
	public void searchSong() {
		Scanner sc = new Scanner(System.in);
		try {
			driver.get(url);
//		ui-autocomplete-input 검색
			element = driver.findElement(By.id("top_search"));
			System.out.print("노래 제목 : ");
			
			//입력받은 문자열 검색창에 입력
			element.sendKeys(sc.nextLine());
			
			//검색 영역에서 엔터
			element.sendKeys(Keys.RETURN);
			
			Thread.sleep(1000);
//			section_no_data : 검색결과 없음
			element = driver.findElement(By.className("section_no_data"));
			System.out.println("검색된 결과가 없습니다.");
			
		} catch (Exception e) {
			//검색된 결과가 있다면 오는 영역
//			songTypeOne : 곡명으로 검색
			
			element = driver.findElement(By.id("frm_searchSong"));
			
			List<WebElement> numList =  element.findElements(By.className("no"));
			List<WebElement> titleList = element.findElements(By.className("fc_gray"));
			//아티스트 찾기 : List에 넣기 내일까지 해오기~
			List<WebElement> artistList = element.findElements(By.id("artistName"));
			
			for (int i = 0; i < numList.size(); i++) {
				System.out.println(numList.get(i).getText() + ". " + titleList.get(i).getText() + ", 아티스트 : " + artistList.get(i).getText());
			}
			
			System.out.print("곡 번호 : ");
			int num = sc.nextInt();
			//엔터 상쇄
			sc.nextLine();
			List<WebElement> detailList = element.findElements(By.className("btn_icon_detail"));
			
			detailList.get(num-1).click();
			try {Thread.sleep(1000);} catch (InterruptedException e1) {;}
			
			try {
				driver.findElement(By.className("button_more")).click();
				try {Thread.sleep(1000);} catch (InterruptedException e1) {;}
			} catch (Exception e1) {
				
			}
			
			//가사 찾기(class = lyric)
			try {
				element = driver.findElement(By.className("lyric"));
				System.out.println(element.getText());
			} catch (Exception e1) {
				try {
					driver.findElement(By.className("btn_base02 adult_register")).click();
					try {Thread.sleep(1000);} catch (InterruptedException e2) {;}
					System.out.println("성인인증이 필요합니다.");
				} catch (Exception e2) {
					System.out.println("가사 정보가 없습니다.");
				}
			}
		}
	}
}









