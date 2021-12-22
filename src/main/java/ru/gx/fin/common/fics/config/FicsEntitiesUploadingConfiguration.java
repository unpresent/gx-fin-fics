package ru.gx.fin.common.fics.config;

import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.gx.fin.core.fics.channels.FicsSnapshotCurrencyDataPublishChannelApiV1;
import ru.gx.fin.core.fics.channels.FicsSnapshotDerivativeDataPublishChannelApiV1;
import ru.gx.fin.core.fics.channels.FicsSnapshotSecurityDataPublishChannelApiV1;
import ru.gx.fin.core.fics.keyextractors.CurrencyKeyExtractor;
import ru.gx.fin.core.fics.keyextractors.DerivativeKeyExtractor;
import ru.gx.fin.core.fics.keyextractors.SecurityKeyExtractor;
import ru.gx.fin.core.fics.out.*;

import javax.annotation.PostConstruct;

import static lombok.AccessLevel.PROTECTED;

public class FicsEntitiesUploadingConfiguration extends AbstractEntitiesUploadingConfiguration {
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Fields">
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private FicsSnapshotCurrencyDataPublishChannelApiV1 currencyDataPublishChannelApiV1;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private FicsSnapshotSecurityDataPublishChannelApiV1 securityDataPublishChannelApiV1;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private FicsSnapshotDerivativeDataPublishChannelApiV1 derivativeDataPublishChannelApiV1;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private CurrencyKeyExtractor currencyKeyExtractor;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SecurityKeyExtractor securityKeyExtractor;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DerivativeKeyExtractor derivativeKeyExtractor;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private CurrencyDtoFromEntityConverter currencyDtoFromEntityConverter;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SecurityDtoFromEntityConverter securityDtoFromEntityConverter;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DerivativeDtoFromEntityConverter derivativeDtoFromEntityConverter;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private CurrenciesRepository currenciesRepository;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SecuritiesRepository securitiesRepository;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
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
