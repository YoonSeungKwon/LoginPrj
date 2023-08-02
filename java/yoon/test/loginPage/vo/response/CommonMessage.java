package yoon.test.loginPage.vo.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CommonMessage {

    private HttpStatus code;

    private String message;

    private Object data;

    public CommonMessage(){
        this.code = HttpStatus.BAD_REQUEST;
        this.message = null;
        this.data = null;
    }

}
