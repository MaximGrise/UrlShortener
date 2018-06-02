package com.maxim.us.converter;

public interface BaseConverter {

    public String encode(Long rawId);

    public Long decode(String shortenedId);
}
