package com.example.companyemployeemanagementsystem;

/*
* Created by David Jones
*/
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.text.FontWeight;
import javafx.stage.Window;
import javafx.print.*;
import javafx.print.PageLayout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

import static com.example.companyemployeemanagementsystem.newTable.createTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

public class MainAccess extends Application {
    //static Connection conn;
    static int empssn = 0;
    static int mangssn = 0;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Employee Mangement System JavaFX Application");
        // Create the registration form grid pane
        GridPane gridPane = createRegistrationFormPane();
        // Add UI controls to the registration form grid pane
        managerLogin(gridPane, primaryStage);
        // Create a scene with registration form grid pane as the root node
        ScrollPane sp = new ScrollPane(gridPane);
        Scene scene = new Scene(sp, 800, 500);
        // Set the scene in primary stage
        
        primaryStage.setScene(scene);
        primaryStage.show();
    } 
    
    //--------------------NEW 2022---------
    // init database constants
    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://*/Company";
    private static final String DATABASE_URL_2 = "jdbc:mysql://*:3306";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "*";
    private static final String MAX_POOL = "250";

    // init connection object
    private static Connection connection;
    // init properties object
    private static Properties properties;
    
    public static void initialSetup()
    {
        Connection conn = null;
        try {
            Class.forName(DATABASE_DRIVER);
            conn = connect();
            //System.out.println("Connected");
        }catch (ClassNotFoundException e){
            System.err.println(e);
        }
    }
    
    public static Connection LocalinitialSetup(int option)
    {
        Connection conn = null;
        try {
            Class.forName(DATABASE_DRIVER);
            conn = Localconnect(option);
            return conn;
            //System.out.println("Connected");
        }catch (ClassNotFoundException e){
            System.err.println(e);
        }
        return null;
    }

    // create properties
    private static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
            properties.setProperty("MaxPooledStatements", MAX_POOL);
        }
        return properties;
    }
    
    // Locally connect database
    public static Connection Localconnect(int option) {
        Connection connection = null;
            try {
                Class.forName(DATABASE_DRIVER);
                if(option != 0)
                {
                    connection = DriverManager.getConnection(DATABASE_URL, getProperties());
                }
                else
                {
                    connection = DriverManager.getConnection(DATABASE_URL_2, getProperties());
                }
                //System.out.println("CONNECT");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        return connection;
    }

    // connect database
    public static Connection connect() {
        if (connection == null) {
            try {
                Class.forName(DATABASE_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL, getProperties());
                //System.out.println("CONNECT");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //-------------------------------------

    // Create gridPane
    public static GridPane createRegistrationFormPane() {
        GridPane gridPane = new GridPane();

        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        gridPane.setHgap(10);
        gridPane.setVgap(15);

        // Add Column Constraints

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints);

        return gridPane;
    }
    
    public static void insertData(String tablename, String filename) {
        Connection conn8 = LocalinitialSetup(1);
                    
        try
        {
            Statement st8 = conn8.createStatement();

            StringBuffer sql = new StringBuffer();
            try {
              File myObj = new File("C:\\Users\\David.Jones\\Documents\\NetBeansProjects\\djones_cs450_project_1\\src\\main\\java\\com\\mycompany\\djones_cs450_project_1\\" + filename + "");
              Scanner myReader = new Scanner(myObj);
              while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                st8.executeUpdate(data);
              }
              myReader.close();
            } catch (FileNotFoundException e) {
              System.out.println("An error occurred.");
              e.printStackTrace();
            }

            st8.close();
            conn8.close();
            //System.out.println("Data inserted successfully created in to " + tablename + " Table.");
        }
        catch(Exception ex) 
        {
            System.err.println(ex);
        }
    }
    
    // Log's in a manger by verifying
    private static void managerLogin(GridPane gridPane, Stage primaryStage) {
        Label headerLabel = new Label("Manager Login");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.LEFT);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        Label fnameLabel = new Label("Please enter your Social Security #:\n(You can use 222222200 for testing) ");
        GridPane.setHalignment(fnameLabel, HPos.LEFT);
        gridPane.add(fnameLabel, 0,1,3,1);
        
        TextField ssnField = new TextField("222222200");
        ssnField.setPrefHeight(40);
        // Restricts input to numbers
        ssnField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    ssnField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        gridPane.add(ssnField, 0,2,3,1);

        final CheckBox newTableCheckBox = new CheckBox("Check to reload COMPANY table in database");
        GridPane.setHalignment(newTableCheckBox, HPos.LEFT);
        gridPane.add(newTableCheckBox, 0, 3, 2, 1);
        
        Button submitButton = new Button("Submit");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 4, 2, 1);
        GridPane.setHalignment(submitButton, HPos.LEFT);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));

        /*Label noteLabel = new Label("Note: The COMPANY table refreshes each time you login");
        noteLabel.setFont(Font.font("Arial", FontWeight.LIGHT, 10));
        GridPane.setHalignment(noteLabel, HPos.LEFT);
        gridPane.add(noteLabel, 0,6,3,1);*/

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	int newtableoption = 1;

            	if(ssnField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your Social Security #");
                    return;
                }

                if(newTableCheckBox.isSelected())
                {
                    newtableoption = 0;
                }
                else
                {
                    newtableoption = 1;
                }
            	
            	//initialSetup();
            	boolean check = validateManager(Integer.valueOf(ssnField.getText()), newtableoption); // 0 creates new database; 1 uses existing
            	
            	//primaryStage.setTitle("New Employee Form JavaFX Application");
            	GridPane gridPane_1 = createRegistrationFormPane();
            	
            	if(check == true)
            	{
                    mainPage(gridPane_1,primaryStage,Integer.valueOf(ssnField.getText()));
            		
                    // Create a scene with registration form grid pane as the root node
                    ScrollPane sp = new ScrollPane(gridPane_1);
                    Scene scene = new Scene(sp, 800, 500);
                    // Set the scene in primary stage
                    primaryStage.setScene(scene);
                    primaryStage.show();
            	}
            	else
            	{
            		stopScreen(gridPane_1, primaryStage);
            		ScrollPane sp = new ScrollPane(gridPane_1);
                    Scene scene = new Scene(sp, 800, 500);
                    // Set the scene in primary stage
                    primaryStage.setScene(scene);
                    primaryStage.show();
            	}
            }
        });
    }
    
    // Main page user sees after logging in
    public static void mainPage(GridPane gridPane, Stage primaryStage, int ssn_1)
    {
        Label headerLabel = new Label("Company Employee HR System");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,5,1);
        GridPane.setHalignment(headerLabel, HPos.LEFT);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));
        
        Button newEmpButton = new Button("Add New Employee");
        newEmpButton.setPrefHeight(20);
        newEmpButton.setDefaultButton(true);
        gridPane.add(newEmpButton, 0, 2, 6, 1);
        GridPane.setHalignment(newEmpButton, HPos.LEFT);
        GridPane.setMargin(newEmpButton, new Insets(2,0,2,0));

        newEmpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	GridPane gridPane_1 = createRegistrationFormPane();
                newEmployee(gridPane_1,primaryStage, ssn_1);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        
        Button viewEmpButton = new Button("View All Employees");
        viewEmpButton.setPrefHeight(20);
        viewEmpButton.setDefaultButton(true);
        gridPane.add(viewEmpButton, 0, 3, 6, 1);
        GridPane.setHalignment(viewEmpButton, HPos.LEFT);
        GridPane.setMargin(viewEmpButton, new Insets(2,0,2,0));

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
        
        Button signoutButton = new Button("Sign Out");
        signoutButton.setPrefHeight(20);
        signoutButton.setDefaultButton(true);
        gridPane.add(signoutButton, 0, 4, 6, 1);
        GridPane.setHalignment(signoutButton, HPos.LEFT);
        GridPane.setMargin(signoutButton, new Insets(2,0,2,0));

        signoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Manager Signed Out: ", String.valueOf(ssn_1));
            	GridPane gridPane_1 = createRegistrationFormPane();
                
                managerLogin(gridPane_1,primaryStage);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        
        Label manLabel = new Label("Manager Logged In:\n" + ssn_1 + "");
        GridPane.setHalignment(manLabel, HPos.LEFT);
        gridPane.add(manLabel, 0,6,2,1);
    }
    
    // Program stop screen
    private static void stopScreen(GridPane gridPane, Stage primaryStage) {
        Label headerLabel = new Label("Manager Login");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.LEFT);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        Label fnameLabel = new Label("Program has stopped because the SSN you entered isn't valid");
        GridPane.setHalignment(fnameLabel, HPos.LEFT);
        gridPane.add(fnameLabel, 0,1,5,1);
        
        Button submitButton = new Button("Try Again");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 2, 2, 1);
        GridPane.setHalignment(submitButton, HPos.LEFT);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	GridPane gridPane_1 = createRegistrationFormPane();

            	managerLogin(gridPane_1, primaryStage);
            	ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                // Set the scene in primary stage
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
    }
    
    // Validate manager SSN
    private static boolean validateManager(int superssn, int option)
    {
        boolean check = false; // changed to true for testing
        //Connection conn = conn_1;
        int num = superssn;

        if(option == 0)
        {
            createTable();
        }

        Connection conn = LocalinitialSetup(1);

        String sql1 = "Select distinct superssn AS SSN\r\n" + 
                        "FROM EMPLOYEE";
        try
        {
                Statement st = conn.createStatement(); 
                ResultSet res = st.executeQuery(sql1);

            while(res.next()){
                //Retrieve by column name
                int ssn = res.getInt("SSN");
                if(ssn == num)
                {
                    check = true;
                    break;
                }
            }

            if(check == false)
            {
                //System.out.println("Your SSN is NOT VALID.");
            }
            else
            {
                mangssn = num;
                //System.out.println("Your SSN is VALID");
            }

            res.close();
            st.close();
                conn.close(); 
        }
        catch(Exception ex) 
        {
            System.err.println(ex);
        }

        return check;
    }
    
    // Assign projects based on developer role
    public static void assignProject(GridPane gridPane, Stage primaryStage, int ssn_1, int emp_ssn) {
        Label headerLabel = new Label("Assign Employee to Projects Form");
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
        String sql1 = "SELECT pname AS NAME, pnumber AS NUMB FROM PROJECT";
        
        try
        {
            Connection conn = LocalinitialSetup(1);
            Statement st = conn.createStatement(); 
            ResultSet res = st.executeQuery(sql1);

            while(res.next()){
                //Retrieve by column name
                String name = res.getString("NAME");
                project_names.add(name);
            }

            res.close();
            st.close();
            conn.close(); 
        }
        catch(Exception ex) 
        {
            System.err.println(ex);
        }

        //initialSetup();
        try
        {
            Connection conn2 = LocalinitialSetup(1);
            Statement st = conn2.createStatement(); 
            ResultSet res = st.executeQuery(sql1);

            while(res.next()){
                //Retrieve by column name
                int number = res.getInt("NUMB");
                project_numbers.add(number);
            }

            res.close();
            st.close();
            conn2.close(); 
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
            final CheckBox cb = cbs[i] = new CheckBox(project_names.get(i));
            GridPane.setHalignment(cb, HPos.LEFT);
            gridPane.add(cb, 0, n, 2, 1);
            final TextField b = project_hours[i] = new TextField();
            b.setPrefHeight(40);
            GridPane.setHalignment(b, HPos.LEFT);
            gridPane.add(b, 3, n,2,1 );
            n = n + 1;
        }

        n = n + 1;

        Button submitButton = new Button("Submit");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, n, 2, 1);
        GridPane.setHalignment(submitButton, HPos.LEFT);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));
        n = n + 1;
        Label manLabel = new Label("Manager Logged In:\n" + ssn + "\n\nEmployee being edited:\n" + empssn);
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
                            String s = project_hours[i].getText();
                            double d = ParseDouble(s);
                            a = (int) d;
                            //a = Integer.valueOf(project_hours[i].getText());
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
                            Connection conn = LocalinitialSetup(1);
                            Statement st = null;
                            PreparedStatement pt = null;
                            String sql = "";
                            st = conn.createStatement();
                            sql = "INSERT INTO WORKS_ON VALUES (?, ?, ?)";
                            pt = conn.prepareStatement(sql);

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

                            if(project_hours[k].getText() != "") // already checked ..
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
                            st.close();
                            conn.close();
                        }
                    }
                }
            	catch(Exception ex) 
                {
                    showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "New Employee NOT added to any projects!", "Please try again.\n" + ex);
                    System.err.println(ex);
                    return;
                }

            	GridPane gridPane_1 = createRegistrationFormPane();

            	employeeDependents(gridPane_1, primaryStage, ssn, empssn);
            	ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                // Set the scene in primary stage
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
    }
    
    // Enter and display employee's dependents
    private static void employeeDependents(GridPane gridPane, Stage primaryStage, int ssn_1, int emp_ssn)
    {
        Label headerLabel = new Label("Employee Dependents Form");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,5,1);
        GridPane.setHalignment(headerLabel, HPos.LEFT);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));
        
        int ssn = ssn_1; // this is the manager logged in

        Label fnameLabel = new Label("Does the employee have any dependents?");
        GridPane.setHalignment(fnameLabel, HPos.LEFT);
        gridPane.add(fnameLabel, 0,1,2,1);
        
        final CheckBox yes = new CheckBox("Yes");
        GridPane.setHalignment(yes, HPos.LEFT);
	    gridPane.add(yes, 0, 2, 2, 1);
        
        final CheckBox no = new CheckBox("No");
        GridPane.setHalignment(no, HPos.LEFT);
        gridPane.add(no, 0, 3, 2, 1);

	    
        EventHandler eh = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() instanceof CheckBox) {
                    CheckBox chk = (CheckBox) event.getSource();

                    Label manLabel = new Label("Please enter dependents");
                    Label manLabel2 = new Label("Proceed to next screen");

                    if ("Yes".equals(chk.getText())) {
                        no.setSelected(!chk.isSelected());
                        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 4);
                        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 5);
                        GridPane.setHalignment(manLabel, HPos.LEFT);
                        gridPane.add(manLabel, 0,4,2,1);

                        Label nameLabel = new Label("First name : ");
                        gridPane.add(nameLabel, 0,5);

                        TextField nameField = new TextField("");
                        nameField.setPrefHeight(40);
                        gridPane.add(nameField, 1,5);

                        Label sexLabel = new Label("Sex : ");
                        gridPane.add(sexLabel, 0,6);

                        final ToggleGroup sexgroup = new ToggleGroup();
                        RadioButton male = new RadioButton("Male");
                        male.setToggleGroup(sexgroup);

                        RadioButton female = new RadioButton("Female");
                        female.setToggleGroup(sexgroup);

                        male.setSelected(true);
                        GridPane.setHalignment(male, HPos.LEFT);
                        gridPane.add(male, 1, 6);

                        female.setSelected(false);
                        GridPane.setHalignment(female, HPos.LEFT);
                        gridPane.add(female, 1, 7);

                        Label bLabel = new Label("Birthday :\n(YYYY-MM-DD) ");
                        gridPane.add(bLabel, 0,8);

                        DatePicker datePicker = new DatePicker();
                        datePicker.setOnAction(event1 -> {
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
                        gridPane.add(datePicker, 1, 8);

                        /*TextField bField = new TextField("");
                        bField.setPrefHeight(40);
                        gridPane.add(bField, 1,8);*/

                        Label rLabel = new Label("Relation : ");
                        gridPane.add(rLabel, 0,9);

                        TextField rField = new TextField("");
                        rField.setPrefHeight(40);
                        gridPane.add(rField, 1,9);

                        Button submitButton = new Button("Submit");
                        submitButton.setPrefHeight(40);
                        submitButton.setDefaultButton(true);
                        submitButton.setPrefWidth(100);
                        gridPane.add(submitButton, 0, 10, 2, 1);
                        GridPane.setHalignment(submitButton, HPos.LEFT);
                        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));

                        submitButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                if(nameField.getText().isEmpty()) {
                                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter the dependent's first name");
                                return;
                                }

                                String essn = String.valueOf(emp_ssn);
                                String depname = nameField.getText();
                                //String sex = sexField.getText();
                                String sex = "";

                                if(male.isSelected())
                                {
                                    sex = "M";
                                }
                                else if(female.isSelected())
                                {
                                    sex = "F";
                                }
                                String bday = String.valueOf(datePicker.getValue());
                                String relation = rField.getText();

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
                                    showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "New Dependent Added Successfully!", "Employee's SSN: " + essn + "\nName: " + depname + "\nSex: " + sex + "\nRelation: " + relation);
                                }
                                catch(Exception ex) 
                                {
                                    showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "New Dependent NOT Added!", "Please try again.\n" + ex);
                                    System.err.println(ex);
                                    return;
                                }
                            }    
                        });
                        Button submitButton2 = new Button("Next");
                        submitButton2.setPrefHeight(40);
                        submitButton2.setDefaultButton(true);
                        submitButton2.setPrefWidth(100);
                        gridPane.add(submitButton2, 1, 10, 2, 1);
                        GridPane.setHalignment(submitButton2, HPos.LEFT);
                        GridPane.setMargin(submitButton2, new Insets(20, 0,20,0));

                        submitButton2.setOnAction(new EventHandler<ActionEvent>() {
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
                    } else if ("No".equals(chk.getText())) {
                        yes.setSelected(!chk.isSelected());
                        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 4);
                        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 5);
                        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 6);
                        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 7);
                        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 8);
                        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 9);
                        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 10);

                        GridPane.setHalignment(manLabel2, HPos.LEFT);
                        gridPane.add(manLabel2, 0,4,2,1);

                        Button submitButton = new Button("Next");
                        submitButton.setPrefHeight(40);
                        submitButton.setDefaultButton(true);
                        submitButton.setPrefWidth(100);
                        gridPane.add(submitButton, 0, 5, 2, 1);
                        GridPane.setHalignment(submitButton, HPos.LEFT);
                        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));

                        submitButton.setOnAction(new EventHandler<ActionEvent>() {
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
                    }
                }
            }
        };

        yes.setOnAction(eh);
        no.setOnAction(eh);

        Label manLabel = new Label("Manager Logged In:\n" + ssn_1 + "\n\nEmployee being edited:\n" + String.valueOf(emp_ssn));
        GridPane.setHalignment(manLabel, HPos.LEFT);
        gridPane.add(manLabel, 0,11,2,1);
    }
    
    // View all employees in database
    public static void viewEmployeeTable(GridPane gridPane, Stage primaryStage, int ssn_1) {
        // Add Header
        Label headerLabel = new Label("Current Employees at Company");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,4,1);
        GridPane.setHalignment(headerLabel, HPos.LEFT);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));
        Label instruction = new Label("Click on row to edit employee");
        GridPane.setHalignment(instruction, HPos.LEFT);
        gridPane.add(instruction, 0,1,4,1);
        
        TableView<UserAccount> table = new TableView<>();

        TableColumn<UserAccount, String> ssnNameCol //
                = new TableColumn<UserAccount, String>("SSN");

        TableColumn<UserAccount, String> firstNameCol//
                = new TableColumn<UserAccount, String>("First Name");

        TableColumn<UserAccount, String> midNameCol//
                = new TableColumn<UserAccount, String>("Middle Initial");

        TableColumn<UserAccount, String> lastNameCol//
                = new TableColumn<UserAccount, String>("Last Name");

        TableColumn<UserAccount, String> addNameCol //
                = new TableColumn<UserAccount, String>("Address");

        TableColumn<UserAccount, String> sexNameCol //
                = new TableColumn<UserAccount, String>("Sex");

        TableColumn<UserAccount, String> dobNameCol //
                = new TableColumn<UserAccount, String>("DOB");

        TableColumn<UserAccount, String> salNameCol //
                = new TableColumn<UserAccount, String>("Salary");

        TableColumn<UserAccount, String> mgssNameCol //
                = new TableColumn<UserAccount, String>("Manager SSN");

        TableColumn<UserAccount, String> dnoNameCol //
                = new TableColumn<UserAccount, String>("Dept No");

        ssnNameCol.setCellValueFactory(new PropertyValueFactory<>("ssn"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        midNameCol.setCellValueFactory(new PropertyValueFactory<>("midinit"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        addNameCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        sexNameCol.setCellValueFactory(new PropertyValueFactory<>("sex"));
        dobNameCol.setCellValueFactory(new PropertyValueFactory<>("dob"));
        salNameCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        mgssNameCol.setCellValueFactory(new PropertyValueFactory<>("mgssn"));
        dnoNameCol.setCellValueFactory(new PropertyValueFactory<>("dno"));

        // Set Sort type for userName column
        //firstNameCol.setSortType(TableColumn.SortType.DESCENDING);
        //lastNameCol.setSortable(false);

        // Display row data
        ObservableList<UserAccount> list = getUserList();
        table.setItems(list);

        table.getColumns().addAll(ssnNameCol, firstNameCol, midNameCol, lastNameCol, addNameCol, sexNameCol, dobNameCol, salNameCol, mgssNameCol, dnoNameCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent event) {
                   //System.out.println("Mouse event");
                   System.out.println(table.getSelectionModel().getSelectedItem().getSsn());
                   GridPane gridPane_1 = createRegistrationFormPane();
                    editUserAccount.editEmployee(gridPane_1, primaryStage, ssn_1, Integer.parseInt(table.getSelectionModel().getSelectedItem().getSsn()));
                    ScrollPane sp = new ScrollPane(gridPane_1);
                    Scene scene = new Scene(sp, 800, 500);
                    primaryStage.setScene(scene);
                    primaryStage.show();
            }
        });
        StackPane root = new StackPane();
        root.setPadding(new Insets(5));
        root.getChildren().add(table);
        gridPane.add(root, 0, 2, 10, 1);
      
        Label manLabel = new Label("Manager Logged In:\n" + ssn_1 + "");
        GridPane.setHalignment(manLabel, HPos.LEFT);
        gridPane.add(manLabel, 0,3,2,1);
        
        Button homeButton = new Button("Go Back Home");
        homeButton.setPrefHeight(20);
        homeButton.setDefaultButton(true);
        gridPane.add(homeButton, 0, 4, 6, 1);
        GridPane.setHalignment(homeButton, HPos.LEFT);
        GridPane.setMargin(homeButton, new Insets(2,0,2,0));

        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	GridPane gridPane_1 = createRegistrationFormPane();
                mainPage(gridPane_1,primaryStage, ssn_1);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
    }
    
    private static ObservableList<UserAccount> getUserList() {
        String ssn = "";
        String fname = "";
        String lname = "";
        String mint = "";
        String dob = "";
        String addr = "";
        String sex = "";
        String salary = "";
        String superssn = "";
        String dno = "";
        
        String sql = "SELECT ssn as SSN, fname AS FIRST_NAME, lname AS LAST_NAME, minit AS M_NAME,\r\n" + 
        				"bdate AS BDAY, address AS ADDRESS_1, sex AS SEX_1, salary AS SAL, superssn AS SUPER,\r\n" + 
        				"dno AS DEPT\r\n" +
        				"FROM EMPLOYEE";
        ArrayList<UserAccount> ml = new ArrayList<UserAccount>();
        
        try
        {
            Connection conn = LocalinitialSetup(1);
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(sql);
            
            while(res.next()){
                ssn = String.valueOf(res.getInt("SSN"));
                fname = res.getString("FIRST_NAME");
                lname = res.getString("LAST_NAME");
                mint = res.getString("M_NAME");
                dob = res.getString("BDAY");
                addr = res.getString("ADDRESS_1");
                sex = res.getString("SEX_1");
                salary = String.valueOf(res.getInt("SAL"));
                superssn = res.getString("SUPER");
                dno = String.valueOf(res.getInt("DEPT"));
                UserAccount user1 = new UserAccount(ssn,fname,mint,lname,addr,sex,dob,salary,superssn,dno);
                ml.add(user1);
            }

            res.close();
            st.close();
            conn.close(); 
        }
        catch(Exception ex) 
        { 
            System.err.println(ex); 
        }

        ObservableList<UserAccount> list = null;
        list = FXCollections.observableArrayList();
        for (UserAccount o : ml) {
              list.add(o);
          }
      
      return list;
  }
    
    private void viewEmployee(GridPane gridPane, Stage primaryStage, int ssn_1) {
        // Add Header
        Label headerLabel = new Label("Current Employees at Company");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,4,1);
        GridPane.setHalignment(headerLabel, HPos.LEFT);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));
        
        Label snLabel = new Label("SSN");
        snLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(snLabel, HPos.LEFT);
        gridPane.add(snLabel, 0,1);
        
        Label fLabel = new Label("First Name");
        fLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(fLabel, HPos.LEFT);
        gridPane.add(fLabel, 1,1);
        
        Label wLabel = new Label("Middle Initial");
        wLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(wLabel, HPos.LEFT);
        gridPane.add(wLabel, 2,1);
        
        Label lLabel = new Label("Last Name");
        lLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(lLabel, HPos.LEFT);
        gridPane.add(lLabel, 3,1);
        
        Label dbLabel = new Label("DOB");
        dbLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(dbLabel, HPos.LEFT);
        gridPane.add(dbLabel, 4,1);
        
        Label adLabel = new Label("Address");
        adLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(adLabel, HPos.LEFT);
        gridPane.add(adLabel, 5,1);
        
        Label sxLabel = new Label("Sex");
        sxLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(sxLabel, HPos.LEFT);
        gridPane.add(sxLabel, 6,1);
        
        Label slLabel = new Label("Salary");
        slLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(slLabel, HPos.LEFT);
        gridPane.add(slLabel, 7,1);
        
        Label ssnLabel = new Label("Supervisor SSN");
        ssnLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(ssnLabel, HPos.LEFT);
        gridPane.add(ssnLabel, 8,1);
        
        Label dnLabel = new Label("Dept #");
        dnLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(dnLabel, HPos.LEFT);
        gridPane.add(dnLabel, 9,1);
        
        //ArrayList report = new ArrayList();
        // ssn_1 is the manager editing
        String ssn = "";
        String fname = "";
        String lname = "";
        String mint = "";
        String dob = "";
        String addr = "";
        String sex = "";
        int salary = 0;
        String superssn = "";
        int dno = 0;
        //String email = "";
        
        String sql = "SELECT ssn as SSN, fname AS FIRST_NAME, lname AS LAST_NAME, minit AS M_NAME,\r\n" + 
        				"bdate AS BDAY, address AS ADDRESS_1, sex AS SEX_1, salary AS SAL, superssn AS SUPER,\r\n" + 
        				"dno AS DEPT\r\n" +
        				"FROM EMPLOYEE";
        
        try
        {
            Connection conn = LocalinitialSetup(1);
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(sql);

            int n = 2; // grid order
            
            while(res.next()){
                ssn = String.valueOf(res.getInt("SSN"));
                fname = res.getString("FIRST_NAME");
                lname = res.getString("LAST_NAME");
                mint = res.getString("M_NAME");
                dob = res.getString("BDAY");
                addr = res.getString("ADDRESS_1");
                sex = res.getString("SEX_1");
                salary = res.getInt("SAL");;
                superssn = res.getString("SUPER");
                dno = res.getInt("DEPT");

                Label ssn2Label = new Label(ssn);
                GridPane.setHalignment(ssn2Label, HPos.LEFT);
                gridPane.add(ssn2Label, 0,n);

                Label fnLabel = new Label(fname);
                GridPane.setHalignment(fnLabel, HPos.LEFT);
                gridPane.add(fnLabel, 1,n);

                Label mnLabel = new Label(mint);
                GridPane.setHalignment(mnLabel, HPos.LEFT);
                gridPane.add(mnLabel, 2,n);

                Label lnLabel = new Label(lname);
                GridPane.setHalignment(lnLabel, HPos.LEFT);
                gridPane.add(lnLabel, 3,n);

                Label db2Label = new Label(dob);
                GridPane.setHalignment(db2Label, HPos.LEFT);
                gridPane.add(db2Label, 4,n);

                Label ad2Label = new Label(addr);
                GridPane.setHalignment(ad2Label, HPos.LEFT);
                gridPane.add(ad2Label, 5,n);

                Label sx2Label = new Label(sex);
                GridPane.setHalignment(sx2Label, HPos.LEFT);
                gridPane.add(sx2Label, 6,n);

                Label sl2Label = new Label(String.valueOf(salary));
                GridPane.setHalignment(sl2Label, HPos.LEFT);
                gridPane.add(sl2Label, 7,n);

                Label sp2Label = new Label(superssn);
                GridPane.setHalignment(sp2Label, HPos.LEFT);
                gridPane.add(sp2Label, 8,n);

                Label dn2Label = new Label(String.valueOf(dno));
                GridPane.setHalignment(dn2Label, HPos.LEFT);
                gridPane.add(dn2Label, 9,n);

                Button editEmployeeButton = new Button("Edit");
                editEmployeeButton.setPrefHeight(15);
                editEmployeeButton.setDefaultButton(true);
                editEmployeeButton.setPrefWidth(100);

                gridPane.add(editEmployeeButton, 10, n, 1, 1);
                GridPane.setHalignment(editEmployeeButton, HPos.LEFT);
                GridPane.setMargin(editEmployeeButton, new Insets(10, 0,10,0));

                n = n + 1;

                editEmployeeButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        GridPane gridPane_1 = createRegistrationFormPane();
                        editUserAccount.editEmployee(gridPane_1, primaryStage, mangssn, Integer.parseInt(ssn2Label.getText().toString()));
                        ScrollPane sp = new ScrollPane(gridPane_1);
                        Scene scene = new Scene(sp, 800, 500);
                        // Set the scene in primary stage
                        primaryStage.setScene(scene);
                        primaryStage.show();
                    }
                });
            }

            res.close();
            st.close();
            conn.close(); 
        }
        catch(Exception ex) 
        { 
            System.err.println(ex); 
        }
    }
    
    // Add a new employee to database
    private static void newEmployee(GridPane gridPane, Stage primaryStage, int mgssn) {
        // Add Header
        Label headerLabel = new Label("New Employee Form");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.LEFT);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        Label fnameLabel = new Label("First Name : ");
        gridPane.add(fnameLabel, 0,1);

        TextField fnameField = new TextField("");
        fnameField.setPrefHeight(40);
        gridPane.add(fnameField, 1,1);
        
        Label lnameLabel = new Label("Last Name : ");
        gridPane.add(lnameLabel, 0,2);

        TextField lnameField = new TextField("");
        lnameField.setPrefHeight(40);
        gridPane.add(lnameField, 1,2);
        
        Label mnameLabel = new Label("Middle Initial : ");
        gridPane.add(mnameLabel, 0,3);

        TextField mnameField = new TextField("");
        mnameField.setPrefHeight(40);
        gridPane.add(mnameField, 1,3);
        
        Label ssnLabel = new Label("Social Security # : ");
        gridPane.add(ssnLabel, 0,4);

        TextField ssnField = new TextField();
        ssnField.setPrefHeight(40);
        ssnField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    ssnField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        gridPane.add(ssnField, 1,4);
        
        Label dobLabel = new Label("Date of Birth :\n(YYYY-MM-DD) ");
        gridPane.add(dobLabel, 0,5);

        DatePicker datePicker = new DatePicker();
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

        /*TextField dobField = new TextField("");
        dobField.setPrefHeight(40);
        gridPane.add(dobField, 1,5);*/
        
        Label addrLabel = new Label("Address : ");
        gridPane.add(addrLabel, 0,6);

        TextField addrField = new TextField("");
        addrField.setPrefHeight(40);
        gridPane.add(addrField, 1,6);
        
        Label sexLabel = new Label("Sex : ");
        gridPane.add(sexLabel, 0,7);
        
        final ToggleGroup sexgroup = new ToggleGroup();
        RadioButton male = new RadioButton("Male");
        male.setToggleGroup(sexgroup);

        RadioButton female = new RadioButton("Female");
        female.setToggleGroup(sexgroup);

        male.setSelected(true);
        GridPane.setHalignment(male, HPos.LEFT);
        gridPane.add(male, 1, 7);

        female.setSelected(false);
        GridPane.setHalignment(female, HPos.LEFT);
        gridPane.add(female, 1, 8);

        Label salaryLabel = new Label("Salary : ");
        gridPane.add(salaryLabel, 0,9);

        TextField salaryField = new TextField("");
        salaryField.setPrefHeight(40);
        gridPane.add(salaryField, 1,9);

        Label superLabel = new Label("Supervisor's SSN : ");
        gridPane.add(superLabel, 0,10);

        TextField superField = new TextField("");
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

        TextField depField = new TextField("");
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

        Button submitButton = new Button("Submit");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 1, 12, 2, 1);
        GridPane.setHalignment(submitButton, HPos.LEFT);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));
        
        Button homeButton = new Button("Go Back Home");
        homeButton.setPrefHeight(20);
        homeButton.setDefaultButton(true);
        gridPane.add(homeButton, 1, 13, 4, 1);
        GridPane.setHalignment(homeButton, HPos.LEFT);
        GridPane.setMargin(homeButton, new Insets(2,0,2,0));

        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	GridPane gridPane_1 = createRegistrationFormPane();
                mainPage(gridPane_1,primaryStage, mgssn);
                ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        
        Label manLabel = new Label("Manager Logged In:\n" + mangssn);
        gridPane.add(manLabel, 1,15,2,1);

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(fnameField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your first name");
                    return;
                }
                if(lnameField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your last name");
                    return;
                }
                if(ssnField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your Social Security #");
                    return;
                }
                
                String fname = fnameField.getText();
                String lname = lnameField.getText();
                String mint = mnameField.getText();
                String ssn = ssnField.getText();
                String dob = String.valueOf(datePicker.getValue());
                String addr = addrField.getText();
                
                String sex = "";
                
                if(male.isSelected())
                {
                    sex = "M";
                }
                else if(female.isSelected())
                {
                    sex = "F";
                }
                //int salary = Integer.valueOf(salaryField.getText());
                int salary = 0;
                String s = salaryField.getText();
                double d = ParseDouble(s);
                int a = (int) d;
                salary = a;
                String superssn = superField.getText();
                int dno = Integer.valueOf(depField.getText());
                //String email = emailField.getText();
        		
                try
        	{
                    Connection conn = LocalinitialSetup(1);
                    Statement st = conn.createStatement();
                    String sql = "INSERT INTO EMPLOYEE VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement pt = conn.prepareStatement(sql);

                    if(fname != "")
                    {
                        pt.setString(1, fname);
                    }
                    else
                    {
                        pt.setNull(1, Types.VARCHAR);
                    }

                    if(lname != "")
                    {
                        pt.setString(3, lname);
                    }
                    else
                    {
                        pt.setNull(3, Types.VARCHAR);
                    }

                    if(mint != "")
                    {
                        pt.setString(2, mint);
                    }
                    else
                    {
                        pt.setNull(2, Types.VARCHAR);
                    }

                    if(ssn != "")
                    {
                        pt.setString(4, ssn);
                    }
                    else
                    {
                        pt.setNull(4, Types.VARCHAR);
                    }

                    if(dob != "")
                    {
                        pt.setString(5, dob);
                    }
                    else
                    {

                        pt.setNull(5, Types.DATE);
                    }

                    if(addr != "")
                    {
                        pt.setString(6, addr);
                    }
                    else
                    {
                        pt.setNull(6, Types.VARCHAR);
                    }

                    if(sex != "")
                    {
                        pt.setString(7, sex);
                    }
                    else
                    {
                        pt.setNull(7, Types.VARCHAR);
                    }

                    if(salary >= 0)
                    {
                        pt.setInt(8, salary);
                    }
                    else
                    {
                        pt.setNull(8, Types.INTEGER);
                    }

                    if(superssn != "")
                    {
                        pt.setString(9, superssn);
                    }
                    else
                    {
                        pt.setNull(9, Types.VARCHAR);
                    }

                    if(dno >= 0)
                    {
                        pt.setInt(10, dno);
                    }
                    else
                    {
                        pt.setNull(10, Types.INTEGER);
                    }
                    /*
                    if(email != "")
                    {
                            //pt.setString(11, email);
                    }
                    else
                    {
                            //pt.setNull(11, Types.VARCHAR);
                    }*/

                    pt.executeUpdate();

                    pt.close();
                    st.close();

                    conn.close();
                    showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "New Employee Added Successfully!", "Name: " + fnameField.getText());
                    empssn = Integer.valueOf(ssn);
                }
                catch(Exception ex) 
                {
                    showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "New Employee NOT Added!", "Please try again.\n" + ex);
                    System.err.println(ex);
                    return;
                }
                
                GridPane gridPane_1 = createRegistrationFormPane();

            	assignProject(gridPane_1, primaryStage, mangssn, Integer.parseInt(ssn));
            	ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                // Set the scene in primary stage
                primaryStage.setScene(scene);
                primaryStage.show();
                // go to next screen
            }
        });
    }

    // Show alerts
    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
    
    // Display and/or Print all of the new employee's info
    public static void finalReport(GridPane gridPane, Stage primaryStage, int ssn_1, int emp_ssn)
    {
    	ArrayList report = new ArrayList();
    	
        Label headerLabel = new Label("Final Report for Employee");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,4,1);
        report.add(headerLabel);
        GridPane.setHalignment(headerLabel, HPos.LEFT);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));
        
        String ssn = String.valueOf(emp_ssn);
        String fname = "";
        String lname = "";
        String mint = "";
        String dob = "";
        String addr = "";
        String sex = "";
        int salary = 0;
        String superssn = "";
        int dno = 0;
        // String email = "";
        
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
        
        Label headerLabel2 = new Label("Employee Information");
        headerLabel2.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gridPane.add(headerLabel2, 0,1,4,1);
        GridPane.setHalignment(headerLabel2, HPos.LEFT); 
        
        Label fnameLabel = new Label("First Name : ");
        GridPane.setHalignment(fnameLabel, HPos.LEFT);
        gridPane.add(fnameLabel, 0,2);
        
        Label fnameField = new Label(fname);
        GridPane.setHalignment(fnameField, HPos.LEFT);
        gridPane.add(fnameField, 1,2);
        
        Label lnameLabel = new Label("Last Name : ");
        GridPane.setHalignment(lnameLabel, HPos.LEFT);
        gridPane.add(lnameLabel, 0,3);

        Label lnameField = new Label(lname);
        GridPane.setHalignment(lnameField, HPos.LEFT);
        gridPane.add(lnameField, 1,3);
        
        Label mnameLabel = new Label("Middle Initial : ");
        GridPane.setHalignment(mnameLabel, HPos.LEFT);
        gridPane.add(mnameLabel, 0,4);

        Label mnameField = new Label(mint);
        GridPane.setHalignment(mnameField, HPos.LEFT);
        gridPane.add(mnameField, 1,4);
        
        Label ssnLabel = new Label("SSN : ");
        GridPane.setHalignment(ssnLabel, HPos.LEFT);
        gridPane.add(ssnLabel, 0,5);

        Label ssnField = new Label(ssn);
        GridPane.setHalignment(ssnField, HPos.LEFT);
        gridPane.add(ssnField, 1,5);
        
        Label dobLabel = new Label("Date of Birth : ");
        GridPane.setHalignment(dobLabel, HPos.LEFT);
        gridPane.add(dobLabel, 0,6);

        Label dobField = new Label(dob);
        GridPane.setHalignment(dobField, HPos.LEFT);
        gridPane.add(dobField, 1,6);
        
        Label addrLabel = new Label("Address : ");
        GridPane.setHalignment(addrLabel, HPos.LEFT);
        gridPane.add(addrLabel, 0,7);

        Label addrField = new Label(addr);
        GridPane.setHalignment(addrField, HPos.LEFT);
        gridPane.add(addrField, 1,7);
        
        Label sexLabel = new Label("Sex : ");
        GridPane.setHalignment(sexLabel, HPos.LEFT);
        gridPane.add(sexLabel, 0,8);

        Label sexField = new Label(sex);
        GridPane.setHalignment(sexField, HPos.LEFT);
        gridPane.add(sexField, 1,8);
        
        Label salaryLabel = new Label("Salary : ");
        GridPane.setHalignment(salaryLabel, HPos.LEFT);
        gridPane.add(salaryLabel, 0,9);

        Label salaryField = new Label(String.valueOf(salary));
        GridPane.setHalignment(salaryField, HPos.LEFT);
        gridPane.add(salaryField, 1,9);
        
        Label ssLabel = new Label("Super SSN : ");
        GridPane.setHalignment(ssLabel, HPos.LEFT);
        gridPane.add(ssLabel, 0,10);

        Label ssField = new Label(superssn);
        GridPane.setHalignment(ssField, HPos.LEFT);
        gridPane.add(ssField, 1,10);
        
        Label dLabel = new Label("Dept # : ");
        GridPane.setHalignment(dLabel, HPos.LEFT);
        gridPane.add(dLabel, 0,11);

        Label dField = new Label(String.valueOf(dno));
        GridPane.setHalignment(dField, HPos.LEFT);
        gridPane.add(dField, 1,11);
        
        /*Label eLabel = new Label("Email : ");
        GridPane.setHalignment(eLabel, HPos.LEFT);
        gridPane.add(eLabel, 0,12);

        Label eField = new Label(email);
        GridPane.setHalignment(eField, HPos.LEFT);
        gridPane.add(eField, 1,12);*/
        
        Label headerLabel3 = new Label("Projects and Hours");
        headerLabel3.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gridPane.add(headerLabel3, 0,13,3,1);
        GridPane.setHalignment(headerLabel3, HPos.LEFT);
        
        Label pLabel = new Label("Project Name");
        pLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(pLabel, HPos.LEFT);
        gridPane.add(pLabel, 0,14);
        
        Label wLabel = new Label("Work Hours");
        wLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(wLabel, HPos.LEFT);
        gridPane.add(wLabel, 1,14);
        
        String sql2 = "SELECT pname AS PROJ_NAME, hours AS WORK_HOURS\r\n" + 
				"FROM PROJECT, WORKS_ON\r\n" + 
				"WHERE WORKS_ON.essn = " + ssn + " AND WORKS_ON.pno = PROJECT.pnumber";
        
        String project_name = "";
        int work_hours = 0;

        int n = 15; // grid order
        
        try
        {
            Connection conn2 = LocalinitialSetup(1);
            Statement st = conn2.createStatement(); 
            ResultSet res = st.executeQuery(sql2);

            while(res.next()){
                 project_name = res.getString("PROJ_NAME");
                 
                 work_hours = res.getInt("WORK_HOURS");

                 Label pnLabel = new Label(project_name);
                 GridPane.setHalignment(pnLabel, HPos.LEFT);
                 gridPane.add(pnLabel, 0,n);

                 Label wnLabel = new Label(String.valueOf(work_hours));
                 GridPane.setHalignment(wnLabel, HPos.LEFT);
                 gridPane.add(wnLabel, 1,n);
                         n = n + 1;
            }

            res.close();
            st.close();
            conn2.close(); 
        }
        catch(Exception ex) 
        { 
            System.err.println(ex); 
        }
		
        Label headerLabel4 = new Label("Dependents");
        headerLabel4.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gridPane.add(headerLabel4, 0,n,3,1);
        GridPane.setHalignment(headerLabel4, HPos.LEFT);
        
        n = n + 1;
        
        Label nameLabel = new Label("Name");
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(nameLabel, HPos.LEFT);
        gridPane.add(nameLabel, 0,n);
        
        Label sexLabel2 = new Label("Sex");
        sexLabel2.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(sexLabel2, HPos.LEFT);
        gridPane.add(sexLabel2, 1,n);
        
        Label dobLabel2 = new Label("Date of Birth");
        dobLabel2.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(dobLabel2, HPos.LEFT);
        gridPane.add(dobLabel2,2,n);

        Label rLabel2 = new Label("Relationship");
        rLabel2.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setHalignment(rLabel2, HPos.LEFT);
        gridPane.add(rLabel2, 3,n);
        
        String sql3 = "SELECT dependent_name AS D_NAME, sex AS D_SEX, bdate AS D_DOB, relationship AS D_REL\r\n" + 
				"FROM DEPENDENT\r\n" + 
				"WHERE essn = " + ssn + "";
        
        String dep_name = "";
        String d_sex = "";
        String d_dob = "";
        String d_rel = "";
        n = n + 1; // grid order
        
        try
        {
            Connection conn3 = LocalinitialSetup(1);
            Statement st2 = conn3.createStatement();
            ResultSet res2 = st2.executeQuery(sql3);

            while(res2.next()){
                dep_name = res2.getString("D_NAME");
                d_sex = res2.getString("D_SEX");
                d_dob = res2.getString("D_DOB");
                d_rel = res2.getString("D_REL");

                Label dnLabel = new Label(dep_name);
                GridPane.setHalignment(dnLabel, HPos.LEFT);
                gridPane.add(dnLabel, 0,n);

                Label dsLabel = new Label(d_sex);
                GridPane.setHalignment(dsLabel, HPos.LEFT);
                gridPane.add(dsLabel, 1,n);

                Label dbLabel = new Label(d_dob);
                GridPane.setHalignment(dbLabel, HPos.LEFT);
                gridPane.add(dbLabel, 2,n);

                Label drLabel = new Label(d_rel);
                GridPane.setHalignment(drLabel, HPos.LEFT);
                gridPane.add(drLabel, 3,n);

                n = n + 1;
            }
            res2.close();
            st2.close();
            conn3.close(); 
        }
        catch(Exception ex) 
        { 
            System.err.println(ex); 
        }

        n = n + 1;

        Button submitButton = new Button("Print Report");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, n, 1, 1);
        GridPane.setHalignment(submitButton, HPos.LEFT);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));
        
        n = n + 1;

        Button goHomeButton = new Button("Go Back Home");
        goHomeButton.setPrefHeight(40);
        goHomeButton.setDefaultButton(true);
        goHomeButton.setPrefWidth(100);
        gridPane.add(goHomeButton, 0, n, 4, 1);
        GridPane.setHalignment(goHomeButton, HPos.LEFT);
        GridPane.setMargin(goHomeButton, new Insets(2,0,2,0));
        
        n = n + 1;
        
        Label manLabel = new Label("Manager Logged In:\n" + ssn_1 + "\n\nEmployee being edited:\n" + emp_ssn);
        GridPane.setHalignment(manLabel, HPos.LEFT);
        gridPane.add(manLabel, 0,n,2,1);
        
        goHomeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane gridPane_1 = createRegistrationFormPane();
            	mainPage(gridPane_1,primaryStage, ssn_1);
            	ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                // Set the scene in primary stage
                primaryStage.setScene(scene);
                primaryStage.show();
                // go to next screen
            }
        });

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	PrinterJob printerJob = PrinterJob.createPrinterJob();
            	PrinterJob job = PrinterJob.createPrinterJob();

            	if (printerJob != null && printerJob.showPrintDialog(gridPane.getScene().getWindow())) {
            	  PageLayout pageLayout = printerJob.getPrinter().createPageLayout(Paper.A5, PageOrientation.PORTRAIT, 0, 0, 0, 0);
            	  final double  prnW = pageLayout.getPrintableWidth();
              	  final double  prnH = pageLayout.getPrintableHeight();
              	  
              	final int  pagesAcross = (int) Math.ceil( gridPane.getWidth() / prnW );
              	final int  pagesDown = (int) Math.ceil( gridPane.getHeight() / prnH );
              	boolean success = true;
              	for ( int pgRow = 0; pgRow < pagesDown; pgRow++ )
              	{
                    for ( int pgCol = 0; pgCol < pagesAcross; pgCol++ )
              	    {
              	        gridPane.setTranslateX( -(prnW * pgCol) );
              	        gridPane.setTranslateY( -(prnH * pgRow) );
              	        success = printerJob.printPage(pageLayout, gridPane);
              	    }
              	}
              	if (success) {
        	        printerJob.endJob();
        	    }
            	}
            	
            	GridPane gridPane_1 = createRegistrationFormPane();

            	finalReport(gridPane_1, primaryStage, ssn_1,empssn);
            	ScrollPane sp = new ScrollPane(gridPane_1);
                Scene scene = new Scene(sp, 800, 500);
                // Set the scene in primary stage
                primaryStage.setScene(scene);
                primaryStage.show();
                // go to next screen
            }
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

    public static void main(String[] args) {
        launch(args);
    }
}

class Launcher {

    public static void main(String[] args) {
        MainAccess.main(args);
    }
}