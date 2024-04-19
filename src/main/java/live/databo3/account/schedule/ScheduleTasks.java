package live.databo3.account.schedule;

import live.databo3.account.error.ErrorCode;
import live.databo3.account.exception.CustomException;
import live.databo3.account.member.entity.States;
import live.databo3.account.member.repository.MemberRepository;
import live.databo3.account.member.repository.StatesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleTasks {

    private final MemberRepository memberRepository;
    private final StatesRepository statesRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void memberStateCheck() {
        log.info("스케쥴러 동작 : {}", LocalDateTime.now());
        States states = statesRepository.findById(3L).orElseThrow(() -> new CustomException(ErrorCode.STATE_NOT_FOUND));

        LocalDateTime cutoffDateTime = LocalDateTime.now().minusDays(30);

        //한 번에 바꾸는게 맞을지
        //0 이상이면 바꿀지
        int total = memberRepository.updateStatesByLastLoginTime(cutoffDateTime, states);

        log.info("휴면 계정 전환 인원 : {}", total);
    }

}
