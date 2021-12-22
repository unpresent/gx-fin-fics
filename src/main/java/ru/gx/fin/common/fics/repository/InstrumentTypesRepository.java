package ru.gx.fin.common.fics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gx.fin.common.fics.entities.InstrumentTypeEntity;

import java.util.Optional;

@Repository
public interface InstrumentTypesRepository extends JpaRepository<InstrumentTypeEntity, Short> {
    Optional<InstrumentTypeEntity> findByCode(String code);
}
