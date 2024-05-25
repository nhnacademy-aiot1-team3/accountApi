package live.databo3.account.dashboard_config.entity;

import live.databo3.account.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "dashboard_configs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "config_id")
    private Long configId;

    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @Column(name = "record_number")
    private Long recordNumber;

    @Column(name = "sequence_number")
    private Long sequenceNumber;

    @Column(name = "chart_type")
    @Enumerated(EnumType.STRING)
    private ChartType chartType;

    public enum ChartType {
        REAL_TIME,
        ONE_HOUR,
        DAILY,
        WEEKLY
    }
}
