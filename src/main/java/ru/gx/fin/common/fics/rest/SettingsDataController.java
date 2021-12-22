package ru.gx.fin.common.fics.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gx.core.settings.StandardSettingsController;

import static lombok.AccessLevel.PROTECTED;

@Tag(name = "settings", description = "The data API")
@RestController
@RequestMapping("settings")
public class SettingsDataController {
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private StandardSettingsController settings;

    @Operation(description = "Get any setting value")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the places",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SettingInfo.class)
                            )
                    }
            )
    )
    @GetMapping("get-setting")
    public SettingInfo getSetting(@RequestParam("name") @NotNull final String name) {
        final var value = this.settings.getSetting(name);
        return new SettingInfo(name, value != null ? value.toString() : null);
    }
}
