package com.astrocharts;

import com.astrocharts.util.AllListsManager;
import com.astrocharts.util.RowRecord;
import javax.annotation.PostConstruct;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SharedCacheService {

    private AllListsManager allListsManager = null;

    @javax.annotation.PostConstruct
    public void initCache() {
        boolean displayOn = false;
        boolean intraDay = false;
        allListsManager =  new AllListsManager(displayOn,intraDay);
        // Diagnostic prints
        System.out.println("[SharedCacheService] Total tickers loaded: " + allListsManager.getTickerCount());
        System.out.println("[SharedCacheService] Ticker list: " + allListsManager.getTickerList());
        if (allListsManager.getTickerList().contains("AAPL")) {
            List<RowRecord> aaplData = allListsManager.getList("AAPL");
            if (aaplData == null) {
                System.out.println("[SharedCacheService] WARNING: AAPL exists but data is null!");
            } else {
                System.out.println("[SharedCacheService] AAPL data size: " + aaplData.size());
                if (aaplData.size() == 0) {
                    System.out.println("[SharedCacheService] WARNING: AAPL data is empty!");
                }
            }
        } else {
            System.out.println("[SharedCacheService] WARNING: AAPL not found in ticker list!");
        }
    }
    
    public List<RowRecord> getRowRecords(String ticker) {
        System.out.println("ticker =" +ticker);
		List<RowRecord> stockData = allListsManager.getList(ticker); 
        return stockData;
    }
}

