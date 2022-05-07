package ru.gx.fin.common.fics.datacontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.gx.core.channels.ChannelApiDescriptor;
import ru.gx.core.channels.ChannelConfigurationException;
import ru.gx.core.data.DataObject;
import ru.gx.core.data.edlinking.EntitiesDtoLinksConfigurationException;
import ru.gx.core.data.edlinking.EntityUploadingDescriptor;
import ru.gx.core.data.entity.EntityObject;
import ru.gx.core.data.sqlwrapping.ThreadConnectionsWrapper;
import ru.gx.core.messaging.Message;
import ru.gx.core.messaging.MessageBody;
import ru.gx.core.messaging.MessageHeader;
import ru.gx.core.redis.upload.RedisOutcomeCollectionUploadingDescriptor;
import ru.gx.core.redis.upload.RedisOutcomeCollectionsUploader;
import ru.gx.core.simpleworker.SimpleWorker;
import ru.gx.core.simpleworker.SimpleWorkerOnIterationExecuteEvent;
import ru.gx.core.simpleworker.SimpleWorkerOnStartingExecuteEvent;
import ru.gx.core.simpleworker.SimpleWorkerOnStoppingExecuteEvent;
import ru.gx.fin.common.fics.config.FicsEntitiesUploadingConfiguration;
import ru.gx.fin.common.fics.config.RedisOutcomeCollectionsConfiguration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.ArrayList;

import static lombok.AccessLevel.PROTECTED;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataController {
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Fields">
    @Getter(PROTECTED)
    @NotNull
    private final ObjectMapper objectMapper;

    @Getter(PROTECTED)
    @NotNull
    private final DataSource dataSource;

    @Getter(PROTECTED)
    @NotNull
    private final SimpleWorker simpleWorker;

    @Getter(PROTECTED)
    @NotNull
    private final FicsEntitiesUploadingConfiguration entitiesUploadingConfiguration;

    @Getter(PROTECTED)
    @NotNull
    private final EntityManagerFactory entityManagerFactory;

    @Getter(PROTECTED)
    @NotNull
    private ThreadConnectionsWrapper threadConnectionsWrapper;

    @Getter(PROTECTED)
    @NotNull
    private final RedisOutcomeCollectionsUploader redisUploader;

    @Getter(PROTECTED)
    @NotNull
    private final RedisOutcomeCollectionsConfiguration redisOutcomeTopicsConfiguration;
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Обработка событий Worker-а">

    /**
     * Обработка события о начале работы цикла итераций.
     *
     * @param event Объект-событие с параметрами.
     */
    @SuppressWarnings("unused")
    @EventListener(SimpleWorkerOnStartingExecuteEvent.class)
    public void startingExecute(SimpleWorkerOnStartingExecuteEvent event) throws Exception {
        log.debug("Starting startingExecute()");

        // Публикация собственных данных
        publishAllOnStart();

        log.debug("Finished startingExecute()");
    }

    /**
     * Обработка события об окончании работы цикла итераций.
     *
     * @param event Объект-событие с параметрами.
     */
    @SuppressWarnings("unused")
    @EventListener(SimpleWorkerOnStoppingExecuteEvent.class)
    public void stoppingExecute(SimpleWorkerOnStoppingExecuteEvent event) {
        log.debug("Starting stoppingExecute()");
        log.debug("Finished stoppingExecute()");
    }

    /**
     * Обработчик итераций.
     *
     * @param event Объект-событие с параметрами итерации.
     */
    @EventListener(SimpleWorkerOnIterationExecuteEvent.class)
    public void iterationExecute(SimpleWorkerOnIterationExecuteEvent event) {
        log.debug("Starting iterationExecute()");
        try {
            this.simpleWorker.runnerIsLifeSet();
            event.setImmediateRunNextIteration(false);

            // TODO: body

        } catch (Exception e) {
            internalTreatmentExceptionOnDataRead(event, e);
        } finally {
            log.debug("Finished iterationExecute()");
        }
    }

    /**
     * Обработка ошибки при выполнении итерации.
     *
     * @param event Объект-событие с параметрами итерации.
     * @param e     Ошибка, которую требуется обработать.
     */
    private void internalTreatmentExceptionOnDataRead(SimpleWorkerOnIterationExecuteEvent event, Exception e) {
        log.error("", e);
        if (e instanceof InterruptedException) {
            log.info("event.setStopExecution(true)");
            event.setStopExecution(true);
        } else {
            log.info("event.setNeedRestart(true)");
            event.setNeedRestart(true);
        }
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Вспомогательные сервисы">
    @SuppressWarnings("unchecked")
    protected void publishAllOnStart() throws Exception {
        for(final var descriptor : this.entitiesUploadingConfiguration.getAll()) {
            this.publishSnapshot(
                    (EntityUploadingDescriptor
                            <ChannelApiDescriptor<Message<?,?>>, EntityObject, DataObject>) descriptor
            );
        }
    }

    @NotNull
    private
    RedisOutcomeCollectionUploadingDescriptor<? extends Message<? extends MessageHeader, ? extends MessageBody>>
    getRedisOutcomeDescriptorByChannelApi(@NotNull final ChannelApiDescriptor<?> channelApiDescriptor) {
        for (final var descriptor : this.redisOutcomeTopicsConfiguration.getAll()) {
            if (descriptor.getApi() == channelApiDescriptor) {
                return (
                        RedisOutcomeCollectionUploadingDescriptor
                                <? extends Message<? extends MessageHeader, ? extends MessageBody>>) descriptor;
            }
        }
        throw new ChannelConfigurationException("There isn't RedisOutcomeCollectionUploadingDescriptor for api: "
                + channelApiDescriptor.getName());
    }

    public <M extends Message<? extends MessageHeader, ? extends MessageBody>,
            E extends EntityObject, O extends DataObject,
            CH extends ChannelApiDescriptor<M>>
    void publishSnapshot(EntityUploadingDescriptor<CH, E, O> entityUploadingDescriptor) throws Exception {

        final var repository = entityUploadingDescriptor.getRepository();
        if (repository == null) {
            throw new EntitiesDtoLinksConfigurationException("Can't get CrudRepository by ChannelApi "
                    + entityUploadingDescriptor.getChannelApiDescriptor().getName());
        }

        final var converter = entityUploadingDescriptor.getDtoFromEntityConverter();
        if (converter == null) {
            throw new EntitiesDtoLinksConfigurationException("Can't get Converter by ChannelApi "
                    + entityUploadingDescriptor.getChannelApiDescriptor().getName());
        }

        final var keyExtractor = entityUploadingDescriptor.getKeyExtractor();
        if (keyExtractor == null) {
            throw new EntitiesDtoLinksConfigurationException("Can't get KeyExtractor by ChannelApi "
                    + entityUploadingDescriptor.getChannelApiDescriptor().getName());
        }

        // Загружаем данные из БД:
        final var entityObjects = repository.findAll();

        // Преобразуем в список DTO
        final var dataObjects = new ArrayList<O>();
        converter.fillDtoCollectionFromSource(dataObjects, entityObjects);

        // Выгружаем данные
        final var redisDescriptor =
                getRedisOutcomeDescriptorByChannelApi(entityUploadingDescriptor.getChannelApiDescriptor());
        this.redisUploader.uploadDataObjects(redisDescriptor, dataObjects, keyExtractor);
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
}
