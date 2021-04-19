package com.example.mars.util;

import com.example.mars.data.EntranceTestData;

import java.util.ArrayList;

public class MyTestData {

    public static ArrayList<EntranceTestData> getFinalTestData(int type) {
        switch (type) {
            case 1:
                return getCssFinalQuestion();
            case 2:
                return getHtmlFinalQuestion();
            case 3:
                return getJavaFinalQuestion();
            case 4:
                return getJavaScriptFinalQuestion();
            case 5:
                return getMySqlFinalQuestion();
        }
        return null;
    }
    public static ArrayList<EntranceTestData> getEntranceData(int type) {
        switch (type) {
            case 1:
                return getCssEntranceQuestion();
            case 2:
                return getHtmlEntranceQuestion();
            case 3:
                return getJavaEntranceQuestion();
            case 4:
                return getJavaScriptEntranceQuestion();

            case 5:
                return getMySqlEntranceQuestion();
        }
        return null;
    }

    private static ArrayList<EntranceTestData> getCssEntranceQuestion() {
        ArrayList<EntranceTestData> list = new ArrayList<>();
        ArrayList<String> q1Option = new ArrayList<>();
        q1Option.add("Cascade style sheets");
        q1Option.add("Color and style sheets");
        q1Option.add("Cascading style sheets");
        q1Option.add("All of the above");

        ArrayList<String> q2Option = new ArrayList<>();
        q2Option.add("bgcolor");
        q2Option.add("color");
        q2Option.add("background-color");
        q2Option.add("All of the above");


        list.add(new EntranceTestData("CSS stands for?", "A", "", q1Option));
        list.add(new EntranceTestData("The property in CSS used to change the text color of an element is", "B", "", q2Option));
        return list;
    }

    private static ArrayList<EntranceTestData> getHtmlEntranceQuestion() {
        ArrayList<EntranceTestData> list = new ArrayList<>();
        ArrayList<String> q1Option = new ArrayList<>();
        q1Option.add("HighText Machine Language");
        q1Option.add("HyperText and links Markup Language");
        q1Option.add("HyperTextMarkup Language");
        q1Option.add("None of these");

        ArrayList<String> q2Option = new ArrayList<>();
        q2Option.add("<pre>");
        q2Option.add("<a>");
        q2Option.add("<b>");
        q2Option.add("<br>");


        list.add(new EntranceTestData("HTML stands for?", "C", "", q1Option));
        list.add(new EntranceTestData("Which of the following element is responsible for making the text bold in HTML?", "C", "", q2Option));
        return list;
    }

    private static ArrayList<EntranceTestData> getJavaEntranceQuestion() {
        ArrayList<EntranceTestData> list = new ArrayList<>();
        ArrayList<String> q1Option = new ArrayList<>();
        q1Option.add("0");
        q1Option.add("Not a Number");
        q1Option.add("Infinity");
        q1Option.add("Run time exception");

        ArrayList<String> q2Option = new ArrayList<>();
        q2Option.add("for ( inti = 99; i>= 0; i / 9 )");
        q2Option.add("for ( inti = 7; i<= 77; i += 7 )");
        q2Option.add("for ( inti = 20; i>= 2; - -i )");
        q2Option.add("for ( inti = 2; i<= 20; i = 2* i )");


        list.add(new EntranceTestData("What does the expression float a = 35 / 0 return?", "C", "", q1Option));
        list.add(new EntranceTestData("Which of the following for loop declaration is not valid?", "A", "", q2Option));
        return list;
    }

    private static ArrayList<EntranceTestData> getJavaScriptEntranceQuestion() {
        ArrayList<EntranceTestData> list = new ArrayList<>();
        ArrayList<String> q1Option = new ArrayList<>();
        q1Option.add("Object-Oriented");
        q1Option.add("Object-Based");
        q1Option.add("Assembly-language");
        q1Option.add("High-level");

        ArrayList<String> q2Option = new ArrayList<>();
        q2Option.add("Alternative to if-else");
        q2Option.add("Switch statement");
        q2Option.add("If-then-else statement");
        q2Option.add("immediate if");


        list.add(new EntranceTestData("Which type of JavaScript language is?", "B", "", q1Option));
        list.add(new EntranceTestData("Which one of the following also known as Conditional Expression?", "D", "", q2Option));
        return list;
    }

    private static ArrayList<EntranceTestData> getMySqlEntranceQuestion() {
        ArrayList<EntranceTestData> list = new ArrayList<>();
        ArrayList<String> q1Option = new ArrayList<>();
        q1Option.add("DATE");
        q1Option.add("TIME");
        q1Option.add("DATETIME");
        q1Option.add("TIMESTAMP");

        ArrayList<String> q2Option = new ArrayList<>();
        q2Option.add("TEXT");
        q2Option.add("TINYTEXT");
        q2Option.add("CHAR");
        q2Option.add("TINYCHAR");


        list.add(new EntranceTestData("The … data type is responsible for sorting a combination of date and time information?", "C", "", q1Option));
        list.add(new EntranceTestData("The … is the data type of MySQL that supports a maximum length of 255 characters.", "B", "", q2Option));
        return list;
    }


    private static ArrayList<EntranceTestData> getCssFinalQuestion() {
        ArrayList<EntranceTestData> list = new ArrayList<>();
        ArrayList<String> q1Option = new ArrayList<>();
        q1Option.add("<style src = example.css>");
        q1Option.add("<style src = \"example.css\" >");
        q1Option.add("<stylesheet> example.css </stylesheet>");
        q1Option.add("<link rel=\"stylesheet\" type=\"text/css\" href=\"example.css\">");

        ArrayList<String> q2Option = new ArrayList<>();
        q2Option.add("text-decoration: line-through");
        q2Option.add("text-decoration: none");
        q2Option.add("text-decoration: overline");
        q2Option.add("text-decoration: underline");
        ArrayList<String> q3Option = new ArrayList<>();
        q3Option.add("visibility property");
        q3Option.add("background-clip property");
        q3Option.add("clip-path property");
        q3Option.add("None of the above");


        list.add(new EntranceTestData("Which of the following is the correct syntax for referring the external style sheet?", "D", "", q1Option));
        list.add(new EntranceTestData("The correct syntax to give a line over text is ", "C", "", q2Option));
        list.add(new EntranceTestData("Which of the following CSS property creates a clipping region and specifies the visible area of the element?", "C", "", q3Option));
        return list;
    }



    private static ArrayList<EntranceTestData> getHtmlFinalQuestion() {
        ArrayList<EntranceTestData> list = new ArrayList<>();
        ArrayList<String> q1Option = new ArrayList<>();
        q1Option.add("<a href = \"www.javatpoint.com\"> javaTpoint.com </a>");
        q1Option.add("<a url = \"www.javatpoint.com\" javaTpoint.com /a>");
        q1Option.add("<a link = \"www.javatpoint.com\"> javaTpoint.com </a>");
        q1Option.add("<a> www.javatpoint.com <javaTpoint.com /a>");

        ArrayList<String> q2Option = new ArrayList<>();
        q2Option.add("<imghref = \"jtp.png\" />");
        q2Option.add("<imgurl = \"jtp.png\" />");
        q2Option.add("<img link = \"jtp.png\" />");
        q2Option.add("<imgsrc = \"jtp.png\" />");

        ArrayList<String> q3Option = new ArrayList<>();
        q3Option.add("<marquee bgcolor: \"red\">");
        q3Option.add("<marquee bg-color = \"red\">");
        q3Option.add("<marquee bgcolor = \"red\">");
        q3Option.add("<marquee color = \"red\">");


        list.add(new EntranceTestData("How to create a hyperlink in HTML?", "A", "", q1Option));
        list.add(new EntranceTestData("How to insert an image in HTML?", "D", "", q2Option));
        list.add(new EntranceTestData("How to add a background color in HTML?", "D", "", q3Option));
        return list;
    }

    private static ArrayList<EntranceTestData> getJavaFinalQuestion() {
        ArrayList<EntranceTestData> list = new ArrayList<>();
        ArrayList<String> q1Option = new ArrayList<>();
        q1Option.add("Unicode escape sequence");
        q1Option.add("Octal escape");
        q1Option.add("Hexadecimal");
        q1Option.add("Line feed");

        ArrayList<String> q2Option = new ArrayList<>();
        q2Option.add("ABH8097");
        q2Option.add("L990023");
        q2Option.add("904423");
        q2Option.add("0xnf029L");
        ArrayList<String> q3Option = new ArrayList<>();
        q3Option.add("It has only methods");
        q3Option.add("Objects can't be created");
        q3Option.add("It has a fixed class name");
        q3Option.add("It has no class name");


        list.add(new EntranceTestData("The \\u0021 article referred to as a?", "A", "", q1Option));
        list.add(new EntranceTestData("Which of the following is a valid long literal?", "D", "", q2Option));
        list.add(new EntranceTestData("Which of the following is true about the anonymous inner class?", "D", "", q3Option));
        return list;
    }

    private static ArrayList<EntranceTestData> getJavaScriptFinalQuestion() {
        ArrayList<EntranceTestData> list = new ArrayList<>();
        ArrayList<String> q1Option = new ArrayList<>();
        q1Option.add("Prints an exception error");
        q1Option.add("Prints an overflow error");
        q1Option.add("Displays \"Infinity\"");
        q1Option.add("Prints the value as such");

        ArrayList<String> q2Option = new ArrayList<>();
        q2Option.add("Syntax error");
        q2Option.add("Missing of semicolons");
        q2Option.add("Division by zero");
        q2Option.add("Missing of Bracket");

        ArrayList<String> q3Option = new ArrayList<>();
        q3Option.add("toExponential()");
        q3Option.add("toFixed()");
        q3Option.add("toPrecision()");
        q3Option.add("toLocaleString()");


        list.add(new EntranceTestData("When there is an indefinite or an infinite value during an arithmetic computation in a program, then JavaScript prints?", "C", "", q1Option));
        list.add(new EntranceTestData("In the JavaScript, which one of the following is not considered as an error?", "C", "", q2Option));
        list.add(new EntranceTestData("Which of the following givenfunctions of the Number Object formats a number with a different number of digits to the right of the decimal?", "C", "", q3Option));
        return list;
    }

    private static ArrayList<EntranceTestData> getMySqlFinalQuestion() {
        ArrayList<EntranceTestData> list = new ArrayList<>();
        ArrayList<String> q1Option = new ArrayList<>();
        q1Option.add("Demand and Supply Responsiveness");
        q1Option.add("Storage, Functionality Responsiveness to queries");
        q1Option.add("Transaction Responsiveness");
        q1Option.add("None of these");

        ArrayList<String> q2Option = new ArrayList<>();
        q2Option.add("DEFAULT");
        q2Option.add("INDEX");
        q2Option.add("NATIONAL");
        q2Option.add("NULL");

        ArrayList<String> q3Option = new ArrayList<>();
        q3Option.add("Data Mining");
        q3Option.add("Data Management");
        q3Option.add("Meta Data");
        q3Option.add("Database");


        list.add(new EntranceTestData("Data Warehouse provides: ", "B", "", q1Option));
        list.add(new EntranceTestData("The … attribute used in MySQL data type is offered as an aid in the database compatibility.", "C", "", q2Option));
        list.add(new EntranceTestData("----- is the process of extracting previously non known valid and actionable information from large data to make crucial business and strategic decisions.", "A", "", q3Option));
        return list;
    }

}
