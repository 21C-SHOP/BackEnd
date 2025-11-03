package _c.shop.global.apiPayload.code.status;

import _c.shop.global.apiPayload.code.BaseErrorCode;
import _c.shop.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    _USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4000", "사용자를 찾을 수 없습니다."),
    _USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER4001", "이미 존재하는 사용자입니다."),

    _ACCESSTOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AT4000", "액세스 토큰이 존재하지 않습니다."),

    _REFRESHTOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "RT4000", "리프레시 토큰이 유효하지 않습니다."),
    _REFRESHTOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "RT4001", "리프레시 토큰이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
