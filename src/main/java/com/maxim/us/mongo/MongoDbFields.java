package com.maxim.us.mongo;

public enum MongoDbFields {
    PseudoIdField("pseudo_id"), LongUrlField("long_url"), UrlCollection("urls"), UrlCounterField("url_counter"),CountersCollection("counters");

    private final String value;

    private MongoDbFields(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
