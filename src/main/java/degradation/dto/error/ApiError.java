package degradation.dto.error;

import degradation.enums.ErrorCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiError {
    private HttpStatus status;
    private ErrorCode errorCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
    private String message;
    private String debugMessage;
    private List<ApiSubError> subErrors;
}
