package com.geercode.elehall.web;

import com.geercode.elehall.web.common.base.BaseCode;
import com.geercode.elehall.web.common.base.BaseResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/check")
    public BaseResp check() {
        String msg = BaseCode.getResponseCodeByCode(0).getMsg();
        System.out.println(msg);
        return BaseResp.success();
    }
}
