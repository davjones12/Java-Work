/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.companyemployeemanagementsystem;

import static com.example.companyemployeemanagementsystem.MainAccess.*;
import static com.example.companyemployeemanagementsystem.MainAccess.LocalinitialSetup;
import static com.example.companyemployeemanagementsystem.MainAccess.createRegistrationFormPane;
import static com.example.companyemployeemanagementsystem.MainAccess.finalReport;
import static com.example.companyemployeemanagementsystem.MainAccess.mainPage;
import static com.example.companyemployeemanagementsystem.MainAccess.showAlert;
import static com.example.companyemployeemanagementsystem.MainAccess.viewEmployeeTable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 *
 * @author David.Jones
 */

class FormInputException extends Exception  
{  
    public FormInputException(String str)  
    {  
        super(str);  
    }  
}  

public class editUserAccount {
    
    public static void editEmployee(GridPane gridPane, Stage primaryStage, int mgssn, int empssn) {
        // Add Header
        Label headerLabel = new Label("Edit Employee Form");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.LEFT);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        String ssn = String.valueOf(empssn);
        String fname = "";
        String lname = "";
        String mint = "";
        String dob = "";
        String addr = "";
        String sex = "";
        int salary = 0;
        String superssn = "";
        int dno = 0;
        String email = "";
        
        String sql = "SELECT fname AS FIRST_NAME, lname AS LAST_NAME, minit AS M_NAME,\r\n" + 
        				"bdate AS BDAY, address AS ADDRESS_1, sex AS SEX_1, salary AS SAL, superssn AS SUPER,\r\n" + 
        				"dno AS DEPT\r\n" +
        				"FROM EMPLOYEE\r\n" + 
        				"WHERE ssn = " + ssn + "";
        
        try
        {
            Connection conn = LocalinitialSetup(1);
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(sql);

            while(res.next()){
                fname = res.getString("FIRST_NAME");
                lname = res.getString("LAST_NAME");
                mint = res.getString("M_NAME");
                dob = res.getString("BDAY");
                addr = res.getString("ADDRESS_1");
                sex = res.getString("SEX_1");
                salary = res.getInt("SAL");;
                superssn = res.getString("SUPER");
                dno = res.getInt("DEPT");;
                //email = res.getString("EMAIL_1");
            }

            res.close();
            st.close();
            conn.close(); 
        }
        catch(Exception ex) 
        { 
            System.err.println(ex); 
        }

        Label fnameLabel = new Label("First Name : ");
        gridPane.add(fnameLabel, 0,1);

        TextField fnameField = new TextField(fname);
        fnameField.setPrefHeight(40);
        gridPane.add(fnameField, 1,1);
        
        Label lnameLabel = new Label("Last Name : ");
        gridPane.add(lnameLabel, 0,2);

        TextField lnameField = new TextField(lname);
        lnameField.setPrefHeight(40);
        gridPane.add(lnameField, 1,2);
        
        Label mnameLabel = new Label("Middle Initial : ");
        gridPane.add(mnameLabel, 0,3);

        TextField mnameField = new TextField(mint);
        mnameField.setPrefHeight(40);
        gridPane.add(mnameField, 1,3);
        
        Label ssnLabel = new Label("Social Security # : ");
        gridPane.add(ssnLabel, 0,4);

        //TextField ssnField = new TextField();
        //ssnField.setPrefHeight(40);
        //gridPane.add(ssnField, 1,4);
        
        Label ssnLabel2 = new Label(ssn); // Can't edit SSN!
        gridPane.add(ssnLabel2, 1,4);
        
        Label dobLabel = new Label("Date of Birth :\n(YYYY-MM-DD) ");
        gridPane.add(dobLabel, 0,5);

        DatePicker datePicker = new DatePicker(LocalDate.parse(dob));
        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            System.out.println("Selected date: " + date);
        });
        String pattern = "yyyy-MM-dd";

        datePicker.setPromptText(pattern.toLowerCase());

        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        datePicker.setPrefHeight(40);
        gridPane.add(datePicker, 1, 5);
        
        Label addrLabel = new Label("Address : ");
        gridPane.add(addrLabel, 0,6);

        TextField addrField = new TextField(addr);
        addrField.setPrefHeight(40);
        gridPane.add(addrField, 1,6);
        
        Label sexLabel = new Label("Sex : ");
        gridPane.add(sexLabel, 0,7);

        final ToggleGroup sexgroup = new ToggleGroup();
        RadioButton male = new RadioButton("Male");
        male.setToggleGroup(sexgroup);

        RadioButton female = new RadioButton("Female");
        female.setToggleGroup(sexgroup);

        if(sex.equals("F")) {
            male.setSelected(false);
            GridPane.setHalignment(male, HPos.LEFT);
            gridPane.add(male, 1, 7);

            female.setSelected(true);
            GridPane.setHalignment(female, HPos.LEFT);
            gridPane.add(female, 1, 8);
        } else {
            male.setSelected(true);
            GridPane.setHalignment(male, HPos.LEFT);
            gridPane.add(male, 1, 7);

            female.setSelected(false);
            GridPane.setHalignment(female, HPos.LEFT);
            gridPane.add(female, 1, 8);
        }
        
        Label salaryLabel = new Label("Salary : ");
        gridPane.add(salaryLabel, 0,9);

        TextField salaryField = new TextField(String.valueOf(salary));
        salaryField.setPrefHeight(40);
        gridPane.add(salaryField, 1,9);

        Label superLabel = new Label("Supervisor's SSN : ");
        gridPane.add(superLabel, 0,10);

        TextField superField = new TextField(superssn);
        superField.setPrefHeight(40);
        superField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    superField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        gridPane.add(superField, 1,10);
        
        Label depLabel = new Label("Dept # : ");
        gridPane.add(depLabel, 0,11);

        TextField depField = new TextField(String.valueOf(dno));
        depField.setPrefHeight(40);
        depField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    depField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        gridPane.add(depField, 1,11);

        Button submitButton = new Button("Update");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 1, 12, 2, 1);
        GridPane.setHalignment(submitButton, HPos.LEFT);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));
        
        
        Button homeButton = new Button("Go Back Home");
        homeButton.setPrefHeight(20);
        homeButton.setDefaultButton(true);
        gridPane.add(homeButton, 1, 13, 1, 1);
        GridPane.setHalignment(homeButton, HPos.LEFT);
        GridPane.setMargin(homeButton, new Insets(2,0,2,0));

        homeButton.setOnAction((ActionEvent event) -> {
            GridPane gridPane_1 = createRegistrationFormPane();
            mainPage(gridPane_1,primaryStage, mgssn);
            ScrollPane sp = new ScrollPane(gridPane_1);
            Scene scene = new Scene(sp, 800, 500);
            primaryStage.setScene(scene);
            primaryStage.show();
        });
        
        Button viewEmpButton = new Button("View All Employees");
        viewEmpButton.setPrefHeight(20);
        viewEmpButton.setDefaultButton(true);
        gridPane.add(viewEmpButton, 1, 14, 1, 1);
        GridPane.setHalignment(viewEmpButton, HPos.LEFT);
        GridPane.setMargin(viewEmpButton, new Insets(2,0,2,0));

        viewEmpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	GridPane gridPane_1 = createRegistrationFormPane();
                viewEmployeeTable(gridPane_1,primaryStage, mgssn);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        
        Button viewEmpButton2 = new Button("Edit Dependents");
        viewEmpButton2.setPrefHeight(20);
        viewEmpButton2.setDefaultButton(true);
        gridPane.add(viewEmpButton2, 3, 14, 1, 1);
        GridPane.setHalignment(viewEmpButton2, HPos.LEFT);
        GridPane.setMargin(viewEmpButton2, new Insets(2,0,2,0));

        viewEmpButton2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	GridPane gridPane_1 = createRegistrationFormPane();
                editEmployeeDependents(gridPane_1,primaryStage, mgssn, empssn);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        
        Button viewEmpButton3 = new Button("Edit Work Projects");
        viewEmpButton3.setPrefHeight(20);
        viewEmpButton3.setDefaultButton(true);
        gridPane.add(viewEmpButton3, 2, 14, 1, 1);
        GridPane.setHalignment(viewEmpButton3, HPos.LEFT);
        GridPane.setMargin(viewEmpButton3, new Insets(2,0,2,0));

        viewEmpButton3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	GridPane gridPane_1 = createRegistrationFormPane();
                editUserProject(gridPane_1,primaryStage, mgssn, empssn);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        
        Button viewEmpButton4 = new Button("Create Report");
        viewEmpButton4.setPrefHeight(20);
        viewEmpButton4.setDefaultButton(true);
        gridPane.add(viewEmpButton4, 2, 13, 1, 1);
        GridPane.setHalignment(viewEmpButton4, HPos.LEFT);
        GridPane.setMargin(viewEmpButton4, new Insets(2,0,2,0));

        viewEmpButton4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane gridPane_1 = createRegistrationFormPane();
                finalReport(gridPane_1, primaryStage, mgssn, empssn);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                // Set the scene in primary stage
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        
        Label manLabel = new Label("Manager Logged In:\n" + mgssn + "\n\nEmployee being edited:\n" + empssn);
        gridPane.add(manLabel, 1,16,2,1);
        
        submitButton.setOnAction((ActionEvent event) -> {
            if(fnameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your first name");
                return;
            }
            if(lnameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your last name");
                return;
            }
            /*if(ssnField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your Social Security #");
                return;
            }*/
            String fname1 = fnameField.getText();
            String lname1 = lnameField.getText();
            String mint1 = mnameField.getText();
            String ssn1 = ssn;
            String dob1 = String.valueOf(datePicker.getValue());
            String addr1 = addrField.getText();
            String sex1 = "";
            if (male.isSelected()) {
                sex1 = "M";
            } else if (female.isSelected()) {
                sex1 = "F";
            }
            //int salary1 = Integer.valueOf(salaryField.getText());
            int salary1 = 0;
            String s = salaryField.getText();
            double d = ParseDouble(s);
            int a = (int) d;
            salary1 = a;
            String superssn1 = superField.getText();
            int dno1 = Integer.valueOf(depField.getText());
            //String email = emailField.getText();
            try {
                Connection conn = LocalinitialSetup(1);
                Statement st = conn.createStatement();
                String sql1 = "UPDATE EMPLOYEE SET fname = ?, minit = ?, lname = ?, bdate = ?, address = ?, sex = ?, salary = ?, superssn = ?, dno = ? WHERE ssn = ?";
                //String sql1 = "INSERT INTO EMPLOYEE VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pt = conn.prepareStatement(sql1);
                if (fname1 != "") {
                    pt.setString(1, fname1);
                } else {
                    //pt.setNull(1, Types.VARCHAR);
                    throw new FormInputException("First name required");
                }
                if (lname1 != "") {
                    pt.setString(3, lname1);
                } else {
                    //pt.setNull(3, Types.VARCHAR);
                    throw new FormInputException("Last name required");
                }
                if (mint1 != "") {
                    pt.setString(2, mint1);
                } else {
                    pt.setNull(2, Types.VARCHAR);
                }
                if (ssn1 != "") {
                    pt.setString(10, ssn1);
                } else {
                    //pt.setNull(4, Types.VARCHAR);
                    throw new FormInputException("SSN required");
                }
                if (dob1 != "") {
                    pt.setString(4, dob1);
                } else {
                    pt.setNull(4, Types.DATE);
                }
                if (addr1 != "") {
                    pt.setString(5, addr1);
                } else {
                    pt.setNull(5, Types.VARCHAR);
                }
                if (sex1 != "") { 
                    pt.setString(6, "M");
                } else {
                    pt.setNull(6, Types.VARCHAR);
                }
                if (salary1 >= 0) { 
                    pt.setInt(7, salary1);
                } else {
                    pt.setNull(7, Types.INTEGER);
                }
                if (superssn1 != "") {
                    pt.setString(8, superssn1);
                } else {
                    pt.setNull(8, Types.VARCHAR);
                }
                if (dno1 >= 0) {
                    pt.setInt(9, dno1);
                } else {
                    //pt.setNull(10, Types.INTEGER);
                    throw new FormInputException("DNO required");
                }

                pt.executeUpdate();
                pt.close();
                st.close();
                conn.close();
                showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Employee Updated Successfully!", "Name: " + fnameField.getText() + " " + lnameField.getText());
                //empssn = Integer.valueOf(ssn);
            }catch(Exception ex) 
            {
                showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Employee NOT Updated!", "Please try again.\n" + ex);
                System.err.println(ex);
                return;
            }
            GridPane gridPane_1 = createRegistrationFormPane();
            editUserProject(gridPane_1, primaryStage, mgssn, Integer.parseInt(ssn1));
            ScrollPane sp = new ScrollPane(gridPane_1);
            Scene scene = new Scene(sp, 800, 500);
            // Set the scene in primary stage
            primaryStage.setScene(scene);
            primaryStage.show();
            // go to next screen
        });
    }
    
    private static double ParseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
               return Double.parseDouble(strNumber);
            } catch(Exception e) {
               return 0;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        }
        else return 0;
    }
    
    // Assign projects based on developer role
    public static void editUserProject(GridPane gridPane, Stage primaryStage, int ssn_1, int emp_ssn) {
        Label headerLabel = new Label("Edit Employee Projects Form");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,4,1);
        GridPane.setHalignment(headerLabel, HPos.LEFT);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));
        
        int ssn = ssn_1; // this is the manager

        Label fnameLabel = new Label("Select project to assign to:");
        GridPane.setHalignment(fnameLabel, HPos.LEFT);
        gridPane.add(fnameLabel, 0,1,2,1);
        
        Label fnameLabel2 = new Label("Enter Hours:");
        GridPane.setHalignment(fnameLabel2, HPos.LEFT);
        gridPane.add(fnameLabel2, 3,1,2,1);
        
        ArrayList<String> project_names = new ArrayList<String>();
        ArrayList<Integer> project_numbers = new ArrayList<Integer>();
        ArrayList<Integer> current_pno = new ArrayList<Integer>();
        ArrayList<Double> current_phours = new ArrayList<Double>();
        String sql1 = "SELECT pname AS NAME, pnumber AS NUMB FROM PROJECT";
        String sql2 = "SELECT pno AS PNUMB, hours as EMPHOURS FROM Company.WORKS_ON WHERE essn = " + emp_ssn;
        
        try
        {
            Connection conn = LocalinitialSetup(1);
            Statement st = conn.createStatement(); 
            ResultSet res = st.executeQuery(sql1);

            while(res.next()){
                //Retrieve by column name
                String name = res.getString("NAME");
                project_names.add(name);
                int number = res.getInt("NUMB");
                project_numbers.add(number);
            }

            res.close();
            st.close();
            conn.close(); 
        }
        catch(Exception ex) 
        {
            System.err.println(ex);
        }
        
        // get current projects for employee
        try
        {
            Connection conn = LocalinitialSetup(1);
            Statement st = conn.createStatement(); 
            ResultSet res = st.executeQuery(sql2);

            while(res.next()){
                //Retrieve by column name
                Integer num = res.getInt("PNUMB");
                current_pno.add(num);
                Double hours = res.getDouble("EMPHOURS");
                current_phours.add(hours); 
            }

            res.close();
            st.close();
            conn.close(); 
        }
        catch(Exception ex) 
        {
            System.err.println(ex);
        }

        final CheckBox[] cbs = new CheckBox[project_names.size()];

        TextField[] project_hours = new TextField[project_names.size()];
        int n = 3;
        for (int i = 0; i < project_names.size(); i++)
        {
            boolean flag = false;

            for(int m = 0; m < current_pno.size(); m++)
            {
                if(current_pno.get(m) == project_numbers.get(i))
                {
                    final CheckBox cb = cbs[i] = new CheckBox(project_names.get(i));
                    cb.setSelected(true);
                    GridPane.setHalignment(cb, HPos.LEFT);
                    gridPane.add(cb, 0, n, 2, 1);
            
                    String hours = String.valueOf(current_phours.get(m));
                    final TextField b = project_hours[i] = new TextField(hours);
                    b.setPrefHeight(40);
                    GridPane.setHalignment(b, HPos.LEFT);
                    gridPane.add(b, 3, n,2,1 );
                    n = n + 1;
                    flag = true;
                    break;
                }
            }
            if(flag == false)
            {
                final CheckBox cb = cbs[i] = new CheckBox(project_names.get(i));
                GridPane.setHalignment(cb, HPos.LEFT);
                gridPane.add(cb, 0, n, 2, 1);

                final TextField b = project_hours[i] = new TextField();
                b.setPrefHeight(40);
                GridPane.setHalignment(b, HPos.LEFT);
                gridPane.add(b, 3, n,2,1 );
                n = n + 1;
            }
        }

        int n1 = n + 1; // submit
        int n2 = n + 2; 
        int n3 = n + 3; 
        
        Button homeButton = new Button("Go Back Home");
        homeButton.setPrefHeight(20);
        homeButton.setDefaultButton(true);
        gridPane.add(homeButton, 0, n2, 4, 1);
        GridPane.setHalignment(homeButton, HPos.LEFT);
        GridPane.setMargin(homeButton, new Insets(2,0,2,0));

        homeButton.setOnAction((ActionEvent event) -> {
            GridPane gridPane_1 = createRegistrationFormPane();
            mainPage(gridPane_1,primaryStage, ssn);
            ScrollPane sp = new ScrollPane(gridPane_1);
            Scene scene = new Scene(sp, 800, 500);
            primaryStage.setScene(scene);
            primaryStage.show();
        });
        
        Button viewEmpButton = new Button("View All Employees");
        viewEmpButton.setPrefHeight(20);
        viewEmpButton.setDefaultButton(true);
        gridPane.add(viewEmpButton, 0, n3, 2, 1);
        GridPane.setHalignment(viewEmpButton, HPos.LEFT);
        GridPane.setMargin(viewEmpButton, new Insets(2,0,2,0));

        viewEmpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	GridPane gridPane_1 = createRegistrationFormPane();
                viewEmployeeTable(gridPane_1,primaryStage, ssn);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        
        Button viewEmpButton2 = new Button("Edit Dependents");
        viewEmpButton2.setPrefHeight(20);
        viewEmpButton2.setDefaultButton(true);
        gridPane.add(viewEmpButton2, 4, n3, 1, 1);
        GridPane.setHalignment(viewEmpButton2, HPos.LEFT);
        GridPane.setMargin(viewEmpButton2, new Insets(2,0,2,0));

        viewEmpButton2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	GridPane gridPane_1 = createRegistrationFormPane();
                editEmployeeDependents(gridPane_1,primaryStage, ssn, emp_ssn);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        
        Button viewEmpButton3 = new Button("Edit Employee");
        viewEmpButton3.setPrefHeight(20);
        viewEmpButton3.setDefaultButton(true);
        gridPane.add(viewEmpButton3, 3, n3, 1, 1);
        GridPane.setHalignment(viewEmpButton3, HPos.LEFT);
        GridPane.setMargin(viewEmpButton3, new Insets(2,0,2,0));

        viewEmpButton3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	GridPane gridPane_1 = createRegistrationFormPane();
                editEmployee(gridPane_1,primaryStage, ssn, emp_ssn);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        
        Button viewEmpButton4 = new Button("Create Report");
        viewEmpButton4.setPrefHeight(20);
        viewEmpButton4.setDefaultButton(true);
        gridPane.add(viewEmpButton4, 3, n2, 1, 1);
        GridPane.setHalignment(viewEmpButton4, HPos.LEFT);
        GridPane.setMargin(viewEmpButton4, new Insets(2,0,2,0));

        viewEmpButton4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane gridPane_1 = createRegistrationFormPane();
                finalReport(gridPane_1, primaryStage, ssn, emp_ssn);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                // Set the scene in primary stage
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });

        Button submitButton = new Button("Update");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, n1, 2, 1);
        GridPane.setHalignment(submitButton, HPos.LEFT);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));
        n = n3 + 1;
        Label manLabel = new Label("Manager Logged In:\n" + ssn + "\n\nEmployee being edited:\n" + emp_ssn);
        GridPane.setHalignment(manLabel, HPos.LEFT);
        gridPane.add(manLabel, 0,n,2,1);
        
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	int total = 0;
            	
            	for (int i = 0; i < project_names.size(); i++)
                {
                    if(cbs[i].isSelected())
                    {
                        int a = 0;
                        if(project_hours[i].getText() == "")
                        {
                            a = 0;
                        }
                        else
                        {
                            try
                            {
                                a = Integer.valueOf(project_hours[i].getText());
                            }
                            catch(Exception e0)
                            {
                                String s = project_hours[i].getText();
                                double d = ParseDouble(s);
                                a = (int) d; 
                            }
                        }
                        total = total + a;
                    }
                }
            	
            	if(total > 40) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Too many hours!", "Please reduce the amount of hours");
                    return;
                }
            	
            	boolean check = false;
            	
            	for (int i = 0; i < project_names.size(); i++)
                {
                    if(cbs[i].isSelected())
                    {
                        check = true;
                        break;
                    }
                }
            	
            	if(check == false) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Warning");
                    alert.setHeaderText("No projects selected.");
                    alert.setResizable(false);
                    alert.setContentText("Select okay to continue or cancel to go back.");

                    Optional<ButtonType> result = alert.showAndWait();
                    ButtonType button = result.orElse(ButtonType.CANCEL);

                    if (button == ButtonType.OK) {
                        System.out.println("Ok pressed on assigned project");
                    } else {
                        System.out.println("Canceled pressed on assigned project");
                        return;
                    }
                }
            	
            	// Add to works_on table code
            	try
                {
                    Connection conn = LocalinitialSetup(1);
                    Statement st = null;
                    st = conn.createStatement();

                    // delete all employee records then insert
                    String delsql = "DELETE FROM WORKS_ON WHERE essn = " + emp_ssn;
                    st.executeUpdate(delsql);
                    st.close();
                    conn.close();
                    
                    for (int k = 0; k < project_names.size(); k++)
                    {
                        int p = 0;
                        try
                        {
                            p = Integer.valueOf(project_hours[k].getText());
                        }
                        catch(Exception e3)
                        {
                            String s = project_hours[k].getText();
                            double d = ParseDouble(s);
                            int a = (int) d;
                            p = a;
                        }
                        
                        if((cbs[k].isSelected()) && (p > 0) && (project_hours[k].getText() != ""))
                        {
                            Connection conn1 = LocalinitialSetup(1);
                            Statement st1 = null;
                            PreparedStatement pt = null;
                            String sql = "";
                            st1 = conn1.createStatement();

                            sql = "INSERT INTO WORKS_ON VALUES (?, ?, ?)";
                            pt = conn1.prepareStatement(sql);

                            String string_ssn = String.valueOf(emp_ssn);

                            if(string_ssn != "")
                            {
                                pt.setString(1, string_ssn);
                            }
                            else
                            {
                                pt.setNull(1, Types.VARCHAR);
                            }

                            pt.setInt(2, project_numbers.get(k));

                            if(project_hours[k].getText() != "")
                            {
                                try
                                {
                                    pt.setInt(3, Integer.valueOf(project_hours[k].getText()));
                                }
                                catch(Exception e)
                                {
                                    String s = project_hours[k].getText();
                                    double d = ParseDouble(s);
                                    int a = (int) d;
                                    pt.setInt(3, a);
                                }
                            }
                            else
                            {
                                pt.setNull(3, Types.INTEGER);
                            }
                            pt.executeUpdate();
                            pt.close();
                            st1.close();
                            conn1.close();
                        }
                    }
                }
            	catch(Exception ex) 
                {
                    showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Employee NOT uot updated with any projects!", "Please try again.\n" + ex);
                    System.err.println(ex);
                    return;
                }

            	GridPane gridPane_1 = createRegistrationFormPane();

            	editEmployeeDependents(gridPane_1, primaryStage, ssn_1, emp_ssn);
            	ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                // Set the scene in primary stage
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
    }
    
    private static int nd, nd_max, nd_add = 0;
    private static boolean add_flag = false;
    
    public static void editEmployeeDependents(GridPane gridPane, Stage primaryStage, int ssn_1, int emp_ssn) {
        Label headerLabel = new Label("Edit Employee Dependents Form");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,4,1);
        GridPane.setHalignment(headerLabel, HPos.LEFT);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));
        //int n = 0;
        int count = 0;
        nd = 0;
        nd_max = 0;
        nd_add = 0;
        add_flag = false;
        
        try
        {
            Connection conn = LocalinitialSetup(1);
            Statement st = conn.createStatement();

            String sql = "Select count(1) as NUMBER from DEPENDENT where essn = " + emp_ssn;
            ResultSet res = st.executeQuery(sql);
            
            while(res.next()){
                count = res.getInt("NUMBER");
            }
            res.close();
            st.close();
            conn.close();
        }
        catch(Exception ex) 
        {
            //showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Error getting dependent information", "Please try again.\n" + ex);
            System.err.println(ex);
            return;
        }
        
        Label fnameLabel = new Label("Does the employee have any dependents?");
        GridPane.setHalignment(fnameLabel, HPos.LEFT);
        gridPane.add(fnameLabel, 0,1,2,1);
        // System.out.println("Count: " + count);
        
        final CheckBox yes = new CheckBox("Yes");
        final CheckBox no = new CheckBox("No");
        
        ArrayList<Integer> essnlist = new ArrayList<Integer>();
        ArrayList<String> namelist = new ArrayList<String>();
        ArrayList<String> sexlist = new ArrayList<String>();
        ArrayList<String> bdatelist = new ArrayList<String>();
        ArrayList<String> rellist = new ArrayList<String>();
        
        ArrayList<TextField> namefieldlist = new ArrayList<TextField>();
        ArrayList<TextField> sexfieldlist = new ArrayList<TextField>();
        ArrayList<DatePicker> bdatefieldlist = new ArrayList<DatePicker>();
        ArrayList<TextField> relfieldlist = new ArrayList<TextField>();
        
        ArrayList<RadioButton> malefieldlist = new ArrayList<RadioButton>();
        ArrayList<RadioButton> femalefieldlist = new ArrayList<RadioButton>();
        ArrayList<CheckBox> deletefieldlist = new ArrayList<CheckBox>();
        
        Label manLabel = new Label("Edit dependents");
        //Label manLabel2 = new Label("Proceed to next screen");
        
        if (count > 0)
        {   
            //System.out.println("great");
            yes.setSelected(true);
            GridPane.setHalignment(yes, HPos.LEFT);
            gridPane.add(yes, 0, 2, 2, 1);

            no.setSelected(false);
            GridPane.setHalignment(no, HPos.LEFT);
            gridPane.add(no, 0, 3, 2, 1);
            
            try
            {
                Connection conn = LocalinitialSetup(1);
                Statement st = conn.createStatement();

                String sql = "SELECT essn as ESSN, dependent_name as DEP_NAME, sex as SEX, bdate as BDATE, relationship as REL FROM Company.DEPENDENT where essn = " + emp_ssn;
                ResultSet res = st.executeQuery(sql);

                while(res.next()){
                    //Retrieve by column name
                    essnlist.add(res.getInt("ESSN")); // this should be all the same..
                    namelist.add(res.getString("DEP_NAME"));
                    sexlist.add(res.getString("SEX"));
                    bdatelist.add(res.getString("BDATE"));
                    rellist.add(res.getString("REL"));
                }

                res.close();
                st.close();
                conn.close();
            }
            catch(Exception ex) 
            {
                showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Error getting dependent information", "Please try again.\n" + ex);
                System.err.println(ex);
                return;
            }

            nd = 4;

            GridPane.setHalignment(manLabel, HPos.LEFT);
            gridPane.add(manLabel, 0,4,2,1);
            
            dependentEditList(gridPane, namefieldlist, malefieldlist, femalefieldlist, 
            relfieldlist, bdatefieldlist, deletefieldlist, namelist, rellist, bdatelist, sexlist);

            nd = nd + 1;
            
            Button submitButton = new Button("Update");
            submitButton.setPrefHeight(40);
            submitButton.setDefaultButton(true);
            submitButton.setPrefWidth(100);
            gridPane.add(submitButton, 0, nd, 2, 1);
            GridPane.setHalignment(submitButton, HPos.LEFT);
            GridPane.setMargin(submitButton, new Insets(20, 0,20,0));

            submitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    updateButtonAction(gridPane, namefieldlist, malefieldlist, femalefieldlist, 
                    relfieldlist, bdatefieldlist, deletefieldlist, emp_ssn, 
                    primaryStage, ssn_1);
                }    
            });

            nd = nd + 0;
            nd_add = nd + 1;
            Button submitButton2 = new Button("Add");
            submitButton2.setPrefHeight(40);
            submitButton2.setDefaultButton(true);
            submitButton2.setPrefWidth(100);
            gridPane.add(submitButton2, 1, nd, 2, 1);
            GridPane.setHalignment(submitButton2, HPos.LEFT);
            GridPane.setMargin(submitButton2, new Insets(20, 0,20,0));
            //nd_max = nd;
            submitButton2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    addDependentSection(gridPane, namefieldlist, malefieldlist, femalefieldlist, 
                    relfieldlist, bdatefieldlist, deletefieldlist, ssn_1, emp_ssn, primaryStage);
                }
            });
            
            Button nextButton = new Button("Next");
            nextButton.setPrefHeight(40);
            nextButton.setDefaultButton(true);
            nextButton.setPrefWidth(100);
            gridPane.add(nextButton, 2, nd, 2, 1);
            GridPane.setHalignment(nextButton, HPos.LEFT);
            GridPane.setMargin(nextButton, new Insets(20, 0,20,0));

            nextButton.setOnAction(new EventHandler<ActionEvent>() { 
                @Override
                public void handle(ActionEvent event) {
                    nextButtonAction(emp_ssn, ssn_1, primaryStage, 0); // 1 will delete all dependents
                }
                });
            
            bottomButtons(gridPane, primaryStage, ssn_1, emp_ssn);
        }
        else
        {
            yes.setSelected(false);
            GridPane.setHalignment(yes, HPos.LEFT);
            gridPane.add(yes, 0, 2, 2, 1);

            no.setSelected(true);
            GridPane.setHalignment(no, HPos.LEFT);
            gridPane.add(no, 0, 3, 2, 1);
            
            nd = 4;
            
            Button nextButton = new Button("Next");
            nextButton.setPrefHeight(40);
            nextButton.setDefaultButton(true);
            nextButton.setPrefWidth(100);
            gridPane.add(nextButton, 0, nd, 2, 1);
            GridPane.setHalignment(nextButton, HPos.LEFT);
            GridPane.setMargin(nextButton, new Insets(20, 0,20,0));

            nextButton.setOnAction(new EventHandler<ActionEvent>() { 
                @Override
                public void handle(ActionEvent event) {
                    nextButtonAction(emp_ssn, ssn_1, primaryStage, 0); // 1 will delete all dependents
                }
            });
            
            bottomButtons(gridPane, primaryStage, ssn_1, emp_ssn);
        }
	    
        EventHandler eh = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() instanceof CheckBox) {
                    CheckBox chk = (CheckBox) event.getSource();

                    Label manLabel = new Label("Edit dependents");
                    Label manLabel2 = new Label("Proceed to next screen");
                    
                    essnlist.clear(); // this should be all the same..
                    namelist.clear();
                    sexlist.clear();
                    bdatelist.clear();
                    rellist.clear();
                    
                    namefieldlist.clear();
                    sexfieldlist.clear();
                    bdatefieldlist.clear();
                    relfieldlist.clear();
                    malefieldlist.clear();
                    femalefieldlist.clear();
                    deletefieldlist.clear();

                    try
                    {
                        Connection conn = LocalinitialSetup(1);
                        Statement st = conn.createStatement();

                        String sql = "SELECT essn as ESSN, dependent_name as DEP_NAME, sex as SEX, bdate as BDATE, relationship as REL FROM Company.DEPENDENT where essn = " + emp_ssn;
                        ResultSet res = st.executeQuery(sql);

                        while(res.next()){
                            //Retrieve by column name
                            essnlist.add(res.getInt("ESSN")); // this should be all the same..
                            namelist.add(res.getString("DEP_NAME"));
                            sexlist.add(res.getString("SEX"));
                            bdatelist.add(res.getString("BDATE"));
                            rellist.add(res.getString("REL"));
                        }

                        res.close();
                        st.close();
                        conn.close();
                    }
                    catch(Exception ex) 
                    {
                        showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Error getting dependent information", "Please try again.\n" + ex);
                        System.err.println(ex);
                        return;
                    }

                    nd = 4;

                    if ("Yes".equals(chk.getText())){
                        no.setSelected(!chk.isSelected());
                        //System.out.println("ND_MAX: " + nd_max);
                        for(int u = 4; u < nd_max; u++)
                        {
                            int y = u;
                            gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == y);
                        }

                        GridPane.setHalignment(manLabel, HPos.LEFT);
                        gridPane.add(manLabel, 0, nd,2,1);
                        
                        dependentEditList(gridPane, namefieldlist, malefieldlist, femalefieldlist, 
                        relfieldlist, bdatefieldlist, deletefieldlist, namelist, rellist, bdatelist, sexlist);
                        
                        nd = nd + 1;

                        Button submitButton = new Button("Update");
                        submitButton.setPrefHeight(40);
                        submitButton.setDefaultButton(true);
                        submitButton.setPrefWidth(100);
                        gridPane.add(submitButton, 0, nd, 2, 1);
                        GridPane.setHalignment(submitButton, HPos.LEFT);
                        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));

                        submitButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                updateButtonAction(gridPane, namefieldlist, malefieldlist, femalefieldlist, 
                                    relfieldlist, bdatefieldlist, deletefieldlist, emp_ssn, 
                                    primaryStage, ssn_1);
                            }   
                        });

                        nd = nd + 0;
                        nd_add = nd + 1;
                        Button submitButton2 = new Button("Add");
                        submitButton2.setPrefHeight(40);
                        submitButton2.setDefaultButton(true);
                        submitButton2.setPrefWidth(100);
                        gridPane.add(submitButton2, 1, nd, 2, 1);
                        GridPane.setHalignment(submitButton2, HPos.LEFT);
                        GridPane.setMargin(submitButton2, new Insets(20, 0,20,0));
                        //nd_max = nd;
                        submitButton2.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                addDependentSection(gridPane, namefieldlist, malefieldlist, femalefieldlist, 
                                relfieldlist, bdatefieldlist, deletefieldlist, ssn_1, emp_ssn, primaryStage);
                            }
                        });
                        
                        Button nextButton = new Button("Next");
                        nextButton.setPrefHeight(40);
                        nextButton.setDefaultButton(true);
                        nextButton.setPrefWidth(100);
                        gridPane.add(nextButton, 2, nd, 2, 1);
                        GridPane.setHalignment(nextButton, HPos.LEFT);
                        GridPane.setMargin(nextButton, new Insets(20, 0,20,0));

                        nextButton.setOnAction(new EventHandler<ActionEvent>() { 
                            @Override
                            public void handle(ActionEvent event) {
                                nextButtonAction(emp_ssn, ssn_1, primaryStage, 0); // 1 will delete all dependents
                            }
                        });
                        
                        //GridPane gridPane_1 = createRegistrationFormPane();
                        bottomButtons(gridPane, primaryStage, ssn_1, emp_ssn);
                    } else if ("No".equals(chk.getText())) {
                        yes.setSelected(!chk.isSelected());
                        add_flag = false;
                        for(int u = 4; u < nd_max; u++)
                        {
                            int y = u;
                            gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == y);
                        }

                        GridPane.setHalignment(manLabel2, HPos.LEFT);
                        gridPane.add(manLabel2, 0,4,2,1);

                        //nd_max = 5;
                        nd = 5;
                        Button submitButton = new Button("Next");
                        submitButton.setPrefHeight(40);
                        submitButton.setDefaultButton(true);
                        submitButton.setPrefWidth(100);
                        gridPane.add(submitButton, 0, nd, 2, 1);
                        GridPane.setHalignment(submitButton, HPos.LEFT);
                        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));

                        submitButton.setOnAction(new EventHandler<ActionEvent>() { 
                            @Override
                            public void handle(ActionEvent event) {
                                nextButtonAction(emp_ssn, ssn_1, primaryStage, 1); // 1 will delete all dependents
                            }
                            });
                        bottomButtons(gridPane, primaryStage, ssn_1, emp_ssn);
                    }
                }
            }
        };
	    
        yes.setOnAction(eh);
        no.setOnAction(eh);
    }
    
    private static void nextButtonAction(int emp_ssn, int ssn_1, Stage primaryStage, int option)
    {
        // Delete all dependents first if 'No' was selected
        if(option == 1)
        {
            try
            {
                Connection conn = LocalinitialSetup(1);
                Statement st = null;
                st = conn.createStatement();

                // delete all employee records then insert
                String delsql = "DELETE FROM DEPENDENT WHERE essn = " + emp_ssn;
                st.executeUpdate(delsql);
                st.close();
                conn.close();
            } catch (Exception ex) 
            {
                //showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Employee NOT uot updated with any projects!", "Please try again.\n" + ex);
                System.err.println(ex);
                return;
            }
        }

        GridPane gridPane_1 = createRegistrationFormPane();
        finalReport(gridPane_1, primaryStage, ssn_1, emp_ssn);
        ScrollPane sp = new ScrollPane(gridPane_1);
        Scene scene = new Scene(sp, 800, 500);
        // Set the scene in primary stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private static void updateButtonAction(GridPane gridPane, ArrayList<TextField> namefieldlist, 
            ArrayList<RadioButton> malefieldlist, ArrayList<RadioButton> femalefieldlist, 
            ArrayList<TextField> relfieldlist, ArrayList<DatePicker> bdatefieldlist,
            ArrayList<CheckBox> deletefieldlist, int emp_ssn, Stage primaryStage, int ssn_1)
    {
        if(namefieldlist.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter a dependent name");
        return;
        }
        try
        {
            Connection conn = LocalinitialSetup(1);
            Statement st = null;
            st = conn.createStatement();

            // delete all employee records then insert
            String delsql = "DELETE FROM DEPENDENT WHERE essn = " + emp_ssn;
            st.executeUpdate(delsql);
            st.close();
            conn.close();
        } catch (Exception ex) 
        {
            showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Dependents NOT uot updated!", "Please try again.\n" + ex);
            System.err.println(ex);
            return;
        }

        for(int p = 0; p < namefieldlist.size(); p++)
        {
            String essn = String.valueOf(emp_ssn);
            String depname = namefieldlist.get(p).getText();
            if((depname == "") && (add_flag == true))
            {
                break;
            }

            String sex = "";

            if(malefieldlist.get(p).isSelected())
            {
                sex = "M";
            }
            else if(femalefieldlist.get(p).isSelected())
            {
                sex = "F";
            }
            else
            {
                sex = "M";
            }
            String bday = String.valueOf(bdatefieldlist.get(p).getValue());
            String relation = relfieldlist.get(p).getText();

            if(!deletefieldlist.get(p).isSelected())
            {
                try
                {
                    Connection conn = LocalinitialSetup(1);
                    Statement st = conn.createStatement();

                    String sql = "INSERT INTO DEPENDENT VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pt = conn.prepareStatement(sql);

                    if(essn != "")
                    {
                        pt.setString(1, essn);
                    }
                    else
                    {
                        pt.setNull(1, Types.VARCHAR);
                    }

                    if(depname != "")
                    {
                        pt.setString(2, depname);
                    }
                    else
                    {
                        pt.setNull(2, Types.VARCHAR);
                    }

                    if(sex != "")
                    {
                        pt.setString(3, sex);
                    }
                    else
                    {
                        pt.setNull(3, Types.VARCHAR);
                    }

                    if(bday != "")
                    {
                        pt.setString(4, bday);
                    }
                    else
                    {
                        pt.setNull(4, Types.VARCHAR);
                    }

                    if(relation != "")
                    {
                        pt.setString(5, relation);
                    }
                    else
                    {
                        pt.setNull(5, Types.VARCHAR);
                    }

                    pt.executeUpdate();

                    pt.close();
                    st.close();

                    conn.close();

                    //nameField.clear();
                    //sexField.clear();
                    //bField.clear();
                    //rField.clear();
                    add_flag = false;
                    showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Dependent Updated/Added Successfully!", "Employee's SSN: " + essn + "\nName: " + depname + "\nSex: " + sex + "\nRelation: " + relation);
                }
                catch(Exception ex) 
                {
                    showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Dependent NOT Updated/Added!", "Please try again.\n" + ex);
                    System.err.println(ex);
                    return;
                }
            }
            else
            {
                //showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Dependent Deleted Successfully!", "Employee's SSN: " + essn + "\nName: " + depname + "\nSex: " + sex + "\nRelation: " + relation);
            }   
        }
        
        GridPane gridPane_1 = createRegistrationFormPane();
        editEmployeeDependents(gridPane_1, primaryStage, ssn_1, emp_ssn);
        ScrollPane sp = new ScrollPane(gridPane_1);
        Scene scene = new Scene(sp, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private static void addDependentSection(GridPane gridPane, ArrayList<TextField> namefieldlist, 
            ArrayList<RadioButton> malefieldlist, ArrayList<RadioButton> femalefieldlist, 
            ArrayList<TextField> relfieldlist, ArrayList<DatePicker> bdatefieldlist,
            ArrayList<CheckBox> deletefieldlist, int ssn_1, int emp_ssn, Stage primaryStage)
    {
        int diff = 0;

        if(add_flag == false)
        {
            for(int u = nd_add; u < nd_max; u++)
            {
                int y = u;
                diff++;
                gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == y);
            }

            nd = nd_max - diff;
            Label nameLabel = new Label("First name : ");
            gridPane.add(nameLabel, 0,nd);

            TextField nameField = new TextField();
            namefieldlist.add(nameField);
            namefieldlist.get(namefieldlist.size() - 1).setPrefHeight(40);
            gridPane.add(namefieldlist.get(namefieldlist.size() - 1), 1,nd);

            final CheckBox delete = new CheckBox("Delete");
            deletefieldlist.add(delete); // not really used!!

            nd = nd + 1;
            Label sexLabel = new Label("Sex : ");
            gridPane.add(sexLabel, 0,nd);

            final ToggleGroup sexgroup = new ToggleGroup();
            RadioButton male = new RadioButton("Male");
            malefieldlist.add(male);
            male.setToggleGroup(sexgroup);

            RadioButton female = new RadioButton("Female");
            femalefieldlist.add(female);
            female.setToggleGroup(sexgroup);

            nd = nd + 0;
            malefieldlist.get(malefieldlist.size() - 1).setSelected(true);
            GridPane.setHalignment(malefieldlist.get(malefieldlist.size() - 1), HPos.LEFT);
            gridPane.add(malefieldlist.get(malefieldlist.size() - 1), 1, nd);

            nd = nd + 1;
            femalefieldlist.get(femalefieldlist.size() - 1).setSelected(false);
            GridPane.setHalignment(femalefieldlist.get(femalefieldlist.size() - 1), HPos.LEFT);
            gridPane.add(femalefieldlist.get(femalefieldlist.size() - 1), 1, nd);

            nd = nd + 1;
            Label bLabel = new Label("Birthday :\n(YYYY-MM-DD) ");
            gridPane.add(bLabel, 0,nd);

            DatePicker datePicker = new DatePicker();
            datePicker.setOnAction(event -> {
                LocalDate date = datePicker.getValue();
                //System.out.println("Selected date: " + date);
            });
            String pattern = "yyyy-MM-dd";

            datePicker.setPromptText(pattern.toLowerCase());

            datePicker.setConverter(new StringConverter<LocalDate>() {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
                @Override
                public String toString(LocalDate date) {
                    if (date != null) {
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }
                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            });

            //datePicker.setPrefHeight(40);
            //gridPane.add(datePicker, 1, 5);

            //TextField bField = new TextField();
            bdatefieldlist.add(datePicker);
            bdatefieldlist.get(bdatefieldlist.size() - 1).setPrefHeight(40);
            gridPane.add(bdatefieldlist.get(bdatefieldlist.size() - 1), 1,nd);

            nd = nd + 1;
            Label rLabel = new Label("Relation : ");
            gridPane.add(rLabel, 0,nd);

            TextField rField = new TextField();
            relfieldlist.add(rField);
            relfieldlist.get(relfieldlist.size() - 1).setPrefHeight(40);
            gridPane.add(relfieldlist.get(relfieldlist.size() - 1), 1,nd);

            add_flag = true;
        }
        else
        {
            for(int u = nd_add; u < nd_max; u++)
            {
                int y = u;
                diff++;
                gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == y);
            }
            malefieldlist.remove(malefieldlist.size() - 1);
            femalefieldlist.remove(femalefieldlist.size() - 1);
            bdatefieldlist.remove(bdatefieldlist.size() - 1);
            relfieldlist.remove(relfieldlist.size() - 1);
            deletefieldlist.remove(deletefieldlist.size() - 1);
            namefieldlist.remove(namefieldlist.size() - 1);

            nd = nd_add;
            add_flag = false;
        }

        bottomButtons(gridPane, primaryStage, ssn_1, emp_ssn);
    }
    
    private static void dependentEditList(GridPane gridPane, ArrayList<TextField> namefieldlist, 
            ArrayList<RadioButton> malefieldlist, ArrayList<RadioButton> femalefieldlist, 
            ArrayList<TextField> relfieldlist, ArrayList<DatePicker> bdatefieldlist,
            ArrayList<CheckBox> deletefieldlist, ArrayList<String> namelist, 
            ArrayList<String> rellist, ArrayList<String> bdatelist, ArrayList<String> sexlist)
    {
        for(int k = 0; k < namelist.size(); k++)
        {
            nd = nd + 1;
            Label nameLabel = new Label("First name : ");
            gridPane.add(nameLabel, 0,nd);

            TextField nameField = new TextField(namelist.get(k));
            namefieldlist.add(nameField);
            nameField.setPrefHeight(40);
            gridPane.add(nameField, 1,nd);

            final CheckBox delete = new CheckBox("Delete");
            deletefieldlist.add(delete);
            GridPane.setHalignment(deletefieldlist.get(k), HPos.LEFT);
            gridPane.add(delete, 2, nd);

            nd = nd + 1;
            Label sexLabel = new Label("Sex : ");
            gridPane.add(sexLabel, 0,nd);

            final ToggleGroup sexgroup = new ToggleGroup();
            RadioButton male = new RadioButton("Male");
            malefieldlist.add(male);
            male.setToggleGroup(sexgroup);

            RadioButton female = new RadioButton("Female");
            femalefieldlist.add(female);
            female.setToggleGroup(sexgroup);

            if("F".equals(sexlist.get(k)))
            {
                femalefieldlist.get(k).setSelected(true);
                nd = nd + 0;
                GridPane.setHalignment(malefieldlist.get(k), HPos.LEFT);
                gridPane.add(male, 1, nd);

                nd = nd + 1;
                GridPane.setHalignment(femalefieldlist.get(k), HPos.LEFT);
                gridPane.add(female, 1, nd);
            }
            else
            {
                malefieldlist.get(k).setSelected(true);
                nd = nd + 0;
                GridPane.setHalignment(malefieldlist.get(k), HPos.LEFT);
                gridPane.add(male, 1, nd);

                nd = nd + 1;
                GridPane.setHalignment(femalefieldlist.get(k), HPos.LEFT);
                gridPane.add(female, 1, nd);
            }

            nd = nd + 1;
            Label bLabel = new Label("Birthday :\n(YYYY-MM-DD) ");
            gridPane.add(bLabel, 0,nd);

            DatePicker datePicker = new DatePicker(LocalDate.parse(bdatelist.get(k)));
            datePicker.setOnAction(event -> {
                LocalDate date = datePicker.getValue();
                System.out.println("Selected date: " + date);
            });
            String pattern = "yyyy-MM-dd";

            datePicker.setPromptText(pattern.toLowerCase());

            datePicker.setConverter(new StringConverter<LocalDate>() {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
                @Override
                public String toString(LocalDate date) {
                    if (date != null) {
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }
                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            });

            //TextField bField = new TextField(bdatelist.get(k));
            bdatefieldlist.add(datePicker); // fix this
            datePicker.setPrefHeight(40);
            gridPane.add(datePicker, 1,nd);

            nd = nd + 1;
            Label rLabel = new Label("Relation : ");
            gridPane.add(rLabel, 0,nd);

            TextField rField = new TextField(rellist.get(k));
            relfieldlist.add(rField);
            rField.setPrefHeight(40);
            gridPane.add(rField, 1,nd);
        }        
    }
     
    public static void bottomButtons(GridPane gridPane, Stage primaryStage, int ssn_1, int emp_ssn)
    {
        nd = nd + 1;
        Label manLabel3 = new Label("Manager Logged In:\n" + ssn_1 + "\n\nEmployee being edited:\n" + String.valueOf(emp_ssn));
        GridPane.setHalignment(manLabel3, HPos.LEFT);
        gridPane.add(manLabel3, 0,nd,2,1);

        //--------
        nd = nd + 1;
        Button homeButton = new Button("Go Back Home");
        homeButton.setPrefHeight(20);
        homeButton.setDefaultButton(true);
        gridPane.add(homeButton, 0, nd, 1, 1);
        GridPane.setHalignment(homeButton, HPos.LEFT);
        GridPane.setMargin(homeButton, new Insets(2,0,2,0));

        homeButton.setOnAction((ActionEvent event) -> {
        GridPane gridPane_1 = createRegistrationFormPane();
        mainPage(gridPane_1,primaryStage, ssn_1);
        ScrollPane sp = new ScrollPane(gridPane_1);
        Scene scene = new Scene(sp, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
        });
        
        Button viewEmpButton4 = new Button("Create Report");
        viewEmpButton4.setPrefHeight(20);
        viewEmpButton4.setDefaultButton(true);
        gridPane.add(viewEmpButton4, 1, nd, 1, 1);
        GridPane.setHalignment(viewEmpButton4, HPos.LEFT);
        GridPane.setMargin(viewEmpButton4, new Insets(2,0,2,0));

        viewEmpButton4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane gridPane_1 = createRegistrationFormPane();
                finalReport(gridPane_1, primaryStage, ssn_1, emp_ssn);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                // Set the scene in primary stage
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        
        nd = nd + 1;
        Button viewEmpButton = new Button("View All Employees");
        viewEmpButton.setPrefHeight(20);
        viewEmpButton.setDefaultButton(true);
        gridPane.add(viewEmpButton, 0, nd, 1, 1);
        GridPane.setHalignment(viewEmpButton, HPos.LEFT);
        GridPane.setMargin(viewEmpButton, new Insets(2,0,2,0));
        nd_max = nd + 1;
        viewEmpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	GridPane gridPane_1 = createRegistrationFormPane();
                viewEmployeeTable(gridPane_1,primaryStage, ssn_1);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });

        Button viewEmpButton2 = new Button("Edit Employee");
        viewEmpButton2.setPrefHeight(20);
        viewEmpButton2.setDefaultButton(true);
        gridPane.add(viewEmpButton2, 2, nd, 1, 1);
        GridPane.setHalignment(viewEmpButton2, HPos.LEFT);
        GridPane.setMargin(viewEmpButton2, new Insets(2,0,2,0));

        viewEmpButton2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	GridPane gridPane_1 = createRegistrationFormPane();
                editEmployee(gridPane_1,primaryStage, ssn_1, emp_ssn);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        
        Button viewEmpButton3 = new Button("Edit Projects");
        viewEmpButton3.setPrefHeight(20);
        viewEmpButton3.setDefaultButton(true);
        gridPane.add(viewEmpButton3, 1, nd, 1, 1);
        GridPane.setHalignment(viewEmpButton3, HPos.LEFT);
        GridPane.setMargin(viewEmpButton3, new Insets(2,0,2,0));

        viewEmpButton3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	GridPane gridPane_1 = createRegistrationFormPane();
                editUserProject(gridPane_1,primaryStage, ssn_1, emp_ssn);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });    
    }
}
