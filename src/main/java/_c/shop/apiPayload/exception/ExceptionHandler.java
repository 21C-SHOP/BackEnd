package _c.shop.apiPayload.exception;


import _c.shop.apiPayload.code.BaseErrorCode;

public class ExceptionHandler extends GeneralException {
    public ExceptionHandler(BaseErrorCode code) {
        super(code);
    }
}
