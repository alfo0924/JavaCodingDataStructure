package fcu.web;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args)
    {
        System.out.println("Hello world!");
        Student tom=new Student("male","ComputerScienese",3318);
        System.out.println(tom.toString());

        DateTime now = new DateTime();
        System.out.println(now.toString());
        System.out.println(now);


        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String strNow=format.print(now);
        System.out.println(strNow);

        DateTimeFormatter format1=DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
        String strNow1=format1.print(now);
        System.out.println(strNow1);

    }
}