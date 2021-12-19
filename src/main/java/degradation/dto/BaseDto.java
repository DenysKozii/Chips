package degradation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDto {
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private Long id;
}
