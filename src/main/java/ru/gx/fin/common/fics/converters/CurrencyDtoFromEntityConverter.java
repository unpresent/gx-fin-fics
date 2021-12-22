package ru.gx.fin.common.fics.converters;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.NotAllowedObjectUpdateException;
import ru.gx.core.data.edlinking.AbstractDtoFromEntityConverter;
import ru.gx.fin.common.fics.entities.CurrencyEntity;
import ru.gx.fin.core.fics.out.Currency;

public class CurrencyDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Currency, CurrencyEntity> {
    @Override
    @Nullable
    public Currency findDtoBySource(@Nullable final CurrencyEntity source) {
        return null;
    }

    @SneakyThrows(Exception.class)
    @Override
    @NotNull
    public Currency createDtoBySource(@NotNull CurrencyEntity source) {
        final var sourceType = source.getType();
        if (sourceType == null) {
            throw new Exception("It isn't allowed create Currency with null type; source = " + source);
        }

        final var result = new Currency(
            source.getPrimaryGuid(),
            sourceType.getCode(),
            source.getInternalShortName(),
            source.getInternalFullName(),
            source.getCodeAlpha2(),
            source.getCodeAlpha3(),
            source.getCodeDec(),
            source.getSign(),
            source.getPartsNames(),
            source.getPartsInOne()
        );

        CodesFillUtils.fillDtoCodes(result, source);

        return result;
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull Currency currency) {
        return false;
    }

    @Override
    public void updateDtoBySource(@NotNull Currency currency, @NotNull CurrencyEntity currencyEntity) throws NotAllowedObjectUpdateException {
        throw new NotAllowedObjectUpdateException(Currency.class, null);
    }
}
