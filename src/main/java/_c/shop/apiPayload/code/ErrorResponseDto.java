package _c.shop.apiPayload.code;

import _c.shop.apiPayload.code.status.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter

public class ErrorResponseDto extends ErrorReasonDTO {

    private ErrorResponseDto(HttpStatus httpStatus, String code, String message) {
        super(httpStatus, false, code, message);
    }

    public static ErrorResponseDto of(ErrorStatus errorStatus) {
        return new ErrorResponseDto(
                errorStatus.getHttpStatus(),
                errorStatus.getCode(),
                errorStatus.getMessage()
        );
    }

    public static ErrorResponseDto of(ErrorStatus errorStatus, String message) {
        return new ErrorResponseDto(
                errorStatus.getHttpStatus(),
                errorStatus.getCode(),
                errorStatus.getMessage() + " - " + message
        );
    }

    public static ErrorResponseDto of(ErrorStatus errorStatus, Exception e) {
        return new ErrorResponseDto(
                errorStatus.getHttpStatus(),
                errorStatus.getCode(),
                errorStatus.getMessage()
        );
    }
}
