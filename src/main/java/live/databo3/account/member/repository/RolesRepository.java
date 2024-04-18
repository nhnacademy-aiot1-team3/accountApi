package live.databo3.account.member.repository;

import live.databo3.account.member.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Roles JpaRepository interface
 * @author insub
 * @since 1.0.1
 */
public interface RolesRepository extends JpaRepository<Roles, Long> {
}
