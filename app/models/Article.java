/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.google.code.morphia.annotations.Entity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import play.modules.morphia.Model;

/**
 *
 * @author loopasam
 */
@Entity
public class Article extends Model {

    public int feedzillaId;

    public String title;

    public Date published;

    public String source;

    public String sourceUrl;

    public String url;

    public String summary;

    public String author;

    public long stamp;

    public Article(String title, String published, String source, 
            String sourceUrl, String url, String summary, 
            String author, int feedzillaId) throws ParseException {
        
        //TODO remove parentheses
        this.title = title;
        //Transform as Date: Wed, 09 Jul 2014 13:03:00 +0100
        this.published = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss +SSSS", Locale.ENGLISH).parse(published);
        this.stamp = this.published.getTime();
        this.source = source;
        this.sourceUrl = sourceUrl;
        this.url = url;
        this.summary = summary;
        this.author = author;
        this.feedzillaId = feedzillaId;
    }

}
