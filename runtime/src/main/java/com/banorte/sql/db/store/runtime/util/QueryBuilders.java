package com.company.sql.db.store.runtime.util;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class QueryBuilders {

    private static final String SP_NAME_SANITIZER_REGEX = "[^a-zA-Z0-9_\\\\.\\[\\]]";
    private static final String SP_PARAMETER_SANITIZER = "\\W";

    private QueryBuilders() {}

    public static String createMsSqlQuery(String spName, Optional<Map<String, Object>> params) {
        return String.format("EXEC %s%s", sanitizeSpName(spName), params.map(QueryBuilders::formatParams).orElse(""));
    }

    private static String formatParams(Map<String, Object> params) {
        return params.keySet().stream()
                .map(key -> {
                    var sanitizedKey = sanitizeParam(key);
                    return String.format("@%s = :%s", sanitizedKey, sanitizedKey);
                }).collect(Collectors.joining(", ", " ", ""));
    }

    private static String sanitizeSpName(String input) {
        return input.replaceAll(SP_NAME_SANITIZER_REGEX, "");
    }

    private static String sanitizeParam(String input) {
        return input.replaceAll(SP_PARAMETER_SANITIZER, "");
    }
}
