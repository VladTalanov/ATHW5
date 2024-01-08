package org.max.home.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Code",
        "Message",
        "Reference"
})

public class Error {

    @JsonProperty("Code")
    private String code;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Reference")
    private String reference;

    @JsonProperty("Code")
    public String getCode() {
        return code;
    }

    @JsonProperty("Code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("Message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("Message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("Reference")
    public String getReference() {
        return reference;
    }

    @JsonProperty("Reference")
    public void setReference(String reference) {
        this.reference = reference;
    }

}

