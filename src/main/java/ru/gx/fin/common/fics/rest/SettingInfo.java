package ru.gx.fin.common.fics.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class SettingInfo {
    private final String name;
    private final String value;

    @JsonCreator
    public SettingInfo(
            @JsonProperty("name") @NotNull final String name,
            @JsonProperty("value") @Nullable final String value
    ) {
        this.name = name;
        this.value = value;
    }
}
