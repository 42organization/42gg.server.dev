package io.pp.arcade.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "hello World";
    }

    @GetMapping("/test2")
    public String test2(){
        return "hello World2";
    }
}
