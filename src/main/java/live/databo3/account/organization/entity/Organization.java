package live.databo3.account.organization.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "organizations")
public class Organization {
    @Id
    @GeneratedValue
    @Column
    private Integer organizationId;

    @Column
    private String organizationName;

    @Column
    private String gatewaySn;

    @Column
    private String controllerSn;

    @Builder
    public Organization(Integer organizationId, String organizationName, String gatewaySn, String controllerSn) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.gatewaySn = gatewaySn;
        this.controllerSn = controllerSn;
    }

    public void change(Integer organizationId, String organizationName, String gatewaySn, String controllerSn) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.gatewaySn = gatewaySn;
        this.controllerSn = controllerSn;
    }
}
