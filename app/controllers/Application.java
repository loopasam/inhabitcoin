package controllers;

import play.*;
import play.mvc.*;

import java.util.*;
import jobs.NewsUpdateJob;
import models.Article;


public class Application extends Controller {

    public static void index() {
        //Limit to a date here
        List<Article> articles = Article.q().filter("published <", new Date()).order("-published").asList();
        render(articles);
    }
    
    public static void newsUpdate() {
        new NewsUpdateJob().now();
        index();
    }

}