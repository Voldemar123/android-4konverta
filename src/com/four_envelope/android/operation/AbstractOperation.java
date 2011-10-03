package com.four_envelope.android.operation;

/**
 * Асинхронная операция и связанные с этим накладные функции.
 * 
 * @author alexander.ivanov
 * 
 * @param <Result>
 */
public abstract class AbstractOperation<Params, Result> {
	/**
	 * Приложение.
	 */
	UpdateListener context;

	/**
	 * Операция успешны выполнена.
	 */
	boolean complited;

	/**
	 * Выполняемая задача.
	 */
	AbstractTask<Params, Result> task;

	public AbstractOperation(UpdateListener context) {
		this.context = context;
		complited = false;
		task = null;
	}

	/**
	 * Возвращает истину если операция выполнена.
	 * 
	 * @return
	 */
	public boolean isComplited() {
		return complited;
	}

	/**
	 * Очищает результат объект после выполнения необходимых действий.
	 */
	public void clear() {
		complited = false;
		onClear();
	}

	/**
	 * Возвращает истину если операция выполняется.
	 */
	public boolean isInProgress() {
		return task != null;
	}

	/**
	 * Отмена операции.
	 */
	public void cancel() {
		if (task != null)
			task.cancel(true);
	}

	/**
	 * Выполнение операции.
	 * 
	 * @param hole
	 */
	public void execute(Params... params) {
		cancel();
		task = new AbstractTask<Params, Result>(context) {
			@Override
			protected void onPreExecute() {
				clear();
				super.onPreExecute();
			}

			@Override
			Result process(Params... params) throws LocalizedException {
				return AbstractOperation.this.process(params);
			}

			@Override
			void onSuccess(Result result) {
				complited = true;
				AbstractOperation.this.onSuccess(result);
			}

			@Override
			void onFinish() {
				task = null;
			}
		};
		task.execute(params);
	}

	/**
	 * Выполняет необходимую задачу в фоне.
	 * 
	 * @param params
	 * @return Должно возвращать не <code>null</code>, иначе
	 * @throws LocalizedException
	 */
	abstract Result process(Params... params) throws LocalizedException;

	/**
	 * Вызывается для сброса накопленных значений.
	 */
	abstract void onClear();

	/**
	 * Вызывается в случае успешного завершения операции.
	 */
	abstract void onSuccess(Result result);
}
