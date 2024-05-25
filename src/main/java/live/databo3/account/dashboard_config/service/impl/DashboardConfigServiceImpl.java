package live.databo3.account.dashboard_config.service.impl;

import live.databo3.account.dashboard_config.adaptor.SensorAdaptor;
import live.databo3.account.dashboard_config.dto.*;
import live.databo3.account.error.ErrorCode;
import live.databo3.account.exception.CustomException;
import live.databo3.account.member.entity.Member;
import live.databo3.account.member.repository.MemberRepository;
import live.databo3.account.dashboard_config.entity.DashboardConfig;
import live.databo3.account.dashboard_config.repository.DashboardConfigRepository;
import live.databo3.account.dashboard_config.service.DashboardConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardConfigServiceImpl implements DashboardConfigService {
    private final DashboardConfigRepository dashboardConfigRepository;
    private final MemberRepository memberRepository;
    private final SensorAdaptor sensorAdaptor;

    public List<DashboardConfigResponse> getDashboardConfig(String memberId) {
        List<DashboardConfigDto> list = dashboardConfigRepository.findAllByMember_MemberIdOrderBySequenceNumber(memberId);
        List<Long> recordIdList = new ArrayList<>();
        List<DashboardConfigResponse> response = new ArrayList<>();
        for (DashboardConfigDto dashboardConfigDto : list) {
            recordIdList.add(dashboardConfigDto.getRecordNumber());
        }
        Map<Long, SensorInfoDto> sensorInfoMap = sensorAdaptor.getSensorInfo(recordIdList).getBody();
        for (DashboardConfigDto dashboardConfigDto : list) {
            assert sensorInfoMap != null;
            SensorInfoDto sensorInfoDto = sensorInfoMap.get(dashboardConfigDto.getRecordNumber());
            response.add(DashboardConfigResponse.builder()
                            .configId(dashboardConfigDto.getConfigId())
                            .organization(sensorInfoDto.getOrganization())
                            .place(sensorInfoDto.getPlace())
                    .sensorType(sensorInfoDto.getSensorType())
                    .sensorSn(sensorInfoDto.getSensorSn())
                    .sequenceNumber(dashboardConfigDto.getSequenceNumber())
                    .chartType(dashboardConfigDto.getChartType()).build());
        }
        return response;
    }

    public void registerDashboardConfig(String memberId, RegisterDashboardConfigRequest request) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        dashboardConfigRepository.save(new DashboardConfig(null, member, request.getRecordNumber(), request.getSequenceNumber(), DashboardConfig.ChartType.valueOf(request.getChartType())));
    }

    @Transactional
    public void modifyDashboardConfig(List<ModifyDashboardConfigRequest> requests) {
        for (ModifyDashboardConfigRequest request : requests) {
            DashboardConfig dashboardConfig = dashboardConfigRepository.findById(request.getConfigId()).orElseThrow(() -> new CustomException(ErrorCode.DASHBOARD_NOT_FOUND));
            dashboardConfig.setSequenceNumber(request.getSequenceNumber());
        }
    }

    @Transactional
    public void deleteDashboardConfig(Long configId) {
        if (dashboardConfigRepository.existsById(configId)) {
            throw new CustomException(ErrorCode.DASHBOARD_NOT_FOUND);
        }
        dashboardConfigRepository.deleteById(configId);
    }
}
