package xyz.nkomarn.composter.protocol.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EncryptionResponseObject {
    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("properties")
    public Property[] properties;
}
