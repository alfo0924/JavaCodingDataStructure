package fcu.web;

import org.joda.time.DateTime;

public class Main {
    public static void main(String[] args)
    {
        System.out.println("Hello world!");
        Student tom=new Student("male","ComputerScienese",3318);
        System.out.println(tom.toString());

        DateTime now = new DateTime();
        System.out.println(now.toString());
        System.out.println(now);

    }
}