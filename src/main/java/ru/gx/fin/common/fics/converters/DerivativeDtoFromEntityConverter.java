package ru.gx.fin.common.fics.converters;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.gx.core.data.NotAllowedObjectUpdateException;
import ru.gx.core.data.edlinking.AbstractDtoFromEntityConverter;
import ru.gx.fin.common.fics.entities.DerivativeEntity;
import ru.gx.fin.common.fics.out.Derivative;

@RequiredArgsConstructor
@Component
public class DerivativeDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Derivative, DerivativeEntity> {
    @Override
    public @Nullable Derivative findDtoBySource(@Nullable DerivativeEntity source) {
        return null;
    }

    @SneakyThrows(Exception.class)
    @Override
    public @NotNull Derivative createDtoBySource(@NotNull DerivativeEntity source) {

        final var sourceType = source.getType();
        if (sourceType == null) {
            throw new Exception("It isn't allowed create Derivative with null Type; source = " + source);
        }

        final var sourceBaseInstrument = source.getBaseInstrument();

        return new Derivative(
                source.getPrimaryGuid(),
                sourceType.getCode(),
                source.getInternalShortName(),
                source.getInternalFullName(),
                sourceBaseInstrument != null ? sourceBaseInstrument.getPrimaryGuid().toString() : null,
                source.getExpireDate()
        );
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull Derivative destination) {
        return false;
    }

    @Override
    public void updateDtoBySource(@NotNull Derivative destination, @NotNull DerivativeEntity source) throws NotAllowedObjectUpdateException {
        throw new NotAllowedObjectUpdateException(Derivative.class, null);
    }
}
