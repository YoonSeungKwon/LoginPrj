package yoon.test.loginPage.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yoon.test.loginPage.vo.response.CommonMessage;

import java.nio.charset.Charset;

@RestController
@RequestMapping("/api/v1/test")
public class NetworkTestController {

    @GetMapping("/")
    public ResponseEntity<CommonMessage> test(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "JSON", Charset.forName("UTF-8")));

        CommonMessage commonMessage = new CommonMessage();
        commonMessage.setCode(HttpStatus.OK);
        commonMessage.setMessage("정상 작동중");
        commonMessage.setData("Hello World");

        System.out.println("Connected");

        return new ResponseEntity<>(commonMessage, headers, HttpStatus.OK);
    }
}
