package _c.shop.global.apiPayload.exception;


import _c.shop.global.apiPayload.code.BaseErrorCode;

public class ExceptionHandler extends GeneralException {
    public ExceptionHandler(BaseErrorCode code) {
        super(code);
    }
}
