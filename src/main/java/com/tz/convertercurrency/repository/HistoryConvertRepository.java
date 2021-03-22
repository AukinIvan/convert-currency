package com.tz.convertercurrency.repository;

import com.tz.convertercurrency.entity.HistoryConvert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryConvertRepository extends JpaRepository<HistoryConvert, Long> {
    Optional<List<HistoryConvert>> findHistoryConvertByUserId(Long userId);
}
