package com.tz.convertercurrency.controller;

import com.tz.convertercurrency.entity.HistoryConvert;
import com.tz.convertercurrency.entity.RequestConvert;
import com.tz.convertercurrency.service.CurrencyService;
import com.tz.convertercurrency.service.HistoryConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
public class MainController {
    private final CurrencyService currencyService;
    private final HistoryConvertService historyConvertService;

    @Autowired
    public MainController(CurrencyService currencyService, HistoryConvertService historyConvertService) {
        this.currencyService = currencyService;
        this.historyConvertService = historyConvertService;
    }

    @GetMapping(value = "/")
    public String index() {
        return "redirect:/user";
    }

    @GetMapping("/user")
    public String user(Model model) {
        model.addAttribute("currencyList", currencyService.getListNameCurrencies());
        model.addAttribute("historyList", historyConvertService.getHistoryConvertForUser());
        return "/user";
    }

    @PostMapping("/user")
    @ResponseBody
    public double userConvert(@Valid RequestConvert request) {
        return currencyService.convertCurrency(request);
    }

    @PostMapping("/user/history")
    @ResponseBody
    public List<HistoryConvert> getHistoryConvert() {
        return historyConvertService.getHistoryConvertForUser();
    }
}
