package de.thu.excursionapp;

public class Data {
    public static final String Database_Name = "excursion_manager.db";
    public static final String Student_Table_Name = "students";
    public static final String Organiser_Table_Name = "organisers";
    public static final String Excursion_Table_Name = "excursions";
    public static final String Booking_Table_Name = "bookings";
    public static final String Student_Col1 = "username";
    public static final String Student_Col2 = "password";
    public static final String Organiser_Col1 = "username";
    public static final String Organiser_Col2 = "password";
    public static final String Excursion_Col1 = "title";
    public static final String Excursion_Col2 = "description";
    public static final String Excursion_Col3 = "picture";
    public static final String Booking_Col1 = "student";
    public static final String Booking_Col2 = "excursion";

    public static final String Student_Table = "Create Table "+Student_Table_Name+ "(" +
            Student_Col1 + " Text Primary Key, \n"
            + Student_Col2 + " Text)";
    public static final String Organiser_Table = "Create Table "+Organiser_Table_Name+ "(" +
            Organiser_Col1 + " Text Primary Key, \n"
            + Organiser_Col2 + " Text)";
    public static final String Excursion_Table = "Create Table "+Excursion_Table_Name+ "(" +
            Excursion_Col1 + " Text Primary Key, \n"
            + Excursion_Col2 + " Text, \n"
        + Excursion_Col3 +" Text Default '')";
    public static final String Booking_Table = "Create Table "+Booking_Table_Name+ "(" +
            Booking_Col1 + "  Text, \n"
            + Booking_Col2 + " Text, "
            + "Primary Key("+Booking_Col1+", "+Booking_Col2+"))";

   /* public static final String Booking_Table = "Create Table "+Booking_Table_Name+ "(" +
            Booking_Col1 + "  Text, \n"
            + Booking_Col2 + " Text, \n"
            +  "Primary Key("
           + Booking_Col1
           + ", "+Booking_Col2+") \n"
            +"Foreign Key ("+Booking_Col1+") References "+Student_Table_Name+"("+Student_Col1+"), \n"
            + "Foreign Key ("+Booking_Col2+") References "+Excursion_Table_Name+"("+Excursion_Col1+");";*/

    public static final String DropStudent = "Drop Table If Exists "+ Student_Table_Name;
    public static final String DropOrganiser = "Drop Table If Exists "+ Organiser_Table_Name;
    public static final String DropExcursion = "Drop Table If Exists "+ Organiser_Table_Name;
    public static final String DropBooking = "Drop Table If Exists "+ Booking_Table_Name;
}
