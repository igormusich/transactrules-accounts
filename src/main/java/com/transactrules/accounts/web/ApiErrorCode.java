package com.transactrules.accounts.web;

public enum ApiErrorCode {
    ALREADY_EXISTS("name.duplicated", "Name must be unique"),
    NO_SUCH_TYPE("item.missing","Item with this name does not exist"),
    INVALID_IDENTIFIER("name.invalid","Name must be valid identifier"),
    INVALID_ENUM("enum.invalid","Value must match one of the predefined values"),
    EVALUATION_FAILED("evaluation.failed","Invalid expression");
    //private final String invalidIdentifierCode = "name.invalid";
    //private final String invalidIdentifier = "Name must be valid identifier";
    //private final String invalidEnumCode = "enum.invalid";
    //private final String invalidEnum = "Value must match one of the predefined values";
    //private final String duplicateIdentifierCode = "name.duplicated";
    //private final String duplicateIdentifier = "Name must be unique";
    //private final String missingIdentifierCode = "item.missing";
    //private final String missingIdentifier = "Item with this name does not exist";
    //private final String evaluationErrorCode = "evaluation.failed";

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
