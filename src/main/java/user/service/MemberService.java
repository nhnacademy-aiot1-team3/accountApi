package user.service;

import user.dto.LoginRequestDto;
import user.dto.LoginResponseDto;
import user.entity.Member;

public interface MemberService {

    Member getMemberIdAndPassword(LoginRequestDto loginRequestDto);

}
