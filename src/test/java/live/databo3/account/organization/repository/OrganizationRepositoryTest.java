package live.databo3.account.organization.repository;

import live.databo3.account.organization.entity.Organization;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@Slf4j
@DataJpaTest
@TestMethodOrder(MethodOrderer.class)
class OrganizationRepositoryTest {

    @Autowired
    private OrganizationRepository organizationRepository;

    @BeforeEach
    void setUp(){
        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gatewaySn")
                .controllerSn("controllerSn")
                .build();
        organizationRepository.save(organization);
    }

    @Test
    @DisplayName("조직 이름으로 찾기")
    void findByOrganizationName() {
        Optional<Organization> organizationOptional = organizationRepository.findByOrganizationName("nhn 김해");
        log.info("{}", organizationOptional);
        Assertions.assertNotNull(organizationOptional.get());
        Assertions.assertEquals(1, organizationOptional.get().getOrganizationId());
        Assertions.assertEquals("nhn 김해", organizationOptional.get().getOrganizationName());
        Assertions.assertEquals("gatewaySn", organizationOptional.get().getGatewaySn());
        Assertions.assertEquals("controllerSn", organizationOptional.get().getControllerSn());
    }
}
