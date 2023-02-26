/********************************
 *	프로젝트 : gargoyle-webdriver
 *	패키지   : org.kyj.fx.gwd
 *	작성일   : 2023. 2. 25.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package org.kyj.fx.gwd;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import org.kyj.fx.gwd.util.FxUtil;
import org.kyj.fx.gwd.util.WebDriverInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * @author (zaruous@naver.com)
 *
 */
public class ByController {

	@FXML
	public ChoiceBox<String> cbSelection;
	@FXML
	public ChoiceBox<WebElementValueType> cbFindValueType;

	@FXML
	public TextField txtSectionValue, txtWebElementValue;
	private WebDriver webdriver;

	enum WebElementValueType {
		Attibute
	}

	@FXML
	public void initialize() {
		webdriver = WebDriverInstance.getInstance().getDriver();

		Method[] declaredMethods = By.class.getDeclaredMethods();
		for (Method m : declaredMethods) {
			if (!Modifier.isStatic(m.getModifiers()))
				continue;
			String mname = m.getName();
			cbSelection.getItems().add(mname);
		}

		cbFindValueType.getItems().add(WebElementValueType.Attibute);
	}

	@FXML
	public void btnFindOnAction() {
		WebDriverInstance.changeLastTabHandle();

		String mname = cbSelection.getSelectionModel().getSelectedItem();
		String sectionValue = txtSectionValue.getText();

		WebdriverHelperApp.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				try {
					Method declaredMethod = By.class.getDeclaredMethod(mname, String.class);

					By by = (By) declaredMethod.invoke(null, sectionValue);

					List<WebElement> findElements = webdriver.findElements(by);
					String dataWebPage = FxUtil.toDataWebPage(findElements);
					FxUtil.showNewWebPage().accept(dataWebPage);
					
//					Optional<String> reduce = findElements.stream().map(ele -> {
//						// System.out.println(ele);
//						return ele.getAttribute("outerHTML");
//						// return ele.getAttribute(attrName);
//					}).filter(v -> v != null).reduce((a1, a2) -> a1.concat("\n").concat(a2));
//					reduce.ifPresent(imges -> {
//
//						try (BufferedReader reader = new BufferedReader(new FileReader(new File("template")))) {
//							String temp = null;
//							StringBuilder sb = new StringBuilder();
//							while ((temp = reader.readLine()) != null) {
//								sb.append(temp).append("\n");
//							}
//							int startIdx = sb.indexOf("${content}");
//							int endIdx = startIdx + "${content}".length();
//							sb.replace(startIdx, endIdx, imges);
//
//							File file = new File("images.html");
//							if (!file.canWrite()) {
//								FxUtil.showMessageDialog( null, "경고", "", "파일이 사용중이므로 쓸수없음", null);
//								return;
//							}
//							try (FileWriter fileWriter = new FileWriter(file)) {
//								fileWriter.write(sb.toString());
//							}
//
//							webdriver.switchTo().newWindow(WindowType.WINDOW).get(file.toURI().toURL().toExternalForm());
//
//						} catch (FileNotFoundException e) {
//							e.printStackTrace();
//						} catch (IOException e1) {
//							e1.printStackTrace();
//						}
//
//					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
