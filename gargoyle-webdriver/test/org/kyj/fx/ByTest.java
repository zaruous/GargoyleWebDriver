/********************************
 *	프로젝트 : gargoyle-webdriver
 *	패키지   : org.kyj.fx
 *	작성일   : 2023. 2. 25.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package org.kyj.fx;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

/**
 * @author (zaruous@naver.com)
 *
 */
public class ByTest {
	@Test
	public void byMethods() {
		Method[] declaredMethods = By.class.getDeclaredMethods();
		for (Method m : declaredMethods) {
			if (!Modifier.isStatic(m.getModifiers()))
				continue;

			String mname = m.getName();
			System.out.println(mname);
		}
		Assert.assertTrue(true);
	}

}
