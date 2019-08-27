package com.tony.billing.exceptions;

/**
 * @author jiangwenjie 2019-02-02
 */
public class BaseBusinessException extends RuntimeException {
    public BaseBusinessException() {
        super();
    }

    public BaseBusinessException(String message) {
        super(message);
    }

    public BaseBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseBusinessException(Throwable cause) {
        super(cause);
    }

    protected BaseBusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
