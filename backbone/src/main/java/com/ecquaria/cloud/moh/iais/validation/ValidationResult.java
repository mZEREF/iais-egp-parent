package com.ecquaria.cloud.moh.iais.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {
    @Getter @Setter private boolean hasErrors;
    private Map<String, String> errorMsg = new HashMap<>();

    public void addMessage(String key, String msg) {
        errorMsg.put(key, msg);
    }

    public Map<String, String> retrieveAll() {
        return errorMsg;
    }
}
