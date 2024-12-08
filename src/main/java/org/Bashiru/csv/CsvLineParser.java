package org.Bashiru.csv;

import java.util.Optional;

public interface CsvLineParser<T> {
    Optional<T> parseCsvLine(String line, String expectedCsvHeader);
}
