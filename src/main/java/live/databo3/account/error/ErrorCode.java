package live.databo3.account.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //400
    CERTIFICATION_NUMBER_DOES_NOT_MATCH(400, "입력하신 인증 번호가 일치하지 않습니다."),
    METHOD_ARGUMENT_ERROR(400,"잘못된 입력으로 인한 바인딩 에러입니다."),
    ORGANIZAATION_DUPLICATE_NAME(400, "이미 사용 중인 이름입니다"),
    ORGANIZATION_ALREADY_EXIST(400,"이미 존재하는 조직입니다"),
    MEMBERORG_ALREADY_EXIST(400, "이미 신청되었습니다" ),

    //404
    MEMBER_NOT_FOUND(404, "조회한 멤버가 없습니다."),
    STATE_NOT_FOUND(404, "상태를 찾을 수 없습니다."),
    ROLE_NOT_FOUND(404, "역할을 찾을 수 없습니다."),
    EMAIL_KEY_EXPIRE(404,"이미 만료된 코드 입니다."),
    ORGANIZATION_NOT_FOUND(404, "조회한 조직이 없습니다"),
    MEMBERORG_NOT_FOUND(404, "해당 조직에 멤버가 없습니다");
    private final int code;
    private final String message;
}
