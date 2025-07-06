package com.astrocharts;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.astrocharts.util.RowRecord;
import com.astrocharts.util.Commons;

@CrossOrigin(origins = "http://localhost:3000")
@RestController


public class SymbolController {
    @Autowired
    private SharedCacheService sharedCacheService;


    @GetMapping("/api/symbols")
    public List<String> getAllSymbols() {
        return Commons.getTickerList();
    }

    @GetMapping("/api/getSymbolList")
    public List<String> getSymbolList() {
        return Commons.getTickerList();
    }

    @GetMapping("/api/getDataForSymbol/{symbol}")
    public List<RowRecord> getDataForSymbol(@org.springframework.web.bind.annotation.PathVariable String symbol) {
        System.out.println("symbol = " + symbol);
        List<RowRecord> stockRecList = sharedCacheService.getRowRecords(symbol);
        System.out.println("stockRecList.size() = " + stockRecList.size());
        Collections.reverse(stockRecList); // Reverse to send in chronological order
        return stockRecList;
    }

    @GetMapping("/api/getDataForTwoSymbols/{symbol1}/{symbol2}")
    public List<List<RowRecord>> getDataForTwoSymbols(@org.springframework.web.bind.annotation.PathVariable String symbol1, @org.springframework.web.bind.annotation.PathVariable String symbol2) {
        List<RowRecord> bars1 = sharedCacheService.getRowRecords(symbol1);
        List<RowRecord> bars2 = sharedCacheService.getRowRecords(symbol2);
        return Arrays.asList(
            bars1,
            bars2
        );
    }

}

