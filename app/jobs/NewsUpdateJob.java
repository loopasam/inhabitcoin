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
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.libs.WS;
import play.libs.WS.HttpResponse;

/**
 *
 * @author loopasam
 */
//@Every("1mn")
public class NewsUpdateJob extends Job {

    @Override
    public void doJob() throws Exception {
        Logger.info("Job started");
        
        //MorphiaFixtures.delete(Article.class);
        //GET http://api.feedzilla.com/v1/articles/search.json?q=bitcoin&order=date&since=2014-07-06&count=50

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = dateFormat.format(cal.getTime());

        HttpResponse res = WS.url("http://api.feedzilla.com/v1/articles/search.json?q=bitcoin&order=date&since=" + yesterday + "&count=50").get();
        JsonObject json = res.getJson().getAsJsonObject();
        JsonArray articles = json.getAsJsonArray("articles");
        for (JsonElement articleEle : articles) {
            JsonObject article = articleEle.getAsJsonObject();

            String title = article.get("title").getAsString();
            String summary = article.get("summary").getAsString();
            String source = article.get("source").getAsString();
            String sourceUrl = article.get("source_url").getAsString();
            String published = article.get("publish_date").getAsString();
            String author = null;
            if (article.get("author") != null) {
                author = article.get("author").getAsString();
            }
            String url = article.get("url").getAsString();
            
            int feedzillaId = 0;
            Pattern p = Pattern.compile("([0-9]+)");
            Matcher m = p.matcher(url);
            if (m.find()) {
                feedzillaId = Integer.parseInt(m.group(1));
            }

            //Just add the article if not already existing
            if(Article.find("feedzillaId", feedzillaId).first() == null){
                Logger.info("New article: " + feedzillaId);
                Utils.emailAdmin("News: " + title, summary);
                new Article(title, published, source, sourceUrl, url, summary, author, feedzillaId).save();
                //TODO: Take the bitcoin value too
            }else{
                Logger.info("Article already exists: " + feedzillaId);
            }
            
        }
    }

}
