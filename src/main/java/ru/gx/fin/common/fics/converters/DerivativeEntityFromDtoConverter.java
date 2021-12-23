package ru.gx.fin.common.fics.converters;

import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import ru.gx.core.data.edlinking.AbstractEntityFromDtoConverter;
import ru.gx.fin.common.fics.entities.AbstractInstrumentEntity;
import ru.gx.fin.common.fics.entities.DerivativeEntity;
import ru.gx.fin.common.fics.repository.*;
import ru.gx.fin.common.fics.out.Currency;
import ru.gx.fin.common.fics.out.Derivative;
import ru.gx.fin.common.fics.out.Security;

import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

public class DerivativeEntityFromDtoConverter extends AbstractEntityFromDtoConverter<DerivativeEntity, Derivative> {
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DerivativesRepository derivativesRepository;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private CurrenciesRepository currenciesRepository;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SecuritiesRepository securitiesRepository;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private InstrumentTypesRepository instrumentTypesRepository;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private ProvidersRepository providersRepository;

    @Override
    public @Nullable DerivativeEntity findDtoBySource(@Nullable Derivative source) {
        if (source == null) {
            return null;
        }
        return this.derivativesRepository.findByGuid(source.getGuid()).orElse(null);
    }

    @Override
    public @NotNull DerivativeEntity createDtoBySource(@NotNull Derivative source) {
        final var result = new DerivativeEntity();
        updateDtoBySource(result, source);
        return result;
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull DerivativeEntity destination) {
        return true;
    }

    @SneakyThrows(Exception.class)
    @Override
    public void updateDtoBySource(@NotNull DerivativeEntity destination, @NotNull Derivative source) {
        final var sourceTypeCode = source.getType();
        final var type = this.instrumentTypesRepository.findByCode(sourceTypeCode);
        if (type.isEmpty()) {
            throw new Exception("Doesn't exists local Fics.InstrumentType by code = " + sourceTypeCode);
        }

        AbstractInstrumentEntity destBaseInstrument = null;
        if (StringUtils.hasLength(source.getBaseInstrument())) {
            final var sourceBaseInstrumentGuid = UUID.fromString(source.getBaseInstrument());
            destBaseInstrument = this.currenciesRepository.findByGuid(sourceBaseInstrumentGuid).orElse(null);
            if (destBaseInstrument == null) {
                destBaseInstrument = this.securitiesRepository.findByGuid(sourceBaseInstrumentGuid).orElse(null);
            }
            if (destBaseInstrument == null) {
                destBaseInstrument = this.derivativesRepository.findByGuid(sourceBaseInstrumentGuid).orElse(null);
            }
        }
        destination
                .setBaseInstrument(destBaseInstrument)
                .setExpireDate(source.getExpireDate())
                .setType(type.get())
                .setInternalShortName(source.getInternalShortName())
                .setInternalFullName(source.getInternalFullName());

        CodesFillUtils.fillEntityCodes(destination, source, this.providersRepository);
    }
}
