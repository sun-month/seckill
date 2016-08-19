package org.seckill.exception;

/**
 * 秒杀重复提交异常
 * @author Administrator
 *
 */
public class RepeatKillException extends SeckillException { 
	
	private static final long serialVersionUID = -7588706798824576584L;

	public RepeatKillException(String message) {
		super(message);
	}

	public RepeatKillException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
}
