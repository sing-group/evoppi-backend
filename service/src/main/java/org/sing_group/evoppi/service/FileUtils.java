package org.sing_group.evoppi.service;

public class FileUtils {

  public static String sanitize(String string) {
    return string.replaceAll("[^a-zA-Z0-9\\._]+", "_");
  }
}
