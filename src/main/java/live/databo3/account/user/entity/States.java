package live.databo3.account.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * 상태 entity
 * { 0:WAIT, 1:ACTIVE, 2:PAUSE, 3:QUIT }
 * @author insub
 * @version 1.0.1
 */
@Getter
@Entity
@NoArgsConstructor
@Table(name = "states")
@ToString
public class States {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "state_id")
    private Long stateId;

    @Column(name = "state_name")
    @Enumerated(value = EnumType.STRING)
    private STATES stateName;

    public enum STATES {
        WAIT,
        ACTIVE,
        PAUSE,
        QUIT
    }

}
