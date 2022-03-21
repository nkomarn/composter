package xyz.nkomarn.composter.protocol.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Property {
    @JsonProperty("name")
    public String Name;

    @JsonProperty("value")
    public String Value;

    @JsonProperty("signature")
    public String Signature;
}
