package degradation.exception;

import degradation.dto.error.ApiError;
import degradation.enums.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


@RestControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;
    private final Environment environment;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handlerClientError(Exception e){
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handlerPageNotFound(Exception e){
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerServerError(Exception e){
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            AuthenticationException e
    ) throws IOException, ServletException {
        ApiError error = prepareError(e, HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED);

        String body = objectMapper.writeValueAsString(error);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        httpServletResponse.getWriter().write(body);
    }

    private ApiError prepareError(Throwable throwable, HttpStatus status, ErrorCode errorCode) {
        ApiError error = new ApiError();
        error.setStatus(status);
        error.setErrorCode(errorCode);
        error.setMessage(throwable.getLocalizedMessage());

        writeStackTrace(error, throwable);
        return error;
    }

    private void writeStackTrace(ApiError error, Throwable throwable) {
        boolean development = Arrays.asList(environment.getActiveProfiles()).contains("development");
        if (development && false) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter, true);
            throwable.printStackTrace(printWriter);

            error.setDebugMessage(stringWriter.getBuffer().toString());
        }
    }
}
