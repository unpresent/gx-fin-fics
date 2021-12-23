package ru.gx.fin.common.fics.converters;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.NotAllowedObjectUpdateException;
import ru.gx.core.data.edlinking.AbstractDtoFromEntityConverter;
import ru.gx.fin.common.fics.entities.SecurityEntity;
import ru.gx.fin.common.fics.out.Security;

public class SecurityDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Security, SecurityEntity> {

    @Override
    @Nullable
    public Security findDtoBySource(@Nullable SecurityEntity source) {
        return null;
    }

    @SneakyThrows(Exception.class)
    @Override
    public @NotNull Security createDtoBySource(@NotNull SecurityEntity source) {
        final var sourceType = source.getType();
        if (sourceType == null) {
            throw new Exception("It isn't allowed create Security with null type; source = " + source);
        }

        final var result = new Security(
                source.getPrimaryGuid(),
                sourceType.getCode(),
                source.getInternalShortName(),
                source.getInternalFullName(),
                source.getCodeIsin()
        );

        CodesFillUtils.fillDtoCodes(result, source);

        return result;
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull Security destination) {
        return false;
    }

    @Override
    public void updateDtoBySource(@NotNull Security destination, @NotNull SecurityEntity source) throws NotAllowedObjectUpdateException {
        throw new NotAllowedObjectUpdateException(Security.class, null);
    }
}
