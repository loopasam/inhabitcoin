/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import com.google.code.morphia.annotations.Entity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import play.modules.morphia.Model;

/**
 *
 * @author loopasam
 */
@Entity
public class Transaction extends Model {
    
    public Date date;
    
    public long stamp;
    
    public int tid;
    
    public String price;
    
    public String amount;

    public Transaction(int tid, String dateString, String price, String amount) {
        this.date = new Date(Long.parseLong(dateString)* 1000);
        this.stamp = this.date.getTime();
        this.tid = tid;
        this.price = price;
        this.amount = amount;
    }
    
    
}
