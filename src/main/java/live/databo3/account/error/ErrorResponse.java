package live.databo3.account.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ErrorResponse<T, S> {

    private T headers;
    private S body;

}
