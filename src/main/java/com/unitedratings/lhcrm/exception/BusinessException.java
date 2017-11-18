package com.unitedratings.lhcrm.exception;

/**
 * @author wangyongxin
 */
public class BusinessException extends Exception{

    private String errorCode;

    public BusinessException(String errorCode,String errorMessage){
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public BusinessException(String errorCode,String errorMessage,Throwable cause){
        super(errorMessage,cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
