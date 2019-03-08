package com.maksymfedosov.entity;

import com.maksymfedosov.entity.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    private long id;
    private Category category;
    private String name;
    private List<String> protoUrls;
    private List<Tag> tags;
    private Status status;

    public Pet(String name) {
        this.name = name;
    }
}
