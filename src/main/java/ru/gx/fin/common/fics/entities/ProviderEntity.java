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
 * Провайдеры
 */
@Entity
@Table(schema = "Fics", name = "Providers")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class ProviderEntity extends AbstractEntityObject {
    /**
     * Идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private short id;

    /**
     * Код.
     */
    @Column(name = "Code", length = 50, nullable = false, unique = true)
    private String code;
}
