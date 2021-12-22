package ru.gx.fin.common.fics.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.gx.core.data.AbstractDataObject;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@IdClass(InstrumentCodeEntityId.class)
@Table(schema = "Fics", name = "Instruments:Codes")
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class InstrumentCodeEntity extends AbstractDataObject {
    @Id
    @ManyToOne
    @JoinColumn(name = "Instrument_Id", nullable = false)
    private AbstractInstrumentEntity instrument;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "Provider_Id", nullable = false)
    private ProviderEntity provider;

    /**
     * Порядковый номер кода (для случаев, когда провайдер о данном инструменте транслирует несколько кодов)
     */
    @Id
    @Column(name = "Index", nullable = false)
    private short index;

    @Column(name = "Code", length = 50, nullable = false)
    private String code;

    /**
     * Название инструмента на данном провайдере
     */
    @Column(name = "Name", length = 250)
    private String name;

    /**
     * Дата, начиная с которой данный код начал действовать
     */
    @Column(name = "DateFrom", nullable = false)
    private LocalDate dateFrom;

    /**
     * Дата, до (не включая) которой данный код действовал
     */
    @Column(name = "DateTo")
    private LocalDate dateTo;

}
