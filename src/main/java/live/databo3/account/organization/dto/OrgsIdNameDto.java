package live.databo3.account.organization.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrgsIdNameDto {
    private Integer organizationId;
    private String organizationName;

    @Builder
    public OrgsIdNameDto(Integer organizationId, String organizationName) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
    }
}
