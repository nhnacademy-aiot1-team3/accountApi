package live.databo3.account.organization.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyOrgsRequest {
    private String organizationName;

    @Builder
    public ModifyOrgsRequest(String organizationName) {
        this.organizationName = organizationName;
    }
}
