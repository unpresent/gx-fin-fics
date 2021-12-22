package ru.gx.fin.common.fics.converters;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.core.data.edlinking.AbstractEntityFromDtoConverter;
import ru.gx.fin.common.fics.entities.SecurityEntity;
import ru.gx.fin.common.fics.repository.InstrumentTypesRepository;
import ru.gx.fin.common.fics.repository.ProvidersRepository;
import ru.gx.fin.common.fics.repository.SecuritiesRepository;
import ru.gx.fin.core.fics.out.Security;

import static lombok.AccessLevel.PROTECTED;

public class SecurityEntityFromDtoConverter extends AbstractEntityFromDtoConverter<SecurityEntity, Security> {
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
    public @Nullable SecurityEntity findDtoBySource(@Nullable Security source) {
        if (source == null) {
            return null;
        }
        return this.securitiesRepository.findByGuid(source.getGuid()).orElse(null);
    }

    @Override
    public @NotNull SecurityEntity createDtoBySource(@NotNull Security source) {
        final var result = new SecurityEntity();
        updateDtoBySource(result, source);
        return result;
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull SecurityEntity destination) {
        return true;
    }

    @SneakyThrows(Exception.class)
    @Override
    public void updateDtoBySource(@NotNull SecurityEntity destination, @NotNull Security source) {
        final var sourceTypeCode = source.getType();
        final var type = this.instrumentTypesRepository.findByCode(sourceTypeCode);
        if (type.isEmpty()) {
            throw new Exception("Doesn't exists local Fics.InstrumentType by code = " + sourceTypeCode);
        }
        destination
                .setCodeIsin(source.getCodeIsin())
                .setType(type.get())
                .setInternalShortName(source.getInternalShortName())
                .setInternalFullName(source.getInternalFullName());

        CodesFillUtils.fillEntityCodes(destination, source, this.providersRepository);

    }
}
