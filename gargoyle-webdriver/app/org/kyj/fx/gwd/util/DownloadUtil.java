/********************************
 *	프로젝트 : gargoyle-webdriver
 *	패키지   : org.kyj.fx.gwd.util
 *	작성일   : 2023. 2. 26.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package org.kyj.fx.gwd.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author (zaruous@naver.com)
 *
 */
public class DownloadUtil {

	/**
	 * download file. <br/>
	 * 
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 26.
	 * @param srcList
	 */
	public static void download(List<String> srcList) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String format = simpleDateFormat.format(new Date());
		download(format, srcList);
	}

	/**
	 * download file. <br/>
	 * 
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 26.
	 * @param dirName
	 * @param srcList
	 */
	public static void download(String dirName, List<String> srcList) {
		File dir = new File(new File("download"), dirName);
		dir.mkdirs();
		download(dir, srcList);
	}

	/**
	 * download file. <br/>
	 * 
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 26.
	 * @param outdir
	 * @param srcList
	 */
	public static void download(File outdir, List<String> srcList) {

		Stream.iterate(0, a -> a + 1).limit(srcList.size()).filter(idx -> {
			return !srcList.get(idx).isEmpty();
		}).forEach(idx -> {
			String url = srcList.get(idx);

			try (InputStream in = new URL(url).openConnection().getInputStream()) {

				byte[] b = new byte[4096];
				int r = -1;
				try (FileOutputStream writer = new FileOutputStream(new File(outdir, idx + ".jpeg"))) {
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
}
