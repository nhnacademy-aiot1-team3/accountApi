package live.databo3.account.dashboard_config.repository;

import live.databo3.account.dashboard_config.dto.DashboardConfigDto;
import live.databo3.account.dashboard_config.entity.DashboardConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DashboardConfigRepository extends JpaRepository<DashboardConfig, Long> {
    List<DashboardConfigDto> findAllByMember_MemberIdOrderBySequenceNumber(String memberId);
}
