/********************************
 *	프로젝트 : gargoyle-webdriver
 *	패키지   : org.kyj.fx.gwd
 *	작성일   : 2023. 2. 25.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package org.kyj.fx.gwd;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author (zaruous@naver.com)
 *
 */
public class ImageInfoDVO {
	private BooleanProperty check = new SimpleBooleanProperty();
	private StringProperty url = new SimpleStringProperty();

	public final StringProperty urlProperty() {
		return this.url;
	}

	public final String getUrl() {
		return this.urlProperty().get();
	}

	public final void setUrl(final String url) {
		this.urlProperty().set(url);
	}

	public final BooleanProperty checkProperty() {
		return this.check;
	}

	public final boolean isCheck() {
		return this.checkProperty().get();
	}

	public final void setCheck(final boolean check) {
		this.checkProperty().set(check);
	}

}
