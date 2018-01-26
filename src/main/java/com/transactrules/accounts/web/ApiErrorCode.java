package com.transactrules.accounts.web;

public enum ApiErrorCode {
    ALREADY_EXISTS("name.duplicated", "Name must be unique"),
    NO_SUCH_TYPE("item.missing","Item with this name does not exist"),
    INVALID_IDENTIFIER("name.invalid","Name must be valid identifier"),
    INVALID_ENUM("enum.invalid","Value must match one of the predefined values"),
    EVALUATION_FAILED("evaluation.failed","Invalid expression"),
    REQUIRED("name.required","Required property"),
    HAS_DUPLICATE_START_DATE("dateType.isStartDateDuplicated","Multiple dates defined as Start Date");

    private String code;
    private String description;

    ApiErrorCode(String code, String description) {

        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
