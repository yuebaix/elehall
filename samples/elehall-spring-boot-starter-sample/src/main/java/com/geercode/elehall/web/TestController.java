package com.geercode.elehall.web;

import com.geercode.elehall.autoconfigure.web.common.SysResponseCode;
import com.geercode.elehall.common.AppBaseCode;
import com.geercode.elehall.web.common.base.BaseCode;
import com.geercode.elehall.web.common.base.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private SysResponseCode sysResponseCode;

    @GetMapping("/check")
    public BaseResp check() {
        String msg = BaseCode.getResponseCodeByCode(0).getMsg();
        return BaseResp.custom(sysResponseCode.ARG_EMPTY, null);
    }

    @GetMapping("/appCheck")
    public BaseResp appCheck() {
        return BaseResp.custom(AppBaseCode.APP_ERROR, null);
    }
}
