package io.ll.warden.configuration;

import java.io.File;
import java.io.IOException;

/**
 * Creator: LordLambda
 * Date: 3/28/2015
 * Project: Warden
 * Usage: The base for config
 */
public abstract class Config {

  protected abstract void load(File file) throws IOException;

  public abstract String getFullName();

  public abstract String getPrettyName();

  protected abstract void onShutdown();
}
