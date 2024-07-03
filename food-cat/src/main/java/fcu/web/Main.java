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

        DateTimeFormatter format2=DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        String strNow2=format1.print(now);
        System.out.println(strNow2);

        String xmasDay="25/12/2024 00:00:00";
        DateTime xmas = format2.parseDateTime(xmasDay);
        System.out.println(xmas);

        String cnyDay="01-01-2024 00:00:00";
        DateTime cny=format1.parseDateTime(cnyDay);
        System.out.println(cny);

        DateTime day20 = now.plusDays(20);
        String strDay20=format.print(day20);
        System.out.println(strDay20); // now + 20days

        day20=xmas.plusDays(20);
        strDay20=format.print(day20);
        System.out.println(strDay20); // xmas + 20days

        DateTime day30=now.plusDays(30);
        String strDay30=format.print(day30);
        System.out.println(strDay30);

        day30=cny.plusDays(30);
        strDay30=format.print(day30);
        System.out.println(strDay30);
        System.out.println(day30);





    }
}