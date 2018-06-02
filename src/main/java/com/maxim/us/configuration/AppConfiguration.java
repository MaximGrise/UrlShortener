package com.maxim.us.configuration;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Value("${url.short.characters}")
    @NotBlank
    private String characters;

    @Value("${url.short.maxlength}")
    @NotBlank
    private long urlMaxLength;

    private int base = -1;

    /**
     * 3656158440062975 is equivalent to -> "0000000000", the last base 36 available
     * before busting to 11 letters.
     */
    private long maxId = -1;

    public String getCharacters() {
        return characters;
    }

    public int getBase() {
        // lazy init
        if (base < 0) {
            base = characters.length();
        }

        return base;
    }

    public long getUrlMaxLength() {
        return urlMaxLength;
    }

    public long getMaxId() {
        if (maxId < 0) {
            for (int i = 0; i < urlMaxLength; i++) {
                maxId += Math.pow(getBase(), i);

                if (maxId < 0) {
                    throw new IllegalArgumentException(
                            "Bad configuration, the maximum ID would be higher than MAX_LONG");
                }
            }
        }
        return maxId;
    }
}
