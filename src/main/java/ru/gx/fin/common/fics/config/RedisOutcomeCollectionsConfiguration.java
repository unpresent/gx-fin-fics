package ru.gx.fin.common.fics.config;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.core.redis.upload.AbstractRedisOutcomeCollectionsConfiguration;
import ru.gx.core.redis.upload.RedisOutcomeCollectionUploadingDescriptor;
import ru.gx.fin.common.dris.channels.DrisSnapshotInstrumentTypeDataPublishChannelApiV1;
import ru.gx.fin.common.dris.channels.DrisSnapshotPlaceDataPublishChannelApiV1;
import ru.gx.fin.common.dris.channels.DrisSnapshotProviderDataPublishChannelApiV1;
import ru.gx.fin.common.dris.channels.DrisSnapshotProviderTypeDataPublishChannelApiV1;
import ru.gx.fin.core.fics.channels.FicsSnapshotCurrencyDataPublishChannelApiV1;
import ru.gx.fin.core.fics.channels.FicsSnapshotDerivativeDataPublishChannelApiV1;
import ru.gx.fin.core.fics.channels.FicsSnapshotSecurityDataPublishChannelApiV1;

import javax.annotation.PostConstruct;

import static lombok.AccessLevel.PROTECTED;

public class RedisOutcomeCollectionsConfiguration extends AbstractRedisOutcomeCollectionsConfiguration {
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private FicsSnapshotCurrencyDataPublishChannelApiV1 currencyChannelApiV1;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private FicsSnapshotSecurityDataPublishChannelApiV1 securityChannelApiV1;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private FicsSnapshotDerivativeDataPublishChannelApiV1 derivativeChannelApiV1;

    public RedisOutcomeCollectionsConfiguration(@NotNull String configurationName) {
        super(configurationName);
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        // this.getDescriptorsDefaults();

        this
                .newDescriptor(this.currencyChannelApiV1, RedisOutcomeCollectionUploadingDescriptor.class)
                .setPriority(0)
                .init();

        this
                .newDescriptor(this.securityChannelApiV1, RedisOutcomeCollectionUploadingDescriptor.class)
                .setPriority(1)
                .init();

        this
                .newDescriptor(this.derivativeChannelApiV1, RedisOutcomeCollectionUploadingDescriptor.class)
                .setPriority(2)
                .init();
    }
}
