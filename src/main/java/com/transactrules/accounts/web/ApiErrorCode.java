package com.transactrules.accounts.web;

public enum ApiErrorCode {
    ALREADY_EXISTS("AlreadyExists"),
    NO_SUCH_TYPE("NoSuchType");

    private String code;

    ApiErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
