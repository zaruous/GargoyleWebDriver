/********************************
 *	프로젝트 : gargoyle-webdriver
 *	패키지   : org.kyj.fx.gwd.util
 *	작성일   : 2023. 2. 26.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package org.kyj.fx;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.kyj.fx.gwd.util.AppValidation;

/**
 * @author (zaruous@naver.com)
 *
 */
public class AppValidationTest {

	/**
	 * Test method for
	 * {@link org.kyj.fx.gwd.util.AppValidation#getChromeVersion()}.
	 */
	@Test
	public final void testGetChromeVersion() {
		String chromeVersion = AppValidation.getChromeVersion();
		System.out.println("Chrome version: " + chromeVersion);
		assertNotEquals(chromeVersion, "");
		Assert.assertTrue(true);
	}

}
