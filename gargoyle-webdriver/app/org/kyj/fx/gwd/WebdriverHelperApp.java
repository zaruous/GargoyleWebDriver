/********************************
 *	프로젝트 : gargoyle-webdriver
 *	패키지   : org.kyj.fx.gwd
 *	작성일   : 2023. 2. 25.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package org.kyj.fx.gwd;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.kyj.fx.gwd.util.AppValidation;
import org.kyj.fx.gwd.util.WebDriverInstance;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author (zaruous@naver.com)
 *
 */
public class WebdriverHelperApp extends Application {

	static List<UncaughtErrorHandler> messageReceivers = new ArrayList<>();

	public static void addMessageRecevier(UncaughtErrorHandler handler) {
		messageReceivers.add(handler);
	}

	private static ExecutorService threadPool;

	public static ExecutorService getThreadPool() {
		return threadPool;
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 25.
	 * @param args
	 */
	public static void main(String[] args) {
		messageReceivers.add(new UncaughtErrorHandler() {

			@Override
			public void handle(Thread t, Throwable e) {
				e.printStackTrace();
			}
		});
		launch(args);
	}

	public static Stage primaryStage;

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	@Override
	public void start(Stage stage) throws Exception {

		threadPool = Executors.newFixedThreadPool(10, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setDaemon(true);
				t.setName("Helper");
				return t;
			}

		});

		/**/
		primaryStage = stage;

		stage.setOnCloseRequest(ev -> {
			/* 프로그램 종료시 크롬드라이버 프로세스도 종료. */
			System.out.println("on close request.");

			try {
				threadPool.shutdownNow();
				System.out.println("thread poll shutdown");
			} catch (Exception ex) {
			}

			try {
				WebDriverInstance.getInstance().getDriver().quit();
				System.out.println("driver quit");
			} catch (Exception ex) {
			}

			try {
				Platform.exit();
				System.out.println("exit platform");
			} catch (Exception ex) {
			}

			try {
				System.out.println("java exit.");
				System.exit(1);
			} catch (Exception ex) {
			}

		});

		/* Exception handler. 프로그램에서 catch하지 못한 예외를 받아 처리 */
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				Iterator<UncaughtErrorHandler> iterator = messageReceivers.iterator();
				while (iterator.hasNext())
					iterator.next().handle(t, e);
			}
		});

		
		stage.setTitle("Webdriver helper [ chrome version : " + AppValidation.getChromeVersion() + " ] ");
		BorderPane pane = FXMLLoader.load(WebdriverHelperApp.class.getResource("WebdriverHelperApp.fxml"));
		stage.setScene(new Scene(pane));
		stage.show();
	}

}
