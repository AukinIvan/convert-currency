package com.tz.convertercurrency.service;

import com.tz.convertercurrency.entity.HistoryConvert;
import com.tz.convertercurrency.repository.HistoryConvertRepository;
import com.tz.convertercurrency.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class HistoryConvertService {
    private final HistoryConvertRepository historyConvertRepository;

    @Autowired
    public HistoryConvertService(HistoryConvertRepository historyConvertRepository) {
        this.historyConvertRepository = historyConvertRepository;
    }

    public void saveConvert(HistoryConvert historyConvert) {
        historyConvertRepository.saveAndFlush(historyConvert);
    }

    public List<HistoryConvert> getHistoryConvertForUser() {
        return historyConvertRepository.findHistoryConvertByUserId(SecurityUtil.getUserId()).orElse(Collections.EMPTY_LIST);
    }
}
