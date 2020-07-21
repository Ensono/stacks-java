package com.xxAMIDOxx.xxSTACKSxx.exception;

/**
 * Exception class When no menu found.
 *
 * @author ArathyKrishna
 */
public class MenuNotFoundException extends RuntimeException {

  public MenuNotFoundException(String id) {
    super(String.format("Menu with Id %s not found", id));
  }
}
