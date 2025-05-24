package com.example.ahsniper;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PriceFetcher {
    public static double getPriceFromCoflnet(String itemName) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = "https://sky.coflnet.com/api/item/price/" + itemName.replace(" ", "%20");
            HttpGet request = new HttpGet(url);
            String response = EntityUtils.toString(httpClient.execute(request).getEntity());
            
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            return json.get("median").getAsDouble();
        } catch (Exception e) {
            System.err.println("Failed to fetch price for " + itemName + ": " + e.getMessage());
            return 0;
        }
    }
}