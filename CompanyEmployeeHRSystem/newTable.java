/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.companyemployeemanagementsystem;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.Statement;
// import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static com.example.companyemployeemanagementsystem.MainAccess.*;

/**
 *
 * @author David.Jones
 */
public class newTable {
    // Drop current table and create new one - can also be used to reset
    public static void createTable() {
        Connection conn = LocalinitialSetup(0);
        //System.out.println("option 0");
        try
        {
            Statement st = conn.createStatement();
            st.executeUpdate("DROP DATABASE IF EXISTS Company");
            st.executeUpdate("CREATE DATABASE Company");
            st.close();
            conn.close();
            //System.out.println("New COMPANY Database successfully created.");
        }
        catch(Exception ex) 
        {
            System.err.println(ex);
        }

        Connection conn1 = LocalinitialSetup(1);

        try
        {
            Statement st1 = conn1.createStatement();
            st1.executeUpdate("CREATE TABLE DEPARTMENT (\n" +
            "  dname        varchar(25) not null,\n" +
            "  dnumber      int not null,\n" +
            "  mgrssn      char(9) not null, \n" +
            "  mgrstartdate date,\n" +
            "  CONSTRAINT pk_Department primary key (dnumber),\n" +
            "  CONSTRAINT uk_dname UNIQUE (dname) \n" +
            ");");
            st1.close();
            conn1.close();
            //System.out.println("New DEPARTMENT Table successfully created in COMPANY Database.");
        }
        catch(Exception ex) 
        {
            System.err.println(ex);
        }

        Connection conn2 = LocalinitialSetup(1);

        try
        {
            Statement st2 = conn2.createStatement();
            st2.executeUpdate("CREATE TABLE EMPLOYEE (\n" +
            "  fname    varchar(15) not null, \n" +
            "  minit    varchar(1),\n" +
            "  lname    varchar(15) not null,\n" +
            "  ssn     char(9), \n" +
            "  bdate    date,\n" +
            "  address  varchar(50),\n" +
            "  sex      char,\n" +
            "  salary   decimal(10,2),\n" +
            "  superssn char(9),\n" +
            "  dno      int,\n" +
            "  CONSTRAINT pk_employee primary key (ssn),\n" +
            "  #'--CONSTRAINT fk_employee_employee foreign key (superssn) references EMPLOYEE(ssn), -- Constraint Will be added later to prevent cyclic referential itegrity violation'\n" +
            "  CONSTRAINT fk_employee_department foreign key (dno) references DEPARTMENT(dnumber)\n" +
            ");");
            st2.close();
            conn2.close();
            //System.out.println("New EMPLOYEE Table successfully created in COMPANY Database.");
        }
        catch(Exception ex) 
        {
            System.err.println(ex);
        }

        Connection conn3 = LocalinitialSetup(1);

        try
        {
            Statement st3 = conn3.createStatement();
            st3.executeUpdate("CREATE TABLE DEPENDENT (\n" +
            "  essn           char(9),\n" +
            "  dependent_name varchar(15),\n" +
            "  sex            char,\n" +
            "  bdate          date,\n" +
            "  relationship   varchar(8),\n" +
            "  CONSTRAINT pk_essn_dependent_name primary key (essn,dependent_name),\n" +
            "  CONSTRAINT fk_dependent_employee foreign key (essn) references EMPLOYEE(ssn)\n" +
            ")");
            st3.close();
            conn3.close();
            //System.out.println("New DEPENDENT Table successfully created in COMPANY Database.");
        }
        catch(Exception ex) 
        {
            System.err.println(ex);
        }

        Connection conn4 = LocalinitialSetup(1);

        try
        {
            Statement st4 = conn4.createStatement();
            st4.executeUpdate("CREATE TABLE DEPT_LOCATIONS (\n" +
            "  dnumber   int,\n" +
            "  dlocation varchar(15), \n" +
            "  CONSTRAINT pk_dept_locations primary key (dnumber,dlocation),\n" +
            "  CONSTRAINT fk_deptlocations_department foreign key (dnumber) references DEPARTMENT(dnumber)\n" +
            ")");
            st4.close();
            conn4.close();
            //System.out.println("New DEPT_LOCATIONS Table successfully created in COMPANY Database.");
        }
        catch(Exception ex) 
        {
            System.err.println(ex);
        }

        Connection conn5 = LocalinitialSetup(1);

        try
        {
            Statement st5 = conn5.createStatement();
            st5.executeUpdate("CREATE TABLE PROJECT (\n" +
            "  pname      varchar(25) not null,\n" +
            "  pnumber    int,\n" +
            "  plocation  varchar(15),\n" +
            "  dnum       int not null,\n" +
            "  CONSTRAINT ok_project primary key (pnumber),\n" +
            "  CONSTRAINT uc_pnumber unique (pname),\n" +
            "  CONSTRAINT fk_project_department foreign key (dnum) references DEPARTMENT(dnumber)\n" +
            ")");
            st5.close();
            conn5.close();
            //System.out.println("New PROJECT Table successfully created in COMPANY Database.");
        }
        catch(Exception ex) 
        {
            System.err.println(ex);
        }

        Connection conn6 = LocalinitialSetup(1);

        try
        {
            Statement st6 = conn6.createStatement();
            st6.executeUpdate("CREATE TABLE WORKS_ON (\n" +
            "  essn   char(9),\n" +
            "  pno    int,\n" +
            "  hours  decimal(4,1),\n" +
            "  CONSTRAINT pk_worksOn primary key (essn,pno),\n" +
            "  CONSTRAINT fk_workson_employee foreign key (essn) references EMPLOYEE(ssn),\n" +
            "  CONSTRAINT fk_workson_project foreign key (pno) references PROJECT(pnumber)\n" +
            ")");
            st6.close();
            conn6.close();
            //System.out.println("New WORKS_ON Table successfully created in COMPANY Database.");
        }
        catch(Exception ex) 
        {
            System.err.println(ex);
        }

        // Insert Records
        insertData("DEPARTMENT", "department.txt");
        insertData("EMPLOYEE", "employee.txt");
        insertData("DEPT_LOCATIONS", "dept_loc.txt");
        insertData("PROJECT", "project.txt");
        insertData("WORKS_ON", "works_on.txt");
        insertData("DEPENDENTS", "dependents.txt");

        Connection conn7 = LocalinitialSetup(1);

        try
        {
            Statement st7 = conn7.createStatement();
            st7.executeUpdate("Alter table EMPLOYEE\n" +
            "ADD CONSTRAINT fk_employee_employee foreign key (superssn) references EMPLOYEE(ssn);");
            st7.close();
            conn7.close();
            //System.out.println("FK constraint added.");
        }
        catch(Exception ex) 
        {
            System.err.println(ex);
        }
    }
    
}
