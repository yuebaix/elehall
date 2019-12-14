package com.geercode.elehall.autoconfigure.web.common;

import com.geercode.elehall.web.common.base.BaseCode;

public class SysResponseCode {
    public final int CODE_PREFIX_SYS;
    public final int CODE_PREFIX_SVC_MOD;
    public final BaseCode.Builder ARG_EMPTY;
    public final BaseCode.Builder ARG_ILLEGAL;
    public final BaseCode.Builder ARG_JSON_FAIL;
    public final BaseCode.Builder REQUEST_REPEAT;
    public final BaseCode.Builder DB_EXCEPTION;
    public final BaseCode.Builder LOGIN_FAIL;
    public final BaseCode.Builder USER_ILLEGAL;
    public final BaseCode.Builder SIGN_VERIFY_FAIL;
    public final BaseCode.Builder ERROR;

    SysResponseCode(int CODE_PREFIX_SYS,
                    int CODE_PREFIX_SVC_MOD,
                    BaseCode.Builder ARG_EMPTY,
                    BaseCode.Builder ARG_ILLEGAL,
                    BaseCode.Builder ARG_JSON_FAIL,
                    BaseCode.Builder REQUEST_REPEAT,
                    BaseCode.Builder DB_EXCEPTION,
                    BaseCode.Builder LOGIN_FAIL,
                    BaseCode.Builder USER_ILLEGAL,
                    BaseCode.Builder SIGN_VERIFY_FAIL,
                    BaseCode.Builder ERROR) {
        this.CODE_PREFIX_SYS = CODE_PREFIX_SYS;
        this.CODE_PREFIX_SVC_MOD = CODE_PREFIX_SVC_MOD;
        this.ARG_EMPTY = ARG_EMPTY;
        this.ARG_ILLEGAL = ARG_ILLEGAL;
        this.ARG_JSON_FAIL = ARG_JSON_FAIL;
        this.REQUEST_REPEAT = REQUEST_REPEAT;
        this.DB_EXCEPTION = DB_EXCEPTION;
        this.LOGIN_FAIL = LOGIN_FAIL;
        this.USER_ILLEGAL = USER_ILLEGAL;
        this.SIGN_VERIFY_FAIL = SIGN_VERIFY_FAIL;
        this.ERROR = ERROR;
    }
}
