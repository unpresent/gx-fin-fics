package ru.gx.fin.common.fics.converters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gx.core.data.edlinking.AbstractEntityFromDtoConverter;
import ru.gx.fin.common.fics.entities.CurrencyEntity;
import ru.gx.fin.common.fics.out.Currency;
import ru.gx.fin.common.fics.repository.CurrenciesRepository;
import ru.gx.fin.common.fics.repository.InstrumentTypesRepository;
import ru.gx.fin.common.fics.repository.ProvidersRepository;

import static lombok.AccessLevel.PROTECTED;

@RequiredArgsConstructor
@Component
public class CurrencyEntityFromDtoConverter extends AbstractEntityFromDtoConverter<CurrencyEntity, Currency> {
    @Getter(PROTECTED)
    @NotNull
    private final CurrenciesRepository currenciesRepository;

    @Getter(PROTECTED)
    @NotNull
    private final InstrumentTypesRepository instrumentTypesRepository;

    @Getter(PROTECTED)
    @NotNull
    private final ProvidersRepository providersRepository;

    @SuppressWarnings("unused")
    @Nullable
    public static CurrencyEntity getEntityByDto(@NotNull final CurrenciesRepository entitiesRepository, @Nullable final Currency source) {
        if (source == null) {
            return null;
        }
        return entitiesRepository.findByGuid(source.getGuid()).orElse(null);
    }

    @Override
    public @Nullable CurrencyEntity findEntityBySource(@Nullable Currency source) {
        if (source == null) {
            return null;
        }
        return this.currenciesRepository.findByGuid(source.getGuid()).orElse(null);
    }

    @Override
    public @NotNull CurrencyEntity createEntityBySource(@NotNull Currency source) {
        final var result = new CurrencyEntity();
        updateDtoBySource(result, source);
        return result;
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull CurrencyEntity destination) {
        return true;
    }

    @SneakyThrows(Exception.class)
    @Override
    public void updateDtoBySource(@NotNull CurrencyEntity destination, @NotNull Currency source) {
        final var sourceTypeCode = source.getType();
        final var type = this.instrumentTypesRepository.findByCode(sourceTypeCode);
        if (type.isEmpty()) {
            throw new Exception("Doesn't exists local Fics.InstrumentType by code = " + sourceTypeCode);
        }
        destination
                .setCodeAlpha2(source.getCodeAlpha2())
                .setCodeAlpha3(source.getCodeAlpha3())
                .setCodeDec(source.getCodeDec())
                .setSign(source.getSign())
                .setPartsNames(source.getPartsNames())
                .setPartsInOne(source.getPartsInOne())
                .setType(type.get())
                .setInternalShortName(source.getInternalShortName())
                .setInternalFullName(source.getInternalFullName());

        CodesFillUtils.fillEntityCodes(destination, source, this.providersRepository);
    }
}
