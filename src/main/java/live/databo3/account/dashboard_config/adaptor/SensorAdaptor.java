package live.databo3.account.dashboard_config.adaptor;

import live.databo3.account.dashboard_config.dto.SensorInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "sensor-service")
public interface SensorAdaptor {
    @GetMapping("/api/sensor/sensorInfo")
    ResponseEntity<Map<Long, SensorInfoDto>> getSensorInfo(@RequestParam List<Long> id);
}
