package io.ll.warden.configuration;

/**
 * Creator: LordLambda
 * Date: 3/28/2015
 * Project: Warden
 * Usage: A convience method for Configuration Parsing
 */
public class Value<T> {

  private T value;

  public Value(T t) {
    value = t;
  }

  public T getValue() {
    return value;
  }
}
