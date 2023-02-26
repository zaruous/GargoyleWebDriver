/********************************
 *	프로젝트 : gargoyle-webdriver
 *	패키지   : org.kyj.fx.gwd.util
 *	작성일   : 2023. 2. 26.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package org.kyj.fx.gwd.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.kyj.fx.gwd.WebdriverHelperApp;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * @author (zaruous@naver.com)
 *
 */
public class FxUtil {

	/**
	 * show info Dialog
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 12.
	 * @param initOwner
	 * @param title
	 * @param headerText
	 * @param message
	 * @param apply
	 */
	public static void showMessageDialog(Stage initOwner, String title, String headerText, String message, Consumer<Alert> apply) {

		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(title);
			alert.setHeaderText(headerText);
			alert.setContentText(message);
			if (initOwner == null)
				alert.initOwner(WebdriverHelperApp.getPrimaryStage());
			else
				alert.initOwner(initOwner);
			apply.accept(alert);
		});

		// Dialog<Pair<String, String>> dialog = new Dialog<>();
		// dialog.setTitle(title);
		// dialog.setHeaderText(headerText);
		// dialog.setContentText(message);
		// dialog.initOwner(initOwner);
		// apply.accept(dialog);
	}

	static Consumer<String> action = htmlDataCollections -> {

		try (BufferedReader reader = new BufferedReader(new FileReader(new File("template")))) {
			String temp = null;
			StringBuilder sb = new StringBuilder();
			while ((temp = reader.readLine()) != null) {
				sb.append(temp).append("\n");
			}
			int startIdx = sb.indexOf("${content}");
			int endIdx = startIdx + "${content}".length();
			sb.replace(startIdx, endIdx, htmlDataCollections);

			File file = new File("images.html");
			if (!file.canWrite()) {
				showMessageDialog(null, "경고", "", "파일이 사용중이므로 쓸수없음", null);
				return;
			}
			try (FileWriter fileWriter = new FileWriter(file)) {
				fileWriter.write(sb.toString());
			}

			WebDriver driver = WebDriverInstance.getInstance().getDriver();
			driver.switchTo().newWindow(WindowType.WINDOW).get(file.toURI().toURL().toExternalForm());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	};

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 26.
	 * @return
	 */
	public static Consumer<? super String> showNewWebPage() {
		return action;
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 26.
	 * @param findElements
	 * @return
	 */
	public static String toDataWebPage(List<WebElement> findElements) {
		Optional<String> reduce = findElements.stream().map(ele -> {
			// System.out.println(ele);
			return ele.getAttribute("outerHTML");
			// return ele.getAttribute(attrName);
		}).filter(v -> v != null).reduce((a1, a2) -> a1.concat("\n").concat(a2));

		if (reduce.isPresent()) {
			return reduce.get();
		}
		return "";
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 26.
	 * @param findElements
	 * @return
	 */
	public static List<String> toImageSrcList(List<WebElement> findElements) {
		return findElements.stream().map(ele -> {
			return ele.getAttribute("src");
		}).filter(v -> v != null).collect(Collectors.toList());

	}
}
