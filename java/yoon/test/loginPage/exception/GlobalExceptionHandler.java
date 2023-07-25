package yoon.test.loginPage.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.ClientAuthorizationRequiredException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yoon.test.loginPage.enums.ErrorCode;
import yoon.test.loginPage.vo.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> LoginRegisterValidation(MethodArgumentNotValidException e){
        ErrorResponse response;
        BindingResult bindingResult = e.getBindingResult();
        String code = bindingResult.getAllErrors().get(0).getDefaultMessage();
        if(code.equals(ErrorCode.EMAIL_NOT_FORMAT.getCode())){
            response = new ErrorResponse(code , ErrorCode.EMAIL_NOT_FORMAT.getMessage());
        }
        else if (code.equals(ErrorCode.EMAIL_BLANK.getCode())) {
            response = new ErrorResponse(code , ErrorCode.EMAIL_BLANK.getMessage());
        }
        else if (code.equals(ErrorCode.PASSWORD_BLANK.getCode())) {
            response = new ErrorResponse(code , ErrorCode.PASSWORD_BLANK.getMessage());
        }
        else if (code.equals(ErrorCode.USERNAME_BLANK.getCode())) {
            response = new ErrorResponse(code , ErrorCode.USERNAME_BLANK.getMessage());
        }
        else{
            response = new ErrorResponse(code, null);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    protected ResponseEntity<ErrorResponse> EmailErrorHandle(){
        ErrorCode error = ErrorCode.EMAIL_NOT_FOUND;
        ErrorResponse response = new ErrorResponse(error.getCode(), error.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler({BadCredentialsException.class})
    protected ResponseEntity<ErrorResponse> PasswordErrorHandle(){
        ErrorCode error = ErrorCode.PASSWORD_NOT_FOUND;
        ErrorResponse response = new ErrorResponse(error.getCode(), error.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

}
