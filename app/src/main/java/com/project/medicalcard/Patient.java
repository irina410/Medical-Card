package com.project.medicalcard;

public class Patient {
    private String name;
    private String lastname;
    private String fathername;
    private String dateOfB;
    private String typeOfBl;
    private String allerg;
    private String phone;
    private String email;
    private String policynumb;
    private String diseases;

    public String getPersonalPhone() {
        return personalPhone;
    }

    public void setPersonalPhone(String personalPhone) {
        this.personalPhone = personalPhone;
    }

    private String personalPhone;


    public Patient() {
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFathername() {
        return fathername;
    }

    public String getEmail() {
        return email;
    }

    public String getInsurance_policy_number() {
        return policynumb;
    }

       public String getDateOfB() {
        return dateOfB;
    }

    public String getTypeOfBl() {
        return typeOfBl;
    }

    public String getAllerg() {
        return allerg;
    }

    public String getPhone() {
        return phone;
    }

    public String getDiseases() {
        return diseases;
    }

    public String getPolicynumb() {
        return policynumb;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setInsurance_policy_number(String ipn) {
        this.policynumb = ipn;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfB(String dateOfB) {
        this.dateOfB = dateOfB;
    }

    public void setTypeOfBl(String typeOfBl) {
        this.typeOfBl = typeOfBl;
    }

    public void setAllerg(String allerg) {
        this.allerg = allerg;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPolicynumb(String policynumb) {
        this.policynumb = policynumb;
    }
}
