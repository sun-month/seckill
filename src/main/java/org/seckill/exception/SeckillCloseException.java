package org.seckill.exception;

/**
 * 秒杀关闭异常
 * @author Administrator
 *
 */
public class SeckillCloseException extends SeckillException {

	private static final long serialVersionUID = 3053493947005262396L;

	public SeckillCloseException(String message) {
		super(message);
	}

	public SeckillCloseException(String message, Throwable cause) {
		super(message, cause);
	}

}
