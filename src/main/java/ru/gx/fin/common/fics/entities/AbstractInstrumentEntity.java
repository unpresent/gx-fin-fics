package ru.gx.fin.common.fics.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.gx.core.data.entity.AbstractEntityObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * ФИ
 */
@Entity
@Table(schema = "Fics", name = "Instruments")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public abstract class AbstractInstrumentEntity extends AbstractEntityObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private int id;

    /**
     * Тип ФИ
     */
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "Type_Id", nullable = false)
    private InstrumentTypeEntity type;

    /**
     * Краткое внутреннее (в наших системах) наименование ФИ
     */
    @Setter
    @Column(name = "InternalShortName", length = 150, nullable = false)
    private String internalShortName;

    /**
     * Полное внутреннее (в наших системах) наименование ФИ
     */
    @Setter
    @Column(name = "InternalFullName", length = 500, nullable = false)
    private String internalFullName;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "Instrument_Id")
    private Collection<InstrumentGuidEntity> guids = new ArrayList<>();

    public UUID getPrimaryGuid() {
        for (var g : this.guids) {
            if (g.isPrimary()) {
                return g.getGuid();
            }
        }
        return null;
    }

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "Instrument_Id")
    private Collection<InstrumentCodeEntity> codes = new ArrayList<>();
}
