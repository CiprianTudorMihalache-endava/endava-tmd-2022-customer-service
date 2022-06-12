package com.endava.tmd.customer.test.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstants {

    public static final String BUILD_VERSION = readVersionFromPom();
    public static final long INITIAL_DB_RECORDS = 1;

    private static String readVersionFromPom() {
        try {
            final var prefix = "    <version>";
            final var path = Paths.get("pom.xml");
            final var xmlLine = Files.lines(path).filter(line -> line.startsWith(prefix)).findFirst().get();
            return xmlLine.substring(prefix.length(), xmlLine.indexOf("</version>"));
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
