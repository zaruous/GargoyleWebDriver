/********************************
 *	프로젝트 : gargoyle-chromedriver
 *	패키지   : com.kyj.gargoyle.chromedriver
 *	작성일   : 2019. 9. 28.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.gargoyle.chromedriver;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class ApplicationThread extends Thread {
	private GargoyleChromeDriverApp app;
	private WebDriver driver;

	public ApplicationThread() {
		super();
		this.setDaemon(true);
	}

	public ApplicationThread(GargoyleChromeDriverApp app, WebDriver driver) {
		super();
		this.app = app;
		this.driver = driver;
		this.setDaemon(true);
	}

	@Override
	public void run() {
		List<WebElement> findElements = driver.findElements(By.ByXPath.xpath("//div[@class='ani_video_list']/a"));
		// ArrayList<String> arrayList = new ArrayList<String>();
		List<String> urls = findElements.stream().map(e -> e.getAttribute("href")).collect(Collectors.toList());
		Consumer<? super String> action = url -> {

			WebDriver driver;
			try {
				driver = app.createDriver(app.getDriverType());
				System.out.println("visite url : " + url);
				driver.get(url);
				
				NavigateApplication n = new NavigateApplication(app, driver);
				n.start();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			
		};
		
		urls.forEach(action);

	}

}
