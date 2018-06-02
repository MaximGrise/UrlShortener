package com.maxim.us.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maxim.us.configuration.AppConfiguration;

@Component
public final class BaseConverterImpl implements BaseConverter {

    @Autowired
    private AppConfiguration config;

    public String encode(Long rawId) {
        if (rawId <= 0 || rawId > config.getMaxId()) {
            throw new IllegalArgumentException();
        }

        StringBuilder builder = new StringBuilder();

        while (rawId > 0) {
            builder.append(config.getCharacters().charAt((int) (rawId % config.getBase())));
            rawId /= config.getBase();
        }

        return builder.reverse().toString();
    }

    public Long decode(String shortenedId) {
        if (shortenedId.trim().length() < 1) {
            throw new IllegalArgumentException();
        }

        Long converted = 0L;
        for (int i = 0; i < shortenedId.length(); i++) {
            int position = config.getCharacters().indexOf(shortenedId.charAt(i));
            if (position < 0) {
                throw new IllegalArgumentException();
            }
            converted = converted * config.getBase() + position;
        }

        return converted;
    }
}
