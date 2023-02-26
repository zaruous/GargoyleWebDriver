/********************************
 *	프로젝트 : gargoyle-webdriver
 *	패키지   : org.kyj.fx.gwd
 *	작성일   : 2023. 2. 25.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package org.kyj.fx.gwd;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.kyj.fx.gwd.util.WebDriverInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * @author (zaruous@naver.com)
 *
 */

public class WebdriverHelperController {

	private WebDriver webdriver;
	@FXML
	private TextField txtUrl;
	@FXML
	private TextArea txtLog;
	@FXML
	private TableView<ImageInfoDVO> tvImage;
	@FXML
	private TableColumn<ImageInfoDVO, Boolean> tcCheck;
	@FXML
	private TableColumn<ImageInfoDVO, String> tcImage;
	@FXML
	private TableColumn<ImageInfoDVO, String> tcUrl;

	@FXML
	public void initialize() throws FileNotFoundException {
		initImageTable();
		initWebdriver();
	}

	private void initWebdriver() {

		webdriver = WebDriverInstance.getInstance().getDriver();
		WebdriverHelperApp.addMessageRecevier(new UncaughtErrorHandler() {

			public String toString(Throwable e) {
				return toString(e.getStackTrace());
			}

			/**
			 * @작성자 : KYJ
			 * @작성일 : 2017. 3. 28.
			 * @param stackTrace
			 * @return
			 */
			public String toString(StackTraceElement[] stackTrace) {
				return toString(null, stackTrace);
			}

			/**
			 * @작성자 : KYJ
			 * @작성일 : 2018. 9. 28.
			 * @param message
			 * @param stackTrace
			 * @return
			 */
			public static String toString(String message, StackTraceElement[] stackTrace) {
				StringBuffer sb = new StringBuffer();
				if (message != null && !message.isEmpty())
					sb.append(message).append(System.lineSeparator());
				if (stackTrace != null) {
					for (StackTraceElement st : stackTrace) {
						sb.append(st.toString()).append(System.lineSeparator());
					}
				}
				return sb.toString();

			}

			@Override
			public void handle(Thread t, Throwable e) {
				txtLog.appendText(toString(e));
			}
		});

	}

	private void initImageTable() {
		CheckBox headerCheck = new CheckBox();
		headerCheck.setOnAction(ev -> {
			tvImage.getItems().forEach(v -> {
				v.setCheck(headerCheck.isSelected());
			});
		});

		tcCheck.setGraphic(headerCheck);
		tcCheck.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ImageInfoDVO, Boolean>, ObservableValue<Boolean>>() {
			@Override
			public ObservableValue<Boolean> call(CellDataFeatures<ImageInfoDVO, Boolean> arg0) {
				return arg0.getValue().checkProperty();
			}
		});

		Callback<TableColumn<ImageInfoDVO, Boolean>, TableCell<ImageInfoDVO, Boolean>> forTableColumn = CheckBoxTableCell
				.forTableColumn(tcCheck);
		tcCheck.setCellFactory(forTableColumn);
		tcCheck.setEditable(true);

		tcImage.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ImageInfoDVO, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<ImageInfoDVO, String> arg0) {
				return arg0.getValue().urlProperty();
			}
		});
		tcUrl.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ImageInfoDVO, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<ImageInfoDVO, String> arg0) {
				return arg0.getValue().urlProperty();
			}
		});
		tcUrl.setCellFactory(new Callback<TableColumn<ImageInfoDVO, String>, TableCell<ImageInfoDVO, String>>() {

			@Override
			public TableCell<ImageInfoDVO, String> call(TableColumn<ImageInfoDVO, String> arg0) {
				return new TableCell<ImageInfoDVO, String>() {

					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							Hyperlink value = new Hyperlink(item);
							value.setOnAction(new EventHandler<ActionEvent>() {

								@Override
								public void handle(ActionEvent arg0) {
									try {

										Desktop.getDesktop().browse(new URI(item));
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							setGraphic(value);
						}
					}

				};
			}
		});
		tcImage.setCellFactory(new Callback<TableColumn<ImageInfoDVO, String>, TableCell<ImageInfoDVO, String>>() {

			@Override
			public TableCell<ImageInfoDVO, String> call(TableColumn<ImageInfoDVO, String> arg0) {
				return new TableCell<ImageInfoDVO, String>() {

					@Override
					protected void updateItem(String url, boolean isEmpty) {
						super.updateItem(url, isEmpty);
						if (isEmpty) {
							setGraphic(null);
						} else {
							setGraphic(new ImageView(url));
						}
					}

				};
			}
		});

		MenuItem miCheckDownload = new MenuItem("선택한 항목 다운로드");
		miCheckDownload.setOnAction(this::miCheckDownloadOnAction);
		tvImage.setContextMenu(new ContextMenu(miCheckDownload));

		tvImage.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 26.
	 * @param e
	 */
	public void miCheckDownloadOnAction(ActionEvent e) {
		WebdriverHelperApp.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				String format = simpleDateFormat.format(new Date());

				File dir = new File(new File("download"), format);
				dir.mkdirs();

				Stream.iterate(0, a -> a + 1).limit(tvImage.getItems().size()).filter(idx -> {
					return tvImage.getItems().get(idx).isCheck();
				}).forEach(idx -> {
					ImageInfoDVO v = tvImage.getItems().get(idx);

					try (InputStream in = new URL(v.getUrl()).openConnection().getInputStream()) {

						byte[] b = new byte[4096];
						int r = -1;
						try (FileOutputStream writer = new FileOutputStream(new File(dir, idx + ".jpeg"))) {
							while ((r = in.read(b)) != -1) {
								writer.write(b, 0, r);
							}
						}

					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				});
			}
		});

	}

	@FXML
	public void btnUrlEnterOnAction() {

		changeLastTabHandle();
		String url = txtUrl.getText();
		if (url.isEmpty())
			return;

		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			String property = WebDriverInstance.getConfig().getProperty("defaultSearchEngine", "");
			url = property.replace("${keyword}", url);
		}

		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			txtLog.appendText("url은 http:// 혹은 https:// 로 시작해야합니다.");
			return;
		}

		String srchUrl = url;
		WebdriverHelperApp.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				webdriver.get(srchUrl);
			}
		});

	}

	@FXML
	public void btnImageDownloadOnAction() {
		WebdriverHelperApp.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {

				changeLastTabHandle();

				List<ImageInfoDVO> collect = webdriver.findElements(By.tagName("img")).stream().map(we -> {
					return we.getAttribute("src");
				}).filter(str -> str != null).filter(str -> str.startsWith("http")).map(str -> {
					ImageInfoDVO d = new ImageInfoDVO();
					d.setUrl(str);
					return d;
				}).collect(Collectors.toList());

				Platform.runLater(() -> {
					tvImage.getItems().setAll(collect);
					txtUrl.setText(webdriver.getCurrentUrl());
				});
			}

		});

	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 26.
	 */
	private void changeLastTabHandle() {
		WebDriverInstance.changeLastTabHandle();
	}

	@FXML
	public void btnPageSourceOnAction() {

		WebdriverHelperApp.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				changeLastTabHandle();
				String pageSource = webdriver.getPageSource();

				Platform.runLater(() -> {
					Stage s = new Stage();
					s.setScene(new Scene(new BorderPane(new TextArea(pageSource))));
					s.initOwner(WebdriverHelperApp.getPrimaryStage());
					s.showAndWait();
				});
			}

		});

	}

	@FXML
	private TextField txtImgUrlForRegex;

	ObservableList<ImageInfoDVO> tmpOriginalList = FXCollections.observableArrayList();

	@FXML
	public void txtImgUrlForRegexOnKeyReleased() {
		String regex = txtImgUrlForRegex.getText();
		if (regex.trim().isEmpty()) {
			this.tvImage.getItems().setAll(tmpOriginalList);
			tmpOriginalList.clear();
			return;
		}

		if (tmpOriginalList.isEmpty())
			tmpOriginalList.setAll(this.tvImage.getItems());

		Pattern compile = Pattern.compile(regex);
		List<ImageInfoDVO> collect = tmpOriginalList.stream().filter(v -> {
			return compile.matcher(v.getUrl()).find();
		}).collect(Collectors.toList());
		this.tvImage.getItems().setAll(collect);
	}

	@FXML
	public void imgUrlFilterOnAction() {
		txtImgUrlForRegexOnKeyReleased();

	}

	@FXML
	public void byOnAction() throws IOException {
		Stage stage = new Stage();
		stage.setTitle("Dom Finder");
		stage.setScene(new Scene(FXMLLoader.load(ByController.class.getResource("ByApp.fxml"))));
		stage.showAndWait();
	}

	@FXML
	public void txtUrlOnKeyReleased(KeyEvent ex) {
		if (ex.getCode() == KeyCode.ENTER)
			btnUrlEnterOnAction();
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 26.
	 */
	@FXML
	public void btnBookTokiComicDownloadScriptOnAction() {

		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Download 위치 지정");
		chooser.setInitialDirectory(new File("./"));
		// installDefaultPath(chooser);
		// if (option != null)
		// option.accept(chooser);

		File dir = chooser.showDialog(WebdriverHelperApp.getPrimaryStage());
		if (dir != null) {
			new BookToki().download(dir);
		}

	}
}
