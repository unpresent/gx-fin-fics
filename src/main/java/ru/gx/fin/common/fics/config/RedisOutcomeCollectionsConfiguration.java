package ru.gx.fin.common.fics.config;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import ru.gx.core.redis.upload.AbstractRedisOutcomeCollectionsConfiguration;
import ru.gx.core.redis.upload.RedisOutcomeCollectionUploadingDescriptor;
import ru.gx.fin.common.fics.channels.FicsSnapshotCurrencyDataPublishChannelApiV1;
import ru.gx.fin.common.fics.channels.FicsSnapshotDerivativeDataPublishChannelApiV1;
import ru.gx.fin.common.fics.channels.FicsSnapshotSecurityDataPublishChannelApiV1;

import javax.annotation.PostConstruct;

import static lombok.AccessLevel.PROTECTED;

@Configuration
public class RedisOutcomeCollectionsConfiguration extends AbstractRedisOutcomeCollectionsConfiguration {
    @Getter(PROTECTED)
    @NotNull
    private final FicsSnapshotCurrencyDataPublishChannelApiV1 currencyChannelApiV1;

    @Getter(PROTECTED)
    @NotNull
    private final FicsSnapshotSecurityDataPublishChannelApiV1 securityChannelApiV1;

    @Getter(PROTECTED)
    @NotNull
    private final FicsSnapshotDerivativeDataPublishChannelApiV1 derivativeChannelApiV1;

    public RedisOutcomeCollectionsConfiguration(
            @NotNull final RedisConnectionFactory redisConnectionFactory,
            @NotNull final FicsSnapshotCurrencyDataPublishChannelApiV1 currencyChannelApiV1,
            @NotNull final FicsSnapshotSecurityDataPublishChannelApiV1 securityChannelApiV1,
            @NotNull final FicsSnapshotDerivativeDataPublishChannelApiV1 derivativeChannelApiV1) {
        super("redis-outcome-config", redisConnectionFactory);
        this.currencyChannelApiV1 = currencyChannelApiV1;
        this.securityChannelApiV1 = securityChannelApiV1;
        this.derivativeChannelApiV1 = derivativeChannelApiV1;
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        // this.getDescriptorsDefaults();

        this
                .newDescriptor(this.currencyChannelApiV1, RedisOutcomeCollectionUploadingDescriptor.class)
                .init();
        this
                .newDescriptor(this.securityChannelApiV1, RedisOutcomeCollectionUploadingDescriptor.class)
                .init();
        this
                .newDescriptor(this.derivativeChannelApiV1, RedisOutcomeCollectionUploadingDescriptor.class)
                .init();
    }
}
