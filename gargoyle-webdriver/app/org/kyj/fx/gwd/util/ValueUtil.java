/********************************
 *	프로젝트 : gargoyle-webdriver
 *	패키지   : org.kyj.fx.gwd.util
 *	작성일   : 2023. 2. 26.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package org.kyj.fx.gwd.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author (zaruous@naver.com)
 *
 */
public class ValueUtil {
	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 26.
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String toString(InputStream is, String encoding) throws IOException {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding))) {
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		}
		return sb.toString();
	}
}
