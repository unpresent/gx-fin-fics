package ru.gx.fin.common.fics.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Валюта
 */
@Entity
@Table(schema = "Fics", name = "Instruments=Currencies")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class CurrencyEntity extends AbstractInstrumentEntity {

    /**
     * Код Alpha-2
     */
    @Column(name = "CodeAlpha2", length = 2)
    private String codeAlpha2;

    /**
     * Код Alpha-3
     */
    @Column(name = "CodeAlpha3", length = 3)
    private String codeAlpha3;

    /**
     * Код Dec
     */
    @Column(name = "CodeDec")
    private Short codeDec;

    /**
     * Знак
     */
    @Column(name = "Sign")
    private String sign;

    /**
     * Название разменной части
     */
    @Column(name = "PartsName")
    private String partsNames;

    /**
     * Сколько разменных единиц в единице валюты (сколько "копеек в рубле")
     */
    @Column(name = "PartsInOne")
    private Integer partsInOne;
}
