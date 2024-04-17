package com.hyfata.autoclicker.locale;

import java.util.LinkedHashMap;

public class Languages {
    private final LinkedHashMap<String, String> languages = new LinkedHashMap<>();

    public Languages() {
        languages.put("한국어", "ko.json");
        languages.put("English", "en.json");
    }

    public LinkedHashMap<String, String> getLanguages() {
        return languages;
    }

    public String[] getLanguageNames() {
        return languages.keySet().toArray(new String[0]);
    }
}
