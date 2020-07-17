package com.xxAMIDOxx.xxSTACKSxx.exception;

public enum ExceptionCode {

    VALIDATION_ERROR(100),

    MENU_ALREADY_EXISTS(10409),
    MENU_DOES_NOT_EXIST(10404),

    CATEGORY_ALREADY_EXISTS(11409),
    CATEGORY_DOES_NOT_EXIST(11404),

    MENUITEMALREADY_EXISTS(12409),
    MENUITEMDOESNOT_EXIST(12404),
    MENU_ITEM_PRICE_MUST_NOT_BE_ZERO(12500);

    private final int code;

    ExceptionCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
