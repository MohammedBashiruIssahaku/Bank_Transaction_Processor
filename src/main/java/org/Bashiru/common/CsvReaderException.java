package org.Bashiru.common;

public class CsvReaderException extends RuntimeException {
  public CsvReaderException(String fileName, String message) {
    super("Failed to parse file %s: %s".formatted(fileName, message));
  }
}