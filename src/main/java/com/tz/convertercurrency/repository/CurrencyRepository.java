package com.tz.convertercurrency.repository;

import com.tz.convertercurrency.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    @Query(value = "select distinct on (char_code) * from currency", nativeQuery = true)
    List<Currency> findAllDistinctCurrency();

    @Query(value = "select * from currency where valute_id = :valuteId and date = CURRENT_DATE limit 1", nativeQuery = true)
    Currency findCurrencyCurrentDate(String valuteId);

    @Query(value = "select case when exists( select * from currency where date = CURRENT_DATE) then 'true' else 'false' end", nativeQuery = true)
    boolean existsCurrencyCurrentDate();
}
