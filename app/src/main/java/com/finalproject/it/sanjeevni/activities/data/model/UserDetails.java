package com.finalproject.it.sanjeevni.activities.data.model;

import java.util.Date;

public class UserDetails {
    String first_name, last_name, emailid,present_city, gender, password, date_of_birth;
    int mob_no;

    public UserDetails(String first_name, String last_name, String emailid, int mob_no, String present_city,
                       String gender, String date_of_birth,String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.emailid = emailid;
        this.mob_no = mob_no;
        this.present_city = present_city;
        this.gender = gender;
        this.date_of_birth=date_of_birth;
        this.password=password;
    }

    public String getFirst_Name() {return first_name;}
    public void setFirst_name(String name) {this.first_name = name;}

    public String getLast_name() {return last_name;}
    public void setLast_name(String name) {this.last_name = name;}

    public String getEmailid() {return emailid;}
    public void setEmailid(String name) {this.emailid = name;}

    public int getMob_no() {return mob_no;}
    public void setMob_no(int name) {this.mob_no = name;}

    public String getPresent_city() {return present_city;}
    public void setPresent_city(String name) {this.present_city = name;}

    public String getGender() {return gender;}
    public void setGender(String gender) {this.gender = gender;}

    public String getDate_of_birth() {return date_of_birth;}
    public void setDate_of_birth(String name) {this.date_of_birth = name;}

    public String getPassword() {return password;}
    public void setPassword(String name) {this.password = name;}

}
