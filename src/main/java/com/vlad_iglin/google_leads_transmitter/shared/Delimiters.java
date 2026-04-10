package com.vlad_iglin.google_leads_transmitter.shared;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Delimiters {

    COMMA_DELIMITER(", "),
    DOT_DELIMITER(".");

    private final String delimiter;
}
