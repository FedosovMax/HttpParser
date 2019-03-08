package com.maksymfedosov.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maksymfedosov.entity.Enum.Status;
import com.maksymfedosov.entity.Pet;
import com.maksymfedosov.entity.User;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor
public class JsonConverter {

    Gson gson = new Gson();

    public String convertObjectToJson(Object object) {
        return gson.toJson(object);
    }

    public Object convertJsonToObj(String json){
        return gson.fromJson(json, Pet.class);
    }

    public List<Pet> convertJsonToPetList(String json) {
        Objects.requireNonNull(json);
        Type itemsListType = new TypeToken<List<Pet>>() {}.getType();
        return gson.fromJson(json,itemsListType);
    }

    public Map<String,Long> convertJsonToInventoryMap(String jsonString) {
        Type itemsMapType = new TypeToken<Map<String, Long>>() {}.getType();
        return gson.fromJson(jsonString, itemsMapType);
    }

}
