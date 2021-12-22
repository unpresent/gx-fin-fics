package ru.gx.fin.common.fics.converters;

import org.jetbrains.annotations.NotNull;
import ru.gx.fin.common.fics.entities.AbstractInstrumentEntity;
import ru.gx.fin.common.fics.entities.InstrumentCodeEntity;
import ru.gx.fin.common.fics.repository.ProvidersRepository;
import ru.gx.fin.core.fics.out.AbstractInstrument;
import ru.gx.fin.core.fics.out.InstrumentCode;

public abstract class CodesFillUtils {

    public static void fillDtoCodes(
            @NotNull final AbstractInstrument destination,
            @NotNull final AbstractInstrumentEntity source
    ) throws Exception {
        final var destCodes = destination.getCodes();
        final var sourceCodes = source.getCodes();
        for (var sourceCode : sourceCodes) {
            final var sourceProvider = sourceCode.getProvider();
            if (sourceProvider == null) {
                throw new Exception("It isn't allowed create InstrumentCode with null Provider; sourceCode = " + sourceCode);
            }

            final var code = new InstrumentCode(
                    sourceProvider.getCode(),
                    sourceCode.getCode(),
                    sourceCode.getName(),
                    sourceCode.getIndex(),
                    sourceCode.getDateFrom(),
                    sourceCode.getDateTo()
            );
            destCodes.add(code);
        }
    }

    public static void fillEntityCodes(
        @NotNull final AbstractInstrumentEntity destination,
        @NotNull final AbstractInstrument source,
        @NotNull final ProvidersRepository providersRepository
    ) throws Exception {
        final var sourceCodes = source.getCodes();
        final var destCodes = destination.getCodes();
        destCodes.clear();
        for (var sourceCode : sourceCodes) {
            final var provider = providersRepository.findByCode(sourceCode.getProvider());
            if (provider.isEmpty()) {
                throw new Exception("It isn't allowed create InstrumentCode with null Provider!");
            }
            final var destCode = new InstrumentCodeEntity()
                    .setProvider(provider.get())
                    .setCode(sourceCode.getCode())
                    .setName(sourceCode.getName())
                    .setIndex(sourceCode.getIndex())
                    .setDateFrom(sourceCode.getDateFrom())
                    .setDateTo(sourceCode.getDateTo());
            destCodes.add(destCode);
        }
    }
}
