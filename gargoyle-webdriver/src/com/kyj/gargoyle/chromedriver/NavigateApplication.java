/********************************
 *	프로젝트 : gargoyle-chromedriver
 *	패키지   : com.kyj.gargoyle.chromedriver
 *	작성일   : 2019. 9. 28.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.gargoyle.chromedriver;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class NavigateApplication extends ApplicationThread {
	private GargoyleChromeDriverApp app;
	private WebDriver driver;

	public NavigateApplication(GargoyleChromeDriverApp app, WebDriver driver) {
		super();
		this.app = app;
		this.driver = driver;
		this.setDaemon(true);
	}

	@Override
	public void run() {
		// *[@id="box"]/div/text()
		WebElement findElement = driver.findElement(By.xpath("//div[@id='box']/div"));
		if (findElement != null) {

			app.executeScript(driver,
					"document.evaluate(\"//div[@id='box']/div/a\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.remove();");

			findElement.click();

			String mp4 = getMP4(this.driver, e -> {
				try {
					if (e.isDisplayed()) {
						System.out.println(e.getTagName());
						System.out.println(e.getAttribute("src"));
						return e.getAttribute("src");
					} else
						System.out.println("not displayed.");

				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return null;
			});
			if (mp4 == null) {
				System.out.println("can;t found video node.");
			} else {
				
//				this.driver.get(mp4);
				String title = driver.getTitle().replace(':', ' ') + ".mp4";
				

				
				
				app.getObject(v -> {
					v.addProperty(title, mp4);
				});
				System.out.println(mp4);
			}
		}
		app.close(driver);
	}

	public <T> T getMP4(WebDriver driver, Function<WebElement, T> aceept) {
		WebElement findElement = null;

		try {

			List<WebElement> findElements = driver.findElements(By.tagName("iframe"));
			for (WebElement iframe : findElements) {
				WebDriver iframeDriver = null;
				try {
					iframeDriver = driver.switchTo().frame(iframe);
					if (iframeDriver == null)
						continue;
				} catch (Exception e) {

				}

				try {
					Thread.sleep(4000L);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				String xpath = "/html/body/div[1]/div/div[2]/div[4]/video";

				try {

					findElement = iframeDriver.findElement(By.xpath(xpath));
					if (findElement != null) {
						return aceept.apply(findElement);
					}

				} catch (Exception e) {
					// e.printStackTrace();
				}

				try {
					if (findElement == null) {
						JavascriptExecutor js = app.getJs(iframeDriver);

						// for (int i = 0; i < 2; i++) {
						Object executeScript = js.executeScript("document.evaluate('" + xpath
								+ "', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue");

						System.out.println(executeScript);

						findElement = (WebElement) executeScript;

						if (findElement != null)
							return aceept.apply(findElement);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (findElement != null)
				return aceept.apply(findElement);
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.switchTo().defaultContent();
		}
		return null;
	}

}
