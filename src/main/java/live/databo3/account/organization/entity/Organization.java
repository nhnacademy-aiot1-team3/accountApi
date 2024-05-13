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
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "organization_id")
    private Integer organizationId;

    @Setter
    @Column (name = "organization_name")
    private String organizationName;

    @Setter
    @Column (name = "gateway_sn")
    private String gatewaySn;

    @Setter
    @Column (name = "controller_sn")
    private String controllerSn;

    @Builder
    public Organization(Integer organizationId, String organizationName, String gatewaySn, String controllerSn) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.gatewaySn = gatewaySn;
        this.controllerSn = controllerSn;
    }

}
