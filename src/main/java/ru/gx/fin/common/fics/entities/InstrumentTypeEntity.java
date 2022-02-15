package ru.gx.fin.common.fics.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.gx.core.data.entity.AbstractEntityObject;

import javax.persistence.*;

/**
 * Тип ФИ
 */
@Entity
@Table(schema = "Fics", name = "Instruments_Types")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class InstrumentTypeEntity extends AbstractEntityObject {
    @Id
    @Column(name = "Id", nullable = false)
    private short id;

    /**
     * Код ФИ
     */
    @Column(name = "Code", length = 50, nullable = false)
    private String code;
}