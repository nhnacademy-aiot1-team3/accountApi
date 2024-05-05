package live.databo3.account.organization.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    @Column
    private String organizationName;

    @Setter
    @Column
    private String gatewaySn;

    @Setter
    @Column
    private String controllerSn;

    @Builder
    public Organization(Integer organizationId, String organizationName, String gatewaySn, String controllerSn) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.gatewaySn = gatewaySn;
        this.controllerSn = controllerSn;
    }

}
