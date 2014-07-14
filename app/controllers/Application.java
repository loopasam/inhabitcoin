package controllers;

import java.text.SimpleDateFormat;
import java.util.*;
import jobs.NewsUpdateJob;
import jobs.TransactionsUpdateJob;
import models.Article;
import models.Transaction;
import play.*;
import play.mvc.*;

public class Application extends Controller {

    public static void index() {
        //Limit to a date here
        List<Article> articles = Article.q().filter("published <", new Date()).order("-published").limit(5).asList();
        render(articles);
    }

    public static void transactions() {
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        today.add(Calendar.MINUTE, -60);

        List<Transaction> transactions = Transaction.q().filter("date >", today.getTime()).order("-date").asList();
        renderJSON(transactions);
    }

    public static void newsUpdate() {
        new NewsUpdateJob().now();
        index();
    }

    public static void transactionsUpdate() {
        new TransactionsUpdateJob().now();
        index();
    }

}
