package com.four_envelope.android.operation;

/**
 * Объекты данного класса содержат либо результат выполнения операции, либо
 * ошибку, сгенерированную в процессе выполнения.
 * 
 * @author alexander.ivanov
 * 
 * @param <T>
 */
public class ResultOrException<T> {
	public final T result;
	public final LocalizedException exception;

	public ResultOrException(T result) {
		this.result = result;
		this.exception = null;
	}

	public ResultOrException(LocalizedException exception) {
		this.result = null;
		this.exception = exception;
	}
}
