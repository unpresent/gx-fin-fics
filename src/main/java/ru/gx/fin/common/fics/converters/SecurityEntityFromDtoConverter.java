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
import ru.gx.fin.common.fics.entities.SecurityEntity;
import ru.gx.fin.common.fics.repository.InstrumentTypesRepository;
import ru.gx.fin.common.fics.repository.ProvidersRepository;
import ru.gx.fin.common.fics.repository.SecuritiesRepository;
import ru.gx.fin.common.fics.out.Security;

import static lombok.AccessLevel.PROTECTED;

@RequiredArgsConstructor
@Component
public class SecurityEntityFromDtoConverter extends AbstractEntityFromDtoConverter<SecurityEntity, Security> {
    @Getter(PROTECTED)
    @NotNull
    private final SecuritiesRepository securitiesRepository;

    @Getter(PROTECTED)
    @NotNull
    private final InstrumentTypesRepository instrumentTypesRepository;

    @Getter(PROTECTED)
    @NotNull
    private final ProvidersRepository providersRepository;

    @Override
    public @Nullable SecurityEntity findEntityBySource(@Nullable Security source) {
        if (source == null) {
            return null;
        }
        return this.securitiesRepository.findByGuid(source.getGuid()).orElse(null);
    }

    @Override
    public @NotNull SecurityEntity createEntityBySource(@NotNull Security source) {
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
