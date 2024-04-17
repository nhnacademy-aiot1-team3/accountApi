package live.databo3.account.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * 역할 entity
 * { 0:ROLE_ADMIN, 1:ROLE_OWNER, 2:ROLE_VIEWER }
 * @author insub
 * @version 1.0.1
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "roles")
@ToString
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private ROLES roleName;

    public enum ROLES {
        ROLE_ADMIN,
        ROLE_OWNER,
        ROLE_VIEWER

    }

}
