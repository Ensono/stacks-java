package com.xxAMIDOxx.xxSTACKSxx.menu.exception;

public enum ExceptionCode {

    VALIDATION_ERROR(400),

    MENU_ALREADY_EXISTS(10409),
    MENU_DOES_NOT_EXIST(10404),

    CATEGORY_ALREADY_EXISTS(11409),
    CATEGORY_DOES_NOT_EXIST(11404),

    MENUITEMALREADY_EXISTS(12409),
    MENUITEMDOESNOT_EXIST(12404);

    private final int code;

    ExceptionCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
