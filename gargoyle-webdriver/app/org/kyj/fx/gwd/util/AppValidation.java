/********************************
 *	프로젝트 : gargoyle-webdriver
 *	패키지   : org.kyj.fx.gwd.util
 *	작성일   : 2023. 2. 26.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package org.kyj.fx.gwd.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author (zaruous@naver.com)
 *
 */
public class AppValidation {

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 26.
	 * @return
	 */
	public static String getChromeVersion() {
		String version = "";
		try {
			String command = "reg query \"HKEY_CURRENT_USER\\Software\\Google\\Chrome\\BLBeacon\" /v version";
			Process process = Runtime.getRuntime().exec(command);

			String output = ValueUtil.toString(process.getInputStream(), "UTF-8");
			Pattern pattern = Pattern.compile("REG_SZ\\s+(.*)");
			Matcher matcher = pattern.matcher(output);
			if (matcher.find()) {
				version = matcher.group(1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return version;

	}
}
