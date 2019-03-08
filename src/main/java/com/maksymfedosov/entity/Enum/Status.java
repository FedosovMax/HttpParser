package com.maksymfedosov.entity.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Status {
    available("available"),
    pending("pending"),
    sold("sold");

    private String text;

    Status(String text) {
        this.text = text;
    }

    @JsonCreator
    public static Status fromText(String text){
        for(Status s : Status.values()){
            if(s.getText().equals(text)){
                return s;
            }
        }
        throw new IllegalArgumentException(
                "Unknown enum type: " + text + "You can choose from: " + Arrays.toString(Status.values())
        );
    }

}
