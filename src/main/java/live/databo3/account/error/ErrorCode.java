package live.databo3.account.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //404
    MEMBER_NOT_FOUND(404, "조회한 멤버가 없습니다."),
    STATE_NOT_FOUND(404, "상태를 찾을 수 없습니다."),
    ROLE_NOT_FOUND(404, "역할을 찾을 수 없습니다.");

    private final int code;
    private final String message;
}
