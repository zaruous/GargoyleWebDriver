/********************************
 *	프로젝트 : gargoyle-chromedriver
 *	패키지   : com.kyj.gargoyle.chromedriver.util
 *	작성일   : 2019. 9. 29.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.gargoyle.chromedriver.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 
 * 아래 코드로 다운로드 작동 안됨.
 * @author KYJ (callakrsos@naver.com)
 *
 */
@Deprecated
public class DownloadFile implements Runnable {

	private String fileName;
	private String url;

	public DownloadFile(String fileName, String url) {
		super();
		this.fileName = fileName;
		this.url = url;
	}

	static {
		final TrustManager[] trustAllCertificates = new TrustManager[] { new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null; // Not relevant.
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
				// Do nothing. Just allow them all.
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
				// Do nothing. Just allow them all.
			}
		} };

		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCertificates, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
		} catch (GeneralSecurityException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 29.
	 * @throws Exception
	 */
	protected void download() throws Exception {
//
//		try{
//		    java.net.URL downLink = new java.net.URL(this.url);
//		    java.nio.channels.ReadableByteChannel rbc = java.nio.channels.Channels.newChannel(downLink.openStream());
//		    java.io.FileOutputStream outputStream = new java.io.FileOutputStream("테스트.mp4");
//		    outputStream.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
//		                 
//		}catch(java.io.IOException e){
//		        e.printStackTrace();
//		}


	
			
		URL u = new URL(this.url);
		int BUFFER = 8096;
		byte[] b = new byte[BUFFER];
		File file = new File(this.fileName);
		URLConnection openConnection = u.openConnection();
		openConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
		openConnection.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
		openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
//		openConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
		openConnection.setRequestProperty("Connection", "keep-alive");

		
		
		openConnection.connect();
		try (InputStream openStream = u.openStream()) {
			if (!file.exists())
				file.createNewFile();
			try (FileOutputStream out = new FileOutputStream(file, false)) {
				int read = -1;
				System.out.println("start download :: " + this.fileName);
				while ((read = openStream.read(b)) != -1) {
					out.write(b, 0, read);
				}
				System.out.println("end download :: " + this.fileName);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		DownloadFile d = new DownloadFile("test",
				"https://files0.filesaveservice.club/redirect.php?path\\u003d%2ffiles%2f0%2fnew%2fid_38854.mp4");
		d.download();
	}

	@Override
	public void run() {
		try {
			download();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
