package com.four_envelope.android.operation;

import com.four_envelope.android.FourEnvelopeApplication;

/**
 * Исключение, генерируемое приложением, содержащие ресурс с описанием ошибки.
 * 
 * @author alexander.ivanov
 * 
 */
public class LocalizedException extends Exception {

	private static final long serialVersionUID = 511553950546209386L;

	/**
	 * Идентификатор ресурса с описанием ошибки.
	 */
	private final int resourceID;

	/**
	 * Дополнительные данные.
	 */
	private final String extra;

	public LocalizedException(int resourceID, String extra) {
		this.resourceID = resourceID;
		this.extra = extra;
	}

	public LocalizedException(int resourceID) {
		this(resourceID, null);
	}

	/**
	 * Возвращает текст ошибки.
	 * @return
	 */
	public String getString() {
		if (extra == null)
			return FourEnvelopeApplication.getContext().getString(resourceID);
		else
			return FourEnvelopeApplication.getContext().getString(resourceID, extra);
	}
}