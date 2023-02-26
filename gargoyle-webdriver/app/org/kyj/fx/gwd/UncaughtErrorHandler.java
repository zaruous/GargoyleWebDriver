/********************************
 *	프로젝트 : gargoyle-webdriver
 *	패키지   : org.kyj.fx.gwd
 *	작성일   : 2023. 2. 25.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package org.kyj.fx.gwd;

/**
 * @author (zaruous@naver.com)
 *
 */
public interface UncaughtErrorHandler {

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 25. 
	 * @param t
	 * @param e
	 */
	public void handle(Thread t, Throwable e);
}
