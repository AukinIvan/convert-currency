package com.tz.convertercurrency.entity;

import lombok.Data;
import org.springframework.validation.BindingResult;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class RequestConvert {
    private BindingResult bindingResult;

    @NotBlank(message = "Выберите исходную валюту")
    private String currencyConvertFrom;

    @NotBlank(message = "Выберите целевую валюту")
    private String currencyConvertTo;

    @Positive(message = "Неправильно указано значение. Введите положительное число.")
    private double convertFrom;
}
