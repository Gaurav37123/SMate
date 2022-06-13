package com.example.smate.Classes;

public class UserProfile
{
    private String PhoneNumber;
    private String USN,Name;
    private String Branch,Year;

    public UserProfile(String phoneNumber, String USN, String name, String branch, String year) {
        PhoneNumber = phoneNumber;
        this.USN = USN;
        Name = name;
        Branch = branch;
        Year = year;
    }

    public UserProfile() {
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUSN() {
        return USN;
    }

    public void setUSN(String USN) {
        this.USN = USN;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }
}
