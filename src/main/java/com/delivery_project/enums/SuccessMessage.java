package com.delivery_project.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {

    DELETE(" Delete Complete"),
    CREATE(" Create Complete"),
    UPDATE(" Update Complete");

    private final String message;

}
