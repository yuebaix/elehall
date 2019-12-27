package com.geercode.elehall.web;

import com.geercode.elehall.autoconfigure.web.common.SysResponseCode;
import com.geercode.elehall.common.AppBaseCode;
import com.geercode.elehall.dto.FooArg;
import com.geercode.elehall.dto.FooRet;
import com.geercode.elehall.web.common.base.BaseCode;
import com.geercode.elehall.web.common.base.BaseReq;
import com.geercode.elehall.web.common.base.BaseResp;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("testSwagger")
    @PostMapping("/testSwagger")
    public BaseResp<FooRet> testSwagger(@RequestBody @Validated BaseReq<FooArg> req) {
        FooArg fooArg = req.getCnt();
        System.out.println(fooArg);
        System.out.println(BaseReq.of(fooArg));
        FooRet ret = new FooRet().setId(fooArg.getId()).setName(fooArg.getName()).setNickname(fooArg.getName() + ":" + fooArg.getId());
        System.out.println(ret);
        return BaseResp.success(ret);
    }

    @GetMapping("/{id}")
    public BaseResp findById(@PathVariable long id) {
        return BaseResp.success();
    }
}
