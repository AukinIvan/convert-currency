package com.tz.convertercurrency.service;

import com.tz.convertercurrency.entity.Currencies;
import com.tz.convertercurrency.entity.Currency;
import com.tz.convertercurrency.entity.HistoryConvert;
import com.tz.convertercurrency.entity.RequestConvert;
import com.tz.convertercurrency.repository.CurrencyRepository;
import com.tz.convertercurrency.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CurrencyService {
    @Value("${api.url}")
    private static String URL;
    private final CurrencyRepository currencyRepository;
    private final HistoryConvertService convertService;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository, HistoryConvertService convertService) {
        this.currencyRepository = currencyRepository;
        this.convertService = convertService;
    }

    private String formatCurrency(Currency currency) {
        return String.format("%s (%s)", currency.getCharCode(), currency.getName());
    }

    public Map<String, String> getListNameCurrencies() {
        List<Currency> currencyList = currencyRepository.findAllDistinctCurrency();
        if (!currencyList.isEmpty()) {
            return currencyList.stream()
                    .collect(Collectors.toMap(Currency::getValuteId, this::formatCurrency));
        } else {
            return Collections.emptyMap();
        }
    }

    @PostConstruct
    public void getValute() {
        if (!currencyRepository.existsCurrencyCurrentDate()) {

            RestTemplate restTemplate = new RestTemplate();

            var currencies = restTemplate.getForObject(URL, Currencies.class);

            if (currencies != null && !currencies.getValCurs().isEmpty()) {
                log.info("Currency received");
                var listCurrency = currencies.getValCurs();
                listCurrency.add(Currency.builder()
                        .valuteId("R00000")
                        .charCode("RUB")
                        .nominal(0)
                        .name("Российский рубль")
                        .numCode("000")
                        .value(0.0)
                        .build());
                currencyRepository.saveAll(listCurrency);
            } else {
                log.error("Error receiving currency");
            }
        }
    }

    private double convertRubToCurrency(Currency currency, double value) {
        return Math.round((value / currency.getValue()) * 1000.0) / 1000.0;
    }

    private double convertCurrencyToRub(Currency currency, double value) {
        return Math.round((currency.getValue() * value) * 1000.0) / 1000.0;
    }

    public double convertCurrency(RequestConvert request) {
        double resultConvert = request.getConvertFrom();
        getValute();

        Currency currencyFrom = currencyRepository.findCurrencyCurrentDate(request.getCurrencyConvertFrom());
        Currency currencyTo = currencyRepository.findCurrencyCurrentDate(request.getCurrencyConvertTo());

        if (currencyFrom != null && currencyTo != null) {
            if (currencyFrom.getValuteId().equals("R00000") && !currencyTo.getValuteId().equals("R00000")) {
                resultConvert = convertRubToCurrency(currencyTo, request.getConvertFrom());
            }

            if (!currencyFrom.getValuteId().equals("R00000") && currencyTo.getValuteId().equals("R00000")) {
                resultConvert = convertCurrencyToRub(currencyFrom, request.getConvertFrom());
            }

            if (!currencyFrom.getValuteId().equals("R00000") && !currencyTo.getValuteId().equals("R00000")) {
                double valueCurrencyToRub = convertCurrencyToRub(currencyFrom, request.getConvertFrom());
                resultConvert = convertRubToCurrency(currencyTo, valueCurrencyToRub);
            }

            convertService.saveConvert(HistoryConvert.builder()
                    .fromCurrency(formatCurrency(currencyFrom))
                    .toCurrency(formatCurrency(currencyTo))
                    .fromAmount(request.getConvertFrom())
                    .totalAmount(resultConvert)
                    .userId(SecurityUtil.getUserId())
                    .build());
        }

        return resultConvert;
    }
}
