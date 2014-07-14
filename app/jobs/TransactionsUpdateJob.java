/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jobs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import models.Article;
import models.Transaction;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.libs.WS;

/**
 *
 * @author loopasam
 */
@Every("2s")
public class TransactionsUpdateJob extends Job {

    @Override
    public void doJob() throws Exception {
        Logger.info("Update transactions...");

        //MorphiaFixtures.delete(Transaction.class);
        //GET https://www.bitstamp.net/api/transactions/?time=minute
        WS.HttpResponse res = WS.url("https://www.bitstamp.net/api/transactions/?time=minute").get();
        JsonArray transactions = res.getJson().getAsJsonArray();
        for (JsonElement articleEle : transactions) {
            JsonObject transaction = articleEle.getAsJsonObject();

            int tid = transaction.get("tid").getAsInt();
            String price = transaction.get("price").getAsString();
            String date = transaction.get("date").getAsString();
            String amount = transaction.get("amount").getAsString();

            if (Transaction.find("tid", tid).first() == null) {
                Logger.info("New transaction: " + tid);
                new Transaction(tid, date, price, amount).save();
            } else {
                Logger.info("Transaction already exists: " + tid);
            }
        }
    }

}
