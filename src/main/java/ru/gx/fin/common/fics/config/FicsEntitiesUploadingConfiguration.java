package ru.gx.fin.common.fics.config;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import ru.gx.core.data.edlinking.AbstractEntitiesUploadingConfiguration;
import ru.gx.core.data.edlinking.EntitiesDtoLinksConfigurationException;
import ru.gx.fin.common.fics.converters.CurrencyDtoFromEntityConverter;
import ru.gx.fin.common.fics.converters.DerivativeDtoFromEntityConverter;
import ru.gx.fin.common.fics.converters.SecurityDtoFromEntityConverter;
import ru.gx.fin.common.fics.entities.CurrencyEntity;
import ru.gx.fin.common.fics.entities.DerivativeEntity;
import ru.gx.fin.common.fics.entities.SecurityEntity;
import ru.gx.fin.common.fics.repository.CurrenciesRepository;
import ru.gx.fin.common.fics.repository.DerivativesRepository;
import ru.gx.fin.common.fics.repository.SecuritiesRepository;
import ru.gx.fin.common.fics.channels.FicsSnapshotCurrencyDataPublishChannelApiV1;
import ru.gx.fin.common.fics.channels.FicsSnapshotDerivativeDataPublishChannelApiV1;
import ru.gx.fin.common.fics.channels.FicsSnapshotSecurityDataPublishChannelApiV1;
import ru.gx.fin.common.fics.keyextractors.CurrencyKeyExtractor;
import ru.gx.fin.common.fics.keyextractors.DerivativeKeyExtractor;
import ru.gx.fin.common.fics.keyextractors.SecurityKeyExtractor;
import ru.gx.fin.common.fics.out.*;

import javax.annotation.PostConstruct;

import static lombok.AccessLevel.PROTECTED;

@RequiredArgsConstructor
@Configuration
public class FicsEntitiesUploadingConfiguration extends AbstractEntitiesUploadingConfiguration {
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Fields">
    @NotNull
    private FicsSnapshotCurrencyDataPublishChannelApiV1 currencyDataPublishChannelApiV1;

    @NotNull
    private FicsSnapshotSecurityDataPublishChannelApiV1 securityDataPublishChannelApiV1;

    @NotNull
    private FicsSnapshotDerivativeDataPublishChannelApiV1 derivativeDataPublishChannelApiV1;

    @NotNull
    private CurrencyKeyExtractor currencyKeyExtractor;

    @NotNull
    private SecurityKeyExtractor securityKeyExtractor;

    @NotNull
    private DerivativeKeyExtractor derivativeKeyExtractor;

    @NotNull
    private CurrencyDtoFromEntityConverter currencyDtoFromEntityConverter;

    @NotNull
    private SecurityDtoFromEntityConverter securityDtoFromEntityConverter;

    @NotNull
    private DerivativeDtoFromEntityConverter derivativeDtoFromEntityConverter;

    @NotNull
    private CurrenciesRepository currenciesRepository;

    @NotNull
    private SecuritiesRepository securitiesRepository;

    @NotNull
    private DerivativesRepository derivativesRepository;

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="implements EntitiesDtoLinksConfigurator">

    @SneakyThrows(EntitiesDtoLinksConfigurationException.class)
    @PostConstruct
    public void init() {
        this
                .newDescriptor(this.currencyDataPublishChannelApiV1, CurrencyEntity.class, Currency.class)
                .setDtoPackageClass(CurrenciesPackage.class)
                .setRepository(this.currenciesRepository)
                .setDtoFromEntityConverter(this.currencyDtoFromEntityConverter)
                .setKeyExtractor(this.currencyKeyExtractor);
        this
                .newDescriptor(this.securityDataPublishChannelApiV1, SecurityEntity.class, Security.class)
                .setDtoPackageClass(SecuritiesPackage.class)
                .setRepository(this.securitiesRepository)
                .setDtoFromEntityConverter(this.securityDtoFromEntityConverter)
                .setKeyExtractor(this.securityKeyExtractor);
        this
                .newDescriptor(this.derivativeDataPublishChannelApiV1, DerivativeEntity.class, Derivative.class)
                .setDtoPackageClass(DerivativesPackage.class)
                .setRepository(this.derivativesRepository)
                .setDtoFromEntityConverter(this.derivativeDtoFromEntityConverter)
                .setKeyExtractor(this.derivativeKeyExtractor);
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
}
