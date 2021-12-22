package ru.gx.fin.common.fics.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class InstrumentCodeEntityId implements Serializable {
    private AbstractInstrumentEntity instrument;
    private ProviderEntity provider;
    private short index;
}
