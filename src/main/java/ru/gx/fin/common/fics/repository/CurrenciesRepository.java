package ru.gx.fin.common.fics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gx.fin.common.fics.entities.CurrencyEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CurrenciesRepository extends JpaRepository<CurrencyEntity, Integer> {
    @Query(value =
            """
                    SELECT
                        I."Id",
                        I."Type_Id",
                        I."InternalShortName",
                        I."InternalFullName",
                        C."CodeAlpha2",
                        C."CodeAlpha3",
                        C."CodeDec",
                        C."Sign",
                        C."PartsName",
                        C."PartsInOne"
                    FROM "Base"."Instruments" AS I
                    INNER JOIN "Base"."Instruments=Currencies" AS C ON C."Id" = I."Id"
                    WHERE   EXISTS
                            (
                                SELECT 1
                                FROM "Base"."Instruments:Guids" AS G
                                WHERE   G."Instrument_Id" = I."Id"
                                    AND G."Guid" = :guid
                            )""", nativeQuery = true)
    Optional<CurrencyEntity> findByGuid(@Param("guid") UUID guid);
}
