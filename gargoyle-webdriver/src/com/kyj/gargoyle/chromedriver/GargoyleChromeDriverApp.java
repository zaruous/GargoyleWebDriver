/********************************
 *	프로젝트 : gargoyle-chromedriver
 *	패키지   : com.kyj.gargoyle.chromedriver
 *	작성일   : 2019. 9. 28.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.gargoyle.chromedriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

import org.openqa.selenium.ImmutableCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


/**
 * 
 * explorer driver : https://github.com/SeleniumHQ/selenium/wiki/InternetExplorerDriver <br/>
 * ie11  iedriver download site : https://selenium-release.storage.googleapis.com/index.html <br/>
 * 
 * HKEY_CURRENT_USER\Software\Microsoft\Internet Explorer\Main 에 새 키 데이터를 추가하고 TabProcGrowth 값은 0을 넣어주어야함.  
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class GargoyleChromeDriverApp {

	public JsonObject json = new JsonObject();

	private static Object lock = new Object();

	public void getObject(Consumer<JsonObject> accept) {

		synchronized (lock) {
			accept.accept(json);
		}

	}

	private enum DriverType {
		Chrome, Exporer
	};

	private DriverType driverType = DriverType.Chrome;

	public DriverType getDriverType() {
		return driverType;
	}

	public GargoyleChromeDriverApp() {

	}

	public GargoyleChromeDriverApp(String type) {
		this(DriverType.valueOf(type));
	}

	public GargoyleChromeDriverApp(DriverType type) {
		super();
		this.driverType = type;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 28.
	 * @param args
	 * @throws InterruptedException
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws InterruptedException, FileNotFoundException {

		GargoyleChromeDriverApp gargoyleChromeDriverApp = new GargoyleChromeDriverApp(DriverType.Exporer);

		System.out.println("##print arguments");

		for (String arg : args) {
			System.out.println(arg);
		}

		if (args.length != 0)
			gargoyleChromeDriverApp.url = args[0];
		gargoyleChromeDriverApp.execute();

	}

	public WebDriver rootDriver;
	private String url;
	private List<WebDriver> management = new ArrayList<>();

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 22. 
	 * @throws InterruptedException
	 * @throws FileNotFoundException
	 */
	private void execute() throws InterruptedException, FileNotFoundException {
		execute(this.driverType);
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 22. 
	 * @param type
	 * @throws InterruptedException
	 * @throws FileNotFoundException
	 */
	private void execute(final DriverType type) throws InterruptedException, FileNotFoundException {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {

				if (newFixedThreadPool.isShutdown())
					newFixedThreadPool.shutdown();

				management.forEach(d -> {
					try {
						d.close();
						d.quit();
					} catch (Exception e) {
					}
				});

			}
		}));
		System.out.println("start driver ");
		WebDriver driver = createDriver(type);
		this.rootDriver = driver;
		if (this.url != null)
			driver.get(this.url);

		Thread exitMonitor = new Thread() {

			@Override
			public void run() {

				try (Scanner scan = new Scanner(System.in)) {
					String temp = null;
					while ((temp = scan.nextLine()) != null) {
						/* 프로그램 종료 */
						if ("exit".contentEquals(temp)) {
							driver.quit();
							newFixedThreadPool.shutdown();
							return;
						}
						/* 프로세스에 저장된 url을 기반으로 동영상 목록을 추출하는 프로세스 시작. */
						else if ("process".contentEquals(temp)) {
							driver.quit();
							GargoyleChromeDriverApp gargoyleChromeDriverApp = new GargoyleChromeDriverApp();
							gargoyleChromeDriverApp.url = GargoyleChromeDriverApp.this.url;
							gargoyleChromeDriverApp.execute(type);
							scan.close();
							break;
						}
						else if(temp.startsWith("url "))
						{
							try {
								String[] split = temp.split(" ");
								String visiteUrl = split[1];
								rootDriver.get(visiteUrl);	
								System.out.println("visite url : " + visiteUrl);
							}catch(Exception e)
							{
								e.printStackTrace();
								// ignore.
							}
						}
						
						else if("pageSource".contentEquals(temp))
						{
							System.out.println(rootDriver.getPageSource());
						}
						
						/* 파일 다운로드 */
						else if ("downloadall".contentEquals(temp)) {
							json.entrySet().parallelStream().forEach(entry -> {
								String key = entry.getKey();
								JsonElement value = entry.getValue();
								String url = value.toString();
								download(key, url);

								System.out.println(key + " complete.");
							});
						}

						/* 현재 프로세스에 저장된 url 을 출력 */
						else if ("print".equals(temp)) {
							Gson gson = new GsonBuilder().setPrettyPrinting().create();
							System.out.println(url);
							System.out.println(gson.toJson(json));
						}
						
						/* 저장된 url 정보를 로드 */
						else if ("loadconfig".contentEquals(temp)) {
							try {
								List<String> readLines = Files.readLines(new File("download-url.json"), StandardCharsets.UTF_8);
								Optional<String> reduce = readLines.stream().reduce((str1, str2) -> str1.concat(str2));
								if (reduce.isPresent()) {
									String string = reduce.get();
									json = new Gson().fromJson(string, JsonObject.class);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						/* 저장된 url을 출력 */
						else if ("printconfig".contentEquals(temp)) {
							try {
								List<String> readLines = Files.readLines(new File("download-url.json"), StandardCharsets.UTF_8);
								Optional<String> reduce = readLines.stream().reduce((str1, str2) -> str1.concat(str2));
								if (reduce.isPresent()) {
									String string = reduce.get();
									System.out.println(string);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						/* 저장된 url을 파일에 저장 */
						else if ("writeconfig".contentEquals(temp)) {

							try {
								Files.write(json.toString().getBytes("utf-8"), new File("download-url.json"));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						/* 그외에는 url이라고 인식. */
						else {
							url = temp;
							System.out.println("update url " + url);
						}
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}

		};

		exitMonitor.setDaemon(true);
		exitMonitor.start();

		Thread applicationThread = new ApplicationThread(this, driver);
		applicationThread.start();

		exitMonitor.join();

	}

	ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(5, new ThreadFactory() {

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setDaemon(true);
			t.setName("download-thread");
			return t;
		}
	});

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 28.
	 * @param key
	 * @param url
	 */
	private void download(String key, String url) {

		// DownloadFile downloadFile = new DownloadFile(key, url);
		// newFixedThreadPool.submit(downloadFile);

		StringBuffer sb = new StringBuffer();

		sb.append(" var link = document.createElement(\"a\"); ");
		sb.append(" link.download = '" + key + "'; ");
		sb.append(" link.href = " + url + ";");
		sb.append("  link.click(); ");

		executeScript(rootDriver, sb.toString());
		try {
			File file = new File("download-histroy.log");
			if (!file.exists())
				file.createNewFile();
			try (FileWriter fileWriter = new FileWriter(file, true)) {
				fileWriter.write("downlaod ::: " + key + " " + url + "\n");
			} catch (IOException e) {
				/* N/A */
			}
		} catch (Exception e) {
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 28.
	 * @return
	 * @throws FileNotFoundException
	 */
	public WebDriver createDriver(DriverType type) throws FileNotFoundException {
		WebDriver driver;
		switch (type) {
		case Chrome:
			// new GargoyleSSLVertifier().setup();
			File driverFile = new File("lib/chrome/_77", "chromedriver.exe");
			if (!driverFile.exists()) {
				System.err.println("chromedriver.exe does not eixsts.");
				throw new FileNotFoundException("Driver not found. " + driverFile);
			}

			System.setProperty("webdriver.chrome.driver", driverFile.getAbsolutePath());

			ChromeOptions options = new ChromeOptions();
			options.addArguments("headless");
			options.addArguments("--headless");

			driver = new ChromeDriver(options);
			management.add(driver);
			break;
		case Exporer:
			driverFile = new File("C:\\Users\\calla\\git\\GargoyleWebDriver\\gargoyle-webdriver\\lib\\explorer\\3.9\\x32\\IEDriverServer.exe");
			if (!driverFile.exists()) {
				System.err.println("chromedriver.exe does not eixsts.");
				throw new FileNotFoundException("Driver not found. " + driverFile);
			}
			System.setProperty("webdriver.ie.driver", driverFile.getAbsolutePath());
			
//			InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
//		    true
			
			
			
			HashMap<String, Object> capabilities = new HashMap<String,Object>();
			capabilities.put( InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
				    true);
//			capabilities.put( InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION,
//				    true);
//			capabilities.put( InternetExplorerDriver.FORCE_CREATE_PROCESS,
//				    true);
//			capabilities.put( InternetExplorerDriver.IGNORE_ZOOM_SETTING,
//				    true);
			
			
			ImmutableCapabilities c = new ImmutableCapabilities(capabilities);
			
			
			InternetExplorerDriver iedriver = new InternetExplorerDriver(c);
			driver = iedriver;
			driver.get("https://www.google.com");
			
			

			management.add(driver);
			break;
		default:
			throw new RuntimeException("can't found suitable driver.");
		}

		return driver;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 28.
	 * @param driver
	 */
	public void close(WebDriver driver) {
		driver.close();
		driver.quit();
		management.remove(driver);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 28.
	 * @param driver
	 * @return
	 */
	public JavascriptExecutor getJs(WebDriver driver) {
		if (driver instanceof JavascriptExecutor) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			return js;
		}
		return null;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 28.
	 * @param driver
	 * @param script
	 * @param args
	 * @return
	 */
	public Object executeScript(WebDriver driver, String script, Object... args) {
		return getJs(driver).executeScript(script, args);
	}

}
