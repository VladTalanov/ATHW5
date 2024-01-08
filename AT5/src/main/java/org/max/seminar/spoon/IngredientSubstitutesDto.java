package org.max.seminar.spoon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "ingredient",
        "substitutes",
        "message"
})
@Generated("jsonschema2pojo")
public class IngredientSubstitutesDto {

    @JsonProperty("status")
    private String status;
    @JsonProperty("ingredient")
    private String ingredient;
    @JsonProperty("substitutes")
    private List<String> substitutes;
    @JsonProperty("message")
    private String message;

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("ingredient")
    public String getIngredient() {
        return ingredient;
    }

    @JsonProperty("ingredient")
    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @JsonProperty("substitutes")
    public List<String> getSubstitutes() {
        return substitutes;
    }

    @JsonProperty("substitutes")
    public void setSubstitutes(List<String> substitutes) {
        this.substitutes = substitutes;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

}
