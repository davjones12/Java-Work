package com.example.companyemployeemanagementsystem;

/**
 *
 * @author David.Jones
 */

public class UserAccount {

   private String ssn;
   private String midinit;
   private String dob;
   private String firstname;
   private String lastname;
   private String address;
   private String sex;
   private String salary;
   private String mgssn;
   private String dno;

   public UserAccount(String ssn, String firstname, String lastname,
           String midinit, String address, String sex, String dob, 
           String salary, String mgssn, String dno) {
       this.ssn = ssn;
       this.firstname = firstname;
       this.lastname = lastname;
       this.midinit = midinit;
       this.address = address;
       this.sex = sex;
       this.dob = dob;
       this.salary = salary;
       this.mgssn = mgssn;
       this.dno = dno;
   }

   public String getSsn() {
       return ssn;
   }

   public String getFirstname() {
       return firstname;
   }

   public String getLastname() {
       return lastname;
   }

   public String getMidinit() {
       return midinit;
   }
   
   public String getAddress() {
       return address;
   }
   
   public String getSex() {
       return sex;
   }
   
   public String getSalary() {
       return salary;
   }
   
   public String getMgssn() {
       return mgssn;
   }
   
   public String getDno() {
       return dno;
   }
   
   public String getDob() {
       return dob;
   }
   
   public void setSsn(String ssn) {
       this.ssn = ssn;
   }

   public void setFirstname(String firstname) {
       this.firstname = firstname;
   }

   public void setLastname(String lastname) {
       this.lastname = lastname;
   }
   
   public void setMidinit(String midinit) {
       this.midinit = midinit;
   }
   
   public void setAddress(String address) {
       this.address = address;
   }
   
   public void setSex(String sex) {
       this.sex = sex;
   }
   
   public void setDob(String dob) {
       this.dob = dob;
   }
   
   public void setSalary(String salary) {
       this.salary = salary;
   }
   
   public void setMgssn(String mgssn) {
       this.mgssn = mgssn;
   }
   
   public void setDno(String dno) {
       this.dno = dno;
   }
}
