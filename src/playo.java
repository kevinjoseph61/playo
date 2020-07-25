import javafx.application.*;
import javafx.collections.FXCollections;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class playo extends Application {

    private Stage stage;
    private Stage secondaryStage;
    private String selected;
    private LocalDate selectedDate;
    private int selectedInt;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        mainPage();
    }

    private void mainPage() {
        stage.setTitle("Select User");
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);

        Label playoLabel = new Label("PlayO");

        playoLabel.setTextFill(Color.web("#4dde0b", 0.8));
        playoLabel.setStyle("-fx-font-weight: bold");
        playoLabel.setFont(new Font("TimesRoman", 50));

        Button playerButton = new Button("Player");
        playerButton.setStyle(cssStrings.buttonStyle(60,120));
        playerButton.setOnMouseEntered(event -> playerButton.setStyle(cssStrings.onButtonStyle(60,120)));
        playerButton.setOnMouseExited(event -> playerButton.setStyle(cssStrings.buttonStyle(60,120)));
        playerButton.setOnAction(event -> playerFirstPage());

        Button ownerButton = new Button("Arena Owner");
        ownerButton.setStyle(cssStrings.buttonStyle(60,160));
        ownerButton.setOnMouseEntered(event -> ownerButton.setStyle(cssStrings.onButtonStyle(60,160)));
        ownerButton.setOnMouseExited(event -> ownerButton.setStyle(cssStrings.buttonStyle(60,160)));
        ownerButton.setOnAction(event -> ownerFirstPage());

        vbox.getChildren().addAll(playoLabel, playerButton, ownerButton);

        stage.setScene(new Scene(vbox, 400, 500));
        stage.show();
    }

    private void playerFirstPage() {
        stage.setTitle("Select Login/Sign-Up");
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);

        Label playerLabel = new Label("Player");

        playerLabel.setTextFill(Color.web("#4dde0b", 0.8));
        playerLabel.setStyle("-fx-font-weight: bold");
        playerLabel.setFont(new Font("TimesRoman", 50));

        Button logInButton = new Button("Login");
        logInButton.setStyle(cssStrings.buttonStyle(60,120));
        logInButton.setOnAction(event -> playerLoginPage());
        logInButton.setOnMouseEntered(event -> logInButton.setStyle(cssStrings.onButtonStyle(60,120)));
        logInButton.setOnMouseExited(event -> logInButton.setStyle(cssStrings.buttonStyle(60,120)));

        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(event -> playerSignUp());
        signUpButton.setStyle(cssStrings.buttonStyle(60,120));
        signUpButton.setOnMouseEntered(event -> signUpButton.setStyle(cssStrings.onButtonStyle(60,120)));
        signUpButton.setOnMouseExited(event -> signUpButton.setStyle(cssStrings.buttonStyle(60,120)));

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> mainPage());
        backButton.setStyle(cssStrings.buttonStyle(60,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(60,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(60,120)));

        vbox.getChildren().addAll(playerLabel,logInButton, signUpButton, backButton);

        stage.setScene(new Scene(vbox, 400, 500,Color.WHITE));
        stage.show();
    }

    private void playerLoginPage() {
        stage.setTitle("Login");
        Label loginLabel = new Label("Player-Login");
        loginLabel.setTextFill(Color.web("#4dde0b", 0.8));
        loginLabel.setStyle("-fx-font-weight: bold");
        loginLabel.setFont(new Font("TimesRoman", 50));

        Label emailText = new Label("Email");
        emailText.setStyle("-fx-font-weight: bold");
        Label passwordText = new Label("Password");
        passwordText.setStyle("-fx-font-weight: bold");

        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();

        Button submitButton = new Button("Submit");
        submitButton.setStyle(cssStrings.buttonStyle(60,120));
        submitButton.setOnMouseEntered(event -> submitButton.setStyle(cssStrings.onButtonStyle(60,120)));
        submitButton.setOnMouseExited(event ->submitButton.setStyle(cssStrings.buttonStyle(60,120)));
        submitButton.setOnAction(event -> {
            String password = passwordField.getText();
            String email = emailField.getText().toLowerCase();
            if(password.length()==0 || email.length()==0){
                MessageBox.show("Enter a valid username and password!", "Invalid Entry",false);
                return;
            }
            playerObject player = new playerObject();
            boolean flag=false;
            try(
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM player WHERE email='"+email+"';")
            ){
                while(rs.next()){
                    flag=true;
                    player.playerName=rs.getString("pname");
                    player.playerEmail=rs.getString("email");
                    player.playerRegion=rs.getString("region");
                    player.gender=rs.getString("gender");
                    player.playerCity=rs.getString("city");
                    player.playerPassword=rs.getString("password");
                    player.playerDOB=rs.getObject("dob",LocalDate.class);
                }

            }catch (SQLException e) {
                e.printStackTrace();
            }
            if(!flag) {
                MessageBox.show("Email does not exist! Sign up!", "Invalid Entry",false);
                return;
            }
            if(!password.equals(player.playerPassword)) {
                MessageBox.show("Incorrect password!", "Invalid Entry",false);
                return;
            }
            MessageBox.show("You have successfully logged in!","Approved",true);
            playerDashboard(player);
        });

        Button backButton = new Button("Back");
        backButton.setStyle(cssStrings.buttonStyle(60,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(60,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(60,120)));
        backButton.setOnAction(event -> playerFirstPage());

        GridPane gridPane = new GridPane();

        gridPane.setMinSize(500, 500);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(loginLabel, 0, 0, 5, 1);
        gridPane.add(emailText, 1, 4);
        gridPane.add(emailField, 3, 4);
        gridPane.add(passwordText, 1, 5);
        gridPane.add(passwordField, 3, 5);
        gridPane.add(submitButton, 1, 6);
        gridPane.add(backButton, 3, 6);
        stage.setScene(new Scene(gridPane, 400, 500,Color.WHITE));
        stage.show();
    }

    private void playerSignUp() {
        stage.setTitle("Sign Up");

        Label signUpLabel = new Label("Player-Sign-Up");
        signUpLabel.setTextFill(Color.web("#4dde0b", 0.8));
        signUpLabel.setStyle("-fx-font-weight: bold");
        signUpLabel.setFont(new Font("TimesRoman", 50));

        Label nameText = new Label("Name");
        nameText.setStyle("-fx-font-weight: bold ;" );
        Label genderText = new Label("Gender");
        genderText.setStyle("-fx-font-weight: bold ;" );
        Label DOBText = new Label("Date of Birth");
        DOBText.setStyle("-fx-font-weight: bold ;" );
        Label cityText = new Label("City");
        cityText.setStyle("-fx-font-weight: bold ;" );
        Label regionText = new Label("Select City Region");
        regionText.setStyle("-fx-font-weight: bold ;" );
        Label emailText = new Label("Email");
        emailText.setStyle("-fx-font-weight: bold ;" );
        Label passwordText = new Label("Password");
        passwordText.setStyle("-fx-font-weight: bold ;" );
        Label rePasswordText = new Label("Confirm Password");
        rePasswordText.setStyle("-fx-font-weight: bold ;" );

        TextField nameField = new TextField();
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField rePasswordField = new PasswordField();

        ToggleGroup groupGender = new ToggleGroup();
        RadioButton maleRadio = new RadioButton("Male");
        maleRadio.setToggleGroup(groupGender);
        RadioButton femaleRadio = new RadioButton("Female");
        femaleRadio.setToggleGroup(groupGender);
        maleRadio.setSelected(true);
        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(maleRadio,femaleRadio);

        DatePicker DOBDate = new DatePicker();

        ChoiceBox cityChoiceBox = new ChoiceBox();
        cityChoiceBox.getItems().addAll("Bangalore", "Chennai", "Delhi", "Mumbai");
        cityChoiceBox.getSelectionModel().select(0);
        ChoiceBox regionChoiceBox = new ChoiceBox();
        regionChoiceBox.getItems().addAll("North", "South", "East", "West", "Central");
        regionChoiceBox.getSelectionModel().select(0);

        Button submitButton = new Button("Sign Up");
        submitButton.setStyle(cssStrings.buttonStyle(60,120));
        submitButton.setOnMouseEntered(event -> submitButton.setStyle(cssStrings.onButtonStyle(60,120)));
        submitButton.setOnMouseExited(event -> submitButton.setStyle(cssStrings.buttonStyle(60,120)));

        submitButton.setOnAction(event -> {
                    playerObject player = new playerObject();

                    player.playerName = nameField.getText();
                    if (player.playerName.length() == 0) {
                        MessageBox.show("Please Enter Your Name!", "Invalid Entry",false);
                        return;
                    }

                    player.gender = ((RadioButton) groupGender.getSelectedToggle()).getText();

                    DOBDate.setValue(DOBDate.getConverter().fromString(DOBDate.getEditor().getText()));

                    player.playerDOB = DOBDate.getValue();
                    if (player.playerDOB == null) {
                        MessageBox.show("Please Enter a Valid Date!", "Invalid Entry",false);
                        return;
                    }

                    player.playerCity = (String) cityChoiceBox.getValue();
                    player.playerRegion = (String) regionChoiceBox.getValue();

                    player.playerEmail = emailField.getText();
                    if (!Pattern.compile("^[A-Za-z0-9._%-]+@[A-Za-z0-9._-]+\\.[A-Za-z]{2,4}$").matcher(player.playerEmail).find()) {
                        MessageBox.show("Please Enter a Valid E-mail!", "Invalid Entry",false);
                        return;
                    }

                    boolean flag=false;
                    try(
                            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx");
                            Statement stmt = conn.createStatement();
                            ResultSet rs = stmt.executeQuery("SELECT * FROM player WHERE email='"+player.playerEmail+"';")
                    ){
                        while(rs.next()){
                            flag=true;
                        }
                    }catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if(flag) {
                        MessageBox.show("This email is already associated with another account; Please login", "Invalid Entry",false);
                        return;
                    }

                    if(!passwordField.getText().equals(rePasswordField.getText())){
                        MessageBox.show("Passwords do not match. Please recheck", "Invalid Entry",false);
                        return;
                    }
                    player.playerPassword = passwordField.getText();
                    if(player.playerPassword.length()<6) {
                        MessageBox.show("Password is too short!", "Invalid Password",false);
                        return;
                    }
                    if (!Pattern.compile("^(?=.*[0-9])").matcher(player.playerPassword).find()) {
                        MessageBox.show("Password must contain a number!", "Invalid Password",false);
                        return;
                    }
                    if (!Pattern.compile("^(?=.*[a-z])").matcher(player.playerPassword).find()) {
                        MessageBox.show("Password must contain a lower-case alphabet!", "Invalid Password",false);
                        return;
                    }
                    if (!Pattern.compile("^(?=.*[A-Z])").matcher(player.playerPassword).find()) {
                        MessageBox.show("Password must contain an upper-case alphabet!", "Invalid Password",false);
                        return;
                    }
                    if (!Pattern.compile("^(?=.*[-@#$%^&+=,!_])").matcher(player.playerPassword).find()) {
                        MessageBox.show("Password must contain a special character!", "Invalid Password",false);
                        return;
                    }
                    try(
                            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx")
                    ){
                        PreparedStatement stmt=  conn.prepareStatement("INSERT INTO player VALUES (?,?,?,?,?,?,?);");
                        stmt.setString(1,player.playerName);
                        stmt.setString(2,player.playerEmail);
                        stmt.setString(3,player.playerRegion);
                        stmt.setString(4,player.gender);
                        stmt.setString(5,player.playerCity);
                        stmt.setString(6,player.playerPassword);
                        stmt.setObject(7,player.playerDOB);
                        stmt.executeUpdate();
                    }
                    catch(SQLException ex) {
                        ex.printStackTrace();
                    }
                    MessageBox.show("You have successfully signed up!","Approved",true);

                    playerDashboard(player);
                }
        );
        Button backButton = new Button("Back");
        backButton.setStyle(cssStrings.buttonStyle(60,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(60,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(60,120)));

        backButton.setOnAction(event -> playerFirstPage());

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(500, 500);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(signUpLabel,0,0,2,1);
        gridPane.add(nameText, 0, 1);
        gridPane.add(nameField, 1, 1);
        gridPane.add(genderText, 0, 2);
        gridPane.add(hBox, 1, 2);
        gridPane.add(DOBText, 0, 3);
        gridPane.add(DOBDate, 1, 3);
        gridPane.add(cityText, 0, 4);
        gridPane.add(cityChoiceBox, 1, 4);
        gridPane.add(regionText, 0, 5);
        gridPane.add(regionChoiceBox, 1, 5);
        gridPane.add(emailText, 0, 6);
        gridPane.add(emailField, 1, 6);
        gridPane.add(passwordText, 0, 7);
        gridPane.add(passwordField, 1, 7);
        gridPane.add(rePasswordText, 0, 8);
        gridPane.add(rePasswordField, 1, 8);
        gridPane.add(submitButton, 0, 9);
        gridPane.add(backButton, 1, 9);

        stage.setScene(new Scene(gridPane, 400, 500,Color.WHITE));
        stage.show();
    }

    private void playerDashboard(playerObject player) {
        stage.setTitle("Welcome "+player.playerName);
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);

        Label dashboardLabel = new Label("Player-Dashboard");
        dashboardLabel.setTextFill(Color.web("#4dde0b", 0.8));
        dashboardLabel.setStyle("-fx-font-weight: bold");
        dashboardLabel.setFont(new Font("TimesRoman", 40));

        Button newBookButton = new Button("New Booking");
        newBookButton.setStyle(cssStrings.buttonStyle(60,160));
        newBookButton.setOnMouseEntered(event -> newBookButton.setStyle(cssStrings.onButtonStyle(60,160)));
        newBookButton.setOnMouseExited(event -> newBookButton.setStyle(cssStrings.buttonStyle(60,160)));
        newBookButton.setOnAction(event -> selectArena(player));

        Button viewBookButton = new Button("View Bookings");
        viewBookButton.setStyle(cssStrings.buttonStyle(60,160));
        viewBookButton.setOnMouseEntered(event -> viewBookButton.setStyle(cssStrings.onButtonStyle(60,160)));
        viewBookButton.setOnMouseExited(event -> viewBookButton.setStyle(cssStrings.buttonStyle(60,160)));
        viewBookButton.setOnAction(event -> viewBooking(player));

        Button newHostButton = new Button("Join a Hosting");
        newHostButton.setStyle(cssStrings.buttonStyle(60,160));
        newHostButton.setOnMouseEntered(event -> newHostButton.setStyle(cssStrings.onButtonStyle(60,160)));
        newHostButton.setOnMouseExited(event -> newHostButton.setStyle(cssStrings.buttonStyle(60,160)));
        newHostButton.setOnAction(event -> newHosting(player));

        Button viewHostButton = new Button("Shared Games");
        viewHostButton.setStyle(cssStrings.buttonStyle(60,160));
        viewHostButton.setOnMouseEntered(event -> viewHostButton.setStyle(cssStrings.onButtonStyle(60,160)));
        viewHostButton.setOnMouseExited(event -> viewHostButton.setStyle(cssStrings.buttonStyle(60,160)));
        viewHostButton.setOnAction(event -> viewHosting(player));

        Button backButton = new Button("Sign Out");
        backButton.setStyle(cssStrings.buttonStyle(60,160));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(60,160)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(60,160)));
        backButton.setOnAction(event -> mainPage());

        vbox.getChildren().addAll(dashboardLabel,newBookButton, viewBookButton, newHostButton, viewHostButton, backButton);

        stage.setScene(new Scene(vbox, 400, 500,Color.WHITE));
        stage.show();
    }

    private void selectArena(playerObject player){
        selectSport();
        ArrayList<arenaObject> arenas = new ArrayList<arenaObject>();
        arenaObject arena = new arenaObject();
        arena.arenaSport = selected;
        Label region = new Label("Arenas in your region");
        Label city = new Label("Arenas elsewhere in your city");
        TilePane tile1 = new TilePane();
        tile1.setHgap(10);
        tile1.setVgap(10);
        tile1.setPrefColumns(1);
        tile1.setPrefTileWidth(350);
        tile1.setPadding(new Insets(10, 10, 10, 10));
        Button backButton = new Button("Back");
        backButton.setStyle(cssStrings.buttonStyle(50,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(50,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(50,120)));
        backButton.setOnAction(event -> playerDashboard(player));
        int i=0;
        boolean flag=false;
        try(
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM arena WHERE sname='"+arena.arenaSport+"' and city='"+player.playerCity+"' and region='"+player.playerRegion+"' order by aname;");
        ){
            StackPane sp = new StackPane(region);
            sp.setAlignment(region,Pos.CENTER_LEFT);
            tile1.getChildren().add(sp);
            while(rs.next()){
                flag=true;
                arena.arenaID = rs.getInt("aid");
                arena.arenaName=rs.getString("aname");
                arena.openingTime= LocalTime.parse(rs.getString("opening"));
                arena.closingTime= LocalTime.parse(rs.getString("closing"));
                arena.arenaCity=rs.getString("city");
                arena.arenaRegion=rs.getString("region");
                arena.arenaSport=rs.getString("sname");
                arena.ownerMobile=rs.getString("mobile");
                arenas.add(new arenaObject(arena));

                Rectangle r = new Rectangle(350, 50);
                r.setFill(Color.web("#269e1e"));

                Statement stm = conn.createStatement();
                ResultSet rst = stm.executeQuery("SELECT count(*) FROM court WHERE aid='"+arena.arenaID+"';");
                rst.next();
                int nCourts=rst.getInt("count(*)");

                Label name = new Label(arena.arenaName);
                name.setStyle("-fx-font-weight: bold ; -fx-font-size: 18;" );
                name.setTextFill(Color.WHITE);
                Label time = new Label(arena.openingTime+"  to  "+arena.closingTime);
                time.setTextFill(Color.WHITE);
                Label sport = new Label(arena.arenaCity+" "+arena.arenaRegion);
                sport.setTextFill(Color.WHITE);
                Label court = new Label("Number of courts: "+nCourts);
                court.setTextFill(Color.WHITE);

                StackPane s = new StackPane(r, name, time, sport, court);
                s.setAlignment(name, Pos.TOP_LEFT);
                s.setAlignment(time, Pos.BOTTOM_LEFT);
                s.setAlignment(sport, Pos.TOP_RIGHT);
                s.setAlignment(court, Pos.BOTTOM_RIGHT);
                final int x=i;
                s.setOnMouseClicked(e -> selectCourt(player, arenas.get(x)));
                i++;
                tile1.getChildren().add(s);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        if(!flag) {
            Label empty = new Label("None to display! Wait while new arenas are added in your region :)");
            StackPane sp = new StackPane(empty);
            sp.setAlignment(empty,Pos.CENTER_LEFT);
            tile1.getChildren().add(sp);
        }
        flag=false;
        try(
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from playo.arena as sup where sname='"+arena.arenaSport+"' and city='"+player.playerCity+"' and not exists(select * from playo.arena as sub where sname='"+arena.arenaSport+"' and city='"+player.playerCity+"' and region='"+player.playerRegion+"' and sup.aid=sub.aid) order by aname;");
        ){
            StackPane sp = new StackPane(city);
            sp.setAlignment(city,Pos.CENTER_LEFT);
            tile1.getChildren().add(sp);
            while(rs.next()){
                flag=true;
                arena.arenaID = rs.getInt("aid");
                arena.arenaName=rs.getString("aname");
                arena.openingTime= LocalTime.parse(rs.getString("opening"));
                arena.closingTime= LocalTime.parse(rs.getString("closing"));
                arena.arenaCity=rs.getString("city");
                arena.arenaRegion=rs.getString("region");
                arena.arenaSport=rs.getString("sname");
                arena.ownerMobile=rs.getString("mobile");
                arenas.add(new arenaObject(arena));

                Rectangle r = new Rectangle(350, 50);
                r.setFill(Color.web("#269e1e"));

                Statement stm = conn.createStatement();
                ResultSet rst = stm.executeQuery("SELECT count(*) FROM court WHERE aid='"+arena.arenaID+"';");
                rst.next();
                int nCourts=rst.getInt("count(*)");

                Label name = new Label(arena.arenaName);
                name.setStyle("-fx-font-weight: bold ; -fx-font-size: 18;" );
                name.setTextFill(Color.WHITE);
                Label time = new Label(arena.openingTime+"  to  "+arena.closingTime);
                time.setTextFill(Color.WHITE);
                Label sport = new Label(arena.arenaCity+" "+arena.arenaRegion);
                sport.setTextFill(Color.WHITE);
                Label court = new Label("Number of courts: "+nCourts);
                court.setTextFill(Color.WHITE);

                StackPane s = new StackPane(r, name, time, sport, court);
                s.setAlignment(name, Pos.TOP_LEFT);
                s.setAlignment(time, Pos.BOTTOM_LEFT);
                s.setAlignment(sport, Pos.TOP_RIGHT);
                s.setAlignment(court, Pos.BOTTOM_RIGHT);
                final int x=i;
                s.setOnMouseClicked(e -> selectCourt(player, arenas.get(x)));
                i++;
                tile1.getChildren().add(s);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        if(!flag) {
            Label empty = new Label("None to display! Wait while new arenas are added elsewhere in your city :)");
            StackPane sp = new StackPane(empty);
            sp.setAlignment(empty,Pos.CENTER_LEFT);
            tile1.getChildren().add(sp);
        }
        ScrollPane spane = new ScrollPane(tile1);
        spane.setMinWidth(390);
        spane.setPrefWidth(390);
        spane.setMaxWidth(390);
        spane.setMinHeight(400);
        spane.setMaxHeight(400);
        spane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        StackPane stack = new StackPane(spane);
        stack.setMargin(spane, new Insets(20, 40, 20, 40));
        VBox vBox = new VBox(stack,backButton);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox,400,500);
        stage.setScene(scene);
        stage.setTitle("Select Arena");
        stage.show();
    }

    private void selectSport() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Select Sport");
        stage.setMinWidth(350);
        stage.setMinHeight(200);
        Label lbl = new Label("Which sport are you in the mood for?");
        lbl.setStyle("-fx-font-weight: bold");
        lbl.setFont(new Font("TimesRoman", 18));
        ChoiceBox sportChoiceBox = new ChoiceBox(FXCollections.observableArrayList(arenaObject.sports));
        sportChoiceBox.getSelectionModel().select(0);
        Button btnOK = new Button();
        btnOK.setText("Select");
        btnOK.setStyle(cssStrings.buttonStyle(40,90));
        btnOK.setOnMouseEntered(event -> btnOK.setStyle(cssStrings.onButtonStyle(40,90)));
        btnOK.setOnMouseExited(event -> btnOK.setStyle(cssStrings.buttonStyle(40,90)));

        btnOK.setOnAction(e -> {
            selected = (String) sportChoiceBox.getSelectionModel().getSelectedItem();
            stage.close();
        });
        VBox pane = new VBox(20);

        pane.getChildren().addAll(lbl, sportChoiceBox, btnOK);
        pane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void selectCourt(playerObject player, arenaObject arena){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Select Court");
        stage.setMinWidth(400);
        stage.setMinHeight(500);
        secondaryStage=stage;
        TilePane tile1 = new TilePane();
        tile1.setHgap(10);
        tile1.setVgap(10);
        tile1.setPrefColumns(1);
        tile1.setPrefTileWidth(350);
        tile1.setPadding(new Insets(10, 10, 10, 10));

        ArrayList<courtObject> courts = new ArrayList<courtObject>();
        courtObject court = new courtObject();

        Button backButton = new Button("Back");
        backButton.setStyle(cssStrings.buttonStyle(50,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(50,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(50,120)));
        backButton.setOnAction(event -> stage.close());
        int i=0;
        boolean flag=false;
        try(
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM court WHERE aid='"+arena.arenaID+"';");
        ){
            while(rs.next()){
                flag=true;
                court.arenaID = rs.getInt("aid");
                court.cNum=rs.getInt("cnum");
                court.courtID=rs.getInt("cid");
                court.type=rs.getString("type");
                court.cost=rs.getFloat("cost");
                courts.add(new courtObject(court));

                Rectangle r = new Rectangle(350, 50);
                r.setFill(Color.web("#269e1e"));

                Label name = new Label("Court "+court.cNum);
                name.setStyle("-fx-font-weight: bold ; -fx-font-size: 18;" );
                name.setTextFill(Color.WHITE);
                Label cost = new Label("Rs "+court.cost+"/hr");
                cost.setTextFill(Color.WHITE);
                Label type = new Label(court.type);
                type.setTextFill(Color.WHITE);

                StackPane s = new StackPane(r, name, cost, type);
                s.setAlignment(name, Pos.TOP_LEFT);
                s.setAlignment(cost, Pos.BOTTOM_LEFT);
                s.setAlignment(type, Pos.TOP_RIGHT);
                final int x=i;
                s.setOnMouseClicked(e -> newBooking(player, arena, courts.get(x)));
                i++;
                tile1.getChildren().add(s);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        if(!flag) {
            Label empty = new Label("This arena has no courts!");
            StackPane sp = new StackPane(empty);
            sp.setAlignment(empty,Pos.CENTER_LEFT);
            tile1.getChildren().add(sp);
        }
        ScrollPane spane = new ScrollPane(tile1);
        spane.setMinWidth(390);
        spane.setPrefWidth(390);
        spane.setMaxWidth(390);
        spane.setMinHeight(400);
        spane.setMaxHeight(400);
        spane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        StackPane stack = new StackPane(spane);
        stack.setMargin(spane, new Insets(20, 40, 20, 40));
        VBox vBox = new VBox(stack,backButton);
        vBox.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(vBox, 400, 500,Color.WHITE));
        stage.showAndWait();
    }

    private void newBooking(playerObject player, arenaObject arena, courtObject court){
        MessageBox.show("You selected Court "+court.cNum,"Court Selected",true);
        selectDate();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Select Slot");
        stage.setMinWidth(400);
        stage.setMinHeight(500);
        TilePane tile1 = new TilePane();
        tile1.setHgap(10);
        tile1.setVgap(10);
        tile1.setPrefColumns(1);
        tile1.setPrefTileWidth(350);
        tile1.setPadding(new Insets(10, 10, 10, 10));
        bookingObject booking = new bookingObject();
        booking.bdate = selectedDate;
        ArrayList<Integer> bookedSlots = new ArrayList<Integer>();
        ArrayList<Integer> selectedSlots = new ArrayList<Integer>();

        Button backButton = new Button("Back");
        backButton.setStyle(cssStrings.buttonStyle(50,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(50,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(50,120)));
        backButton.setOnAction(event -> stage.close());
        try(
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx");
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM booking WHERE cid='"+court.courtID+"' and bdate=? order by start_slot;");
        ){
            stmt.setDate(1,java.sql.Date.valueOf(booking.bdate));
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                int startSlot = rs.getInt("start_slot");
                int endSlot = rs.getInt("end_slot");
                for (int i = startSlot; i <= endSlot; i++)
                    bookedSlots.add(i);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        int noOfSlots = arena.closingTime.minus(arena.openingTime.getHour(), ChronoUnit.HOURS).getHour();
        boolean check[] = new boolean[noOfSlots];
        for(int i=0;i<noOfSlots;i++){
            Rectangle r = new Rectangle(350, 50);
            r.setFill(Color.web("#269e1e"));

            Label name = new Label("Slot number "+(i+1));
            name.setStyle("-fx-font-weight: bold ; -fx-font-size: 18;" );
            name.setTextFill(Color.WHITE);
            Label status = new Label("Status: Available");
            status.setTextFill(Color.WHITE);
            Label time = new Label(arena.openingTime.plus(i,ChronoUnit.HOURS)+" to "+arena.openingTime.plus(i+1,ChronoUnit.HOURS));
            time.setTextFill(Color.WHITE);

            StackPane s = new StackPane(r, name, status, time);
            s.setAlignment(name, Pos.TOP_LEFT);
            s.setAlignment(status, Pos.BOTTOM_LEFT);
            s.setAlignment(time, Pos.TOP_RIGHT);
            if(bookedSlots.contains(i+1)){
                r.setFill(Color.RED);
                status.setText("Status: Booked");
                s.setOnMouseClicked(e -> {
                    MessageBox.show("This is already booked!","Error",false);
                });
            }
            else{
                final int x=i;
                s.setOnMouseClicked(e -> {
                    if(check[x]==false){
                        status.setText("Status: Selected");
                        r.setFill(Color.GREEN);
                        selectedSlots.add(x+1);
                    }
                    else{
                        status.setText("Status: Available");
                        r.setFill(Color.web("#269e1e"));
                        selectedSlots.remove(new Integer(x+1));
                    }
                    check[x]=!check[x];
                });
            }
            tile1.getChildren().add(s);
        }
        Button bookButton = new Button("Book");
        bookButton.setStyle(cssStrings.buttonStyle(50,120));
        bookButton.setOnMouseEntered(event -> bookButton.setStyle(cssStrings.onButtonStyle(50,120)));
        bookButton.setOnMouseExited(event -> bookButton.setStyle(cssStrings.buttonStyle(50,120)));
        bookButton.setOnAction(event -> {
            if(selectedSlots.size()==0){
                MessageBox.show("Select at least one slot!","Select Slot",false);
                return;
            }
            Collections.sort(selectedSlots);
            if(!checkRange(selectedSlots)){
                MessageBox.show("Select slots in a continuous range","Select Correct Range",false);
                return;
            }
            selectHost();
            int maxPlayers=0;
            if(selectedInt==1){
                booking.hosting=true;
                selectNoPlayers();
                maxPlayers=selectedInt+1;
            }
            else {
                booking.hosting = false;
            }
            booking.cid=court.courtID;
            booking.email=player.playerEmail;
            booking.startSlot=selectedSlots.get(0);
            booking.endSlot=selectedSlots.get(selectedSlots.size()-1);
            try(
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx")
            ){
                PreparedStatement stmt=  conn.prepareStatement("INSERT INTO booking(cid,email,start_slot,end_slot,hosting,bdate) VALUES (?,?,?,?,?,?);");
                stmt.setInt(1,booking.cid);
                stmt.setString(2,booking.email);
                stmt.setInt(3,booking.startSlot);
                stmt.setInt(4,booking.endSlot);
                stmt.setBoolean(5,booking.hosting);
                stmt.setObject(6,booking.bdate);
                stmt.executeUpdate();

                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("select max(bid) from booking;");
                rs.next();
                booking.bid = rs.getInt("max(bid)");

                if(booking.hosting){
                    stmt=conn.prepareStatement("INSERT INTO hosting(bid,email,max_players) VALUES (?,?,?);");
                    stmt.setInt(1,booking.bid);
                    stmt.setString(2,booking.email);
                    stmt.setInt(3,maxPlayers);
                    stmt.executeUpdate();
                }
            }
            catch(SQLException ex) {
                ex.printStackTrace();
            };
            stage.close();
            secondaryStage.close();
            confirmBooking(player,arena,court,booking);
        });

        ScrollPane spane = new ScrollPane(tile1);
        spane.setMinWidth(390);
        spane.setPrefWidth(390);
        spane.setMaxWidth(390);
        spane.setMinHeight(400);
        spane.setMaxHeight(400);
        spane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        StackPane stack = new StackPane(spane);
        HBox hBox = new HBox(bookButton,backButton);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        stack.setMargin(spane, new Insets(10, 40, 10, 40));
        VBox vBox = new VBox(stack,hBox);
        vBox.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(vBox, 400, 500,Color.WHITE));
        stage.showAndWait();
    }

    private boolean checkRange(ArrayList<Integer> toCheck){
        int j=toCheck.get(0);
        j++;
        for(int i=1;i<toCheck.size();i++,j++){
            if(j!=toCheck.get(i))
                return false;
        }
        return true;
    }

    private void selectHost() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Select Hosting Options");
        stage.setMinWidth(350);
        stage.setMinHeight(200);
        Label lbl = new Label("Would you like to put up this booking for hosting?");
        lbl.setStyle("-fx-font-weight: bold");
        lbl.setFont(new Font("TimesRoman", 18));
        ChoiceBox hostChoiceBox = new ChoiceBox();
        hostChoiceBox.getItems().addAll("No","Yes");
        hostChoiceBox.getSelectionModel().select(0);
        Button btnOK = new Button();
        btnOK.setText("Select");
        btnOK.setStyle(cssStrings.buttonStyle(40,90));
        btnOK.setOnMouseEntered(event -> btnOK.setStyle(cssStrings.onButtonStyle(40,90)));
        btnOK.setOnMouseExited(event -> btnOK.setStyle(cssStrings.buttonStyle(40,90)));

        btnOK.setOnAction(e -> {
            selectedInt = hostChoiceBox.getSelectionModel().getSelectedIndex();
            stage.close();
        });
        VBox pane = new VBox(20);

        pane.getChildren().addAll(lbl, hostChoiceBox, btnOK);
        pane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void selectNoPlayers(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Select Hosting Options");
        stage.setMinWidth(350);
        stage.setMinHeight(200);
        Label lbl = new Label("How many more players do you need?");
        lbl.setStyle("-fx-font-weight: bold");
        lbl.setFont(new Font("TimesRoman", 18));
        TextField playerField = new TextField();
        Button btnOK = new Button();
        btnOK.setText("Select");
        btnOK.setStyle(cssStrings.buttonStyle(40,90));
        btnOK.setOnMouseEntered(event -> btnOK.setStyle(cssStrings.onButtonStyle(40,90)));
        btnOK.setOnMouseExited(event -> btnOK.setStyle(cssStrings.buttonStyle(40,90)));

        btnOK.setOnAction(e -> {
            String player = playerField.getText();
            if (!Pattern.compile("^[0-9]+$").matcher(player).find()) {
                MessageBox.show("Please Enter a Valid Number of Players", "Invalid Entry",false);
                return;
            }
            int players = Integer.parseInt(player);
            if(players<1 || players>22){
                MessageBox.show("You must select host a valid number of players", "Invalid Entry",false);
                return;
            }
            selectedInt=players;
            stage.close();
        });
        VBox pane = new VBox(20);

        pane.getChildren().addAll(lbl, playerField, btnOK);
        pane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void selectDate(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Select Sport");
        stage.setMinWidth(350);
        stage.setMinHeight(200);
        Label lbl = new Label("Select a date for your booking");
        lbl.setStyle("-fx-font-weight: bold");
        lbl.setFont(new Font("TimesRoman", 18));
        DatePicker bDate = new DatePicker();
        Button btnOK = new Button();
        btnOK.setText("Select");
        btnOK.setStyle(cssStrings.buttonStyle(40,90));
        btnOK.setOnMouseEntered(event -> btnOK.setStyle(cssStrings.onButtonStyle(40,90)));
        btnOK.setOnMouseExited(event -> btnOK.setStyle(cssStrings.buttonStyle(40,90)));

        btnOK.setOnAction(e -> {
            bDate.setValue(bDate.getConverter().fromString(bDate.getEditor().getText()));
            selectedDate = bDate.getValue();
            if (selectedDate == null) {
                MessageBox.show("Please Enter a Valid Date!", "Invalid Entry",false);
                return;
            }
            stage.close();
        });
        VBox pane = new VBox(20);

        pane.getChildren().addAll(lbl, bDate, btnOK);
        pane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void confirmBooking(playerObject player, arenaObject arena, courtObject court, bookingObject booking){
        Label bookingLabel = new Label("Details");
        bookingLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 50");
        bookingLabel.setTextFill(Color.web("#4dde0b", 0.8));
        Label bookingID = new Label("Booking ID ");
        Label bookingIDCon = new Label(""+booking.bid);
        Label sportName = new Label("Sport");
        Label sportNameCon = new Label(arena.arenaSport);
        Label arenaName = new Label("Arena Name");
        Label arenaNameCon = new Label(arena.arenaName);
        Label courtNum = new Label("Court Number");
        Label courtNumCon = new Label(""+court.cNum);
        Label date = new Label("Booking Date");
        Label dateCon = new Label(""+booking.bdate);
        Label time = new Label("Booking Time");
        Label timeCon = new Label(arena.openingTime.plus((booking.startSlot-1),ChronoUnit.HOURS)+" to "+arena.openingTime.plus((booking.endSlot),ChronoUnit.HOURS));
        Label cost = new Label("Cost");
        Label costCon = new Label(""+((booking.endSlot-booking.startSlot+1)*court.cost));
        Label owner = new Label("Owner Contact");
        Label ownerCon = new Label("+91 "+arena.ownerMobile);
        bookingID.setStyle("-fx-font-weight: bold;");
        bookingIDCon.setStyle("-fx-font-weight: bold;");
        sportName.setStyle("-fx-font-weight: bold;");
        sportNameCon.setStyle("-fx-font-weight: bold;");
        arenaName.setStyle("-fx-font-weight: bold;");
        arenaNameCon.setStyle("-fx-font-weight: bold;");
        courtNum.setStyle("-fx-font-weight: bold;");
        courtNumCon.setStyle("-fx-font-weight: bold;");
        date.setStyle("-fx-font-weight: bold;");
        dateCon.setStyle("-fx-font-weight: bold;");
        time.setStyle("-fx-font-weight: bold;");
        timeCon.setStyle("-fx-font-weight: bold;");
        cost.setStyle("-fx-font-weight: bold;");
        costCon.setStyle("-fx-font-weight: bold;");
        owner.setStyle("-fx-font-weight: bold;");
        ownerCon.setStyle("-fx-font-weight: bold;");
        Button backButton = new Button("OK");
        backButton.setStyle(cssStrings.buttonStyle(50,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(50,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(50,120)));
        backButton.setOnAction(event -> playerDashboard(player));

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(500, 500);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(bookingLabel,0,0,5,1);
        gridPane.add(bookingID, 1, 1);
        gridPane.add(bookingIDCon, 3, 1);
        gridPane.add(sportName, 1, 2);
        gridPane.add(sportNameCon, 3, 2);
        gridPane.add(arenaName, 1, 3);
        gridPane.add(arenaNameCon, 3, 3);
        gridPane.add(courtNum, 1, 4);
        gridPane.add(courtNumCon, 3, 4);
        gridPane.add(date, 1, 5);
        gridPane.add(dateCon, 3, 5);
        gridPane.add(time, 1, 6);
        gridPane.add(timeCon, 3, 6);
        gridPane.add(cost, 1, 7);
        gridPane.add(costCon, 3, 7);
        gridPane.add(owner, 1, 8);
        gridPane.add(ownerCon, 3, 8);
        gridPane.add(backButton, 1, 9,5,1);

        stage.setScene(new Scene(gridPane, 400, 500,Color.WHITE));
        stage.show();
    }

    private void viewBooking(playerObject player) {
        TilePane tile1 = new TilePane();
        tile1.setHgap(10);
        tile1.setVgap(10);
        tile1.setPrefColumns(1);
        tile1.setPrefTileWidth(350);
        tile1.setPadding(new Insets(10, 10, 10, 10));
        Button backButton = new Button("Back");
        backButton.setStyle(cssStrings.buttonStyle(50,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(50,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(50,120)));
        backButton.setOnAction(event -> playerDashboard(player));
        ArrayList<arenaObject> arenas = new ArrayList<arenaObject>();
        ArrayList<courtObject> courts = new ArrayList<courtObject>();
        ArrayList<bookingObject> bookings = new ArrayList<bookingObject>();
        arenaObject arena = new arenaObject();
        courtObject court = new courtObject();
        bookingObject booking = new bookingObject();
        int i=0;
        boolean flag=false;
        try(
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM ((booking NATURAL JOIN court) NATURAL JOIN arena) WHERE email='"+player.playerEmail+"' order by bdate, start_slot;");
        ){
            while(rs.next()){
                flag=true;
                booking.bid = rs.getInt("bid");
                court.cNum = rs.getInt("cnum");
                arena.arenaName=rs.getString("aname");
                arena.arenaSport=rs.getString("sname");
                arena.arenaRegion=rs.getString("region");
                arena.openingTime=LocalTime.parse(rs.getString("opening"));
                arena.ownerMobile=rs.getString("mobile");
                court.cost=rs.getFloat("cost");
                booking.bdate=LocalDate.parse(rs.getString("bdate"));
                booking.startSlot=rs.getInt("start_slot");
                booking.endSlot=rs.getInt("end_slot");
                arenas.add(new arenaObject(arena));
                courts.add(new courtObject(court));
                bookings.add(new bookingObject(booking));

                Rectangle r = new Rectangle(350, 50);
                r.setFill(Color.web("#269e1e"));

                Label name = new Label(arena.arenaName);
                name.setStyle("-fx-font-weight: bold ; -fx-font-size: 18;" );
                name.setTextFill(Color.WHITE);
                Label time = new Label(arena.openingTime.plus((booking.startSlot-1),ChronoUnit.HOURS)+" to "+arena.openingTime.plus((booking.endSlot),ChronoUnit.HOURS));
                time.setTextFill(Color.WHITE);
                Label date = new Label(""+booking.bdate);
                date.setTextFill(Color.WHITE);
                Label courtLabel = new Label("Court Number: "+court.cNum);
                courtLabel.setTextFill(Color.WHITE);

                StackPane s = new StackPane(r, name, time, date, courtLabel);
                s.setAlignment(name, Pos.TOP_LEFT);
                s.setAlignment(time, Pos.BOTTOM_LEFT);
                s.setAlignment(date, Pos.TOP_RIGHT);
                s.setAlignment(courtLabel, Pos.BOTTOM_RIGHT);
                final int x=i;
                s.setOnMouseClicked(e -> confirmBooking(player, arenas.get(x),courts.get(x),bookings.get(x)));
                i++;
                tile1.getChildren().add(s);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        if(!flag) {
            Label empty = new Label("Make a booking first!");
            StackPane sp = new StackPane(empty);
            sp.setAlignment(empty,Pos.CENTER_LEFT);
            tile1.getChildren().add(sp);
        }
        ScrollPane spane = new ScrollPane(tile1);
        spane.setMinWidth(390);
        spane.setPrefWidth(390);
        spane.setMaxWidth(390);
        spane.setMinHeight(400);
        spane.setMaxHeight(400);
        spane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        StackPane stack = new StackPane(spane);
        stack.setMargin(spane, new Insets(20, 40, 20, 40));
        VBox vBox = new VBox(stack,backButton);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox,400,500);
        stage.setScene(scene);
        stage.setTitle("Your Bookings");
        stage.show();
    }

    private void newHosting(playerObject player){
        selectSport();
        selectDate();
        arenaObject arena = new arenaObject();
        courtObject court = new courtObject();
        bookingObject booking = new bookingObject();
        hostingObject hosting = new hostingObject();
        ArrayList<arenaObject> arenas = new ArrayList<arenaObject>();
        ArrayList<courtObject> courts = new ArrayList<courtObject>();
        ArrayList<bookingObject> bookings = new ArrayList<bookingObject>();
        ArrayList<hostingObject> hostings = new ArrayList<hostingObject>();
        arena.arenaSport=selected;
        booking.bdate = selectedDate;
        Label city = new Label("Hostings in your city");
        TilePane tile1 = new TilePane();
        tile1.setHgap(10);
        tile1.setVgap(10);
        tile1.setPrefColumns(1);
        tile1.setPrefTileWidth(350);
        tile1.setPadding(new Insets(10, 10, 10, 10));
        Button backButton = new Button("Back");
        backButton.setStyle(cssStrings.buttonStyle(50,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(50,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(50,120)));
        backButton.setOnAction(event -> playerDashboard(player));
        int i=0;
        boolean flag=false;
        try(
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select count(*), bid, max_players from hosting group by bid, max_players having count(*)<max_players;");
        ){
            StackPane sp = new StackPane(city);
            sp.setAlignment(city,Pos.CENTER_LEFT);
            tile1.getChildren().add(sp);
            while(rs.next()){
                hosting.bid = booking.bid = rs.getInt("hosting.bid");
                hosting.currentNo = rs.getInt("count(*)");
                hosting.maxPlayers = rs.getInt("max_players");
                Statement stmt2 = conn.createStatement();
                ResultSet rs2 = stmt2.executeQuery("select * from booking natural join court natural join arena where bid="+hosting.bid+";");
                rs2.next();
                arena.arenaCity=rs2.getString("city");
                String sportCheck=rs2.getString("sname");
                LocalDate dateCheck=LocalDate.parse(rs2.getString("bdate"));
                if(!arena.arenaCity.equals(player.playerCity) || !arena.arenaSport.equals(sportCheck) || !booking.bdate.equals(dateCheck))
                    continue;
                arena.arenaName=rs2.getString("aname");
                arena.openingTime= LocalTime.parse(rs2.getString("opening"));
                arena.arenaRegion=rs2.getString("region");
                arena.ownerMobile=rs2.getString("mobile");
                court.cNum = rs2.getInt("cnum");
                court.cost=rs2.getFloat("cost");
                booking.startSlot=rs2.getInt("start_slot");
                booking.endSlot=rs2.getInt("end_slot");
                booking.email=rs2.getString("email");
                arenas.add(new arenaObject(arena));
                courts.add(new courtObject(court));
                bookings.add(new bookingObject(booking));
                hostings.add(new hostingObject(hosting));

                Rectangle r = new Rectangle(350, 50);
                r.setFill(Color.web("#269e1e"));

                Label name = new Label(arena.arenaName);
                name.setStyle("-fx-font-weight: bold ; -fx-font-size: 18;" );
                name.setTextFill(Color.WHITE);
                Label time = new Label(arena.openingTime.plus((booking.startSlot-1),ChronoUnit.HOURS)+" to "+arena.openingTime.plus((booking.endSlot),ChronoUnit.HOURS));
                time.setTextFill(Color.WHITE);
                Label sport = new Label(arena.arenaCity+" "+arena.arenaRegion);
                sport.setTextFill(Color.WHITE);
                Label slot = new Label("Slots filled "+hosting.currentNo+"/"+hosting.maxPlayers);
                slot.setTextFill(Color.WHITE);

                StackPane s = new StackPane(r, name, time, sport, slot);
                s.setAlignment(name, Pos.TOP_LEFT);
                s.setAlignment(time, Pos.BOTTOM_LEFT);
                s.setAlignment(sport, Pos.TOP_RIGHT);
                s.setAlignment(slot, Pos.BOTTOM_RIGHT);
                final int x=i;
                s.setOnMouseClicked(e -> hostingDetails(player, arenas.get(x), courts.get(x), bookings.get(x), hostings.get(x)));
                i++;
                tile1.getChildren().add(s);
                flag=true;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        if(!flag) {
            Label empty = new Label("None to display! You can host a game!");
            StackPane sp = new StackPane(empty);
            sp.setAlignment(empty,Pos.CENTER_LEFT);
            tile1.getChildren().add(sp);
        }
        ScrollPane spane = new ScrollPane(tile1);
        spane.setMinWidth(390);
        spane.setPrefWidth(390);
        spane.setMaxWidth(390);
        spane.setMinHeight(400);
        spane.setMaxHeight(400);
        spane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        StackPane stack = new StackPane(spane);
        stack.setMargin(spane, new Insets(20, 40, 20, 40));
        VBox vBox = new VBox(stack,backButton);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox,400,500);
        stage.setScene(scene);
        stage.setTitle("Select Hosting");
        stage.show();
    }

    private void hostingDetails(playerObject player, arenaObject arena, courtObject court, bookingObject booking, hostingObject hosting) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Join Hosting");
        stage.setMinWidth(400);
        stage.setMinHeight(500);
        TilePane tile1 = new TilePane();
        tile1.setHgap(10);
        tile1.setVgap(10);
        tile1.setPrefColumns(1);
        tile1.setPrefTileWidth(350);
        tile1.setPadding(new Insets(10, 10, 10, 10));

        ArrayList<String> playerEmails = new ArrayList<String>();

        Button backButton = new Button("Back");
        backButton.setStyle(cssStrings.buttonStyle(50,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(50,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(50,120)));
        backButton.setOnAction(event -> stage.close());
        int i=0;
        try(
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM hosting natural join player WHERE bid='"+hosting.bid+"';");
        ){
            while(rs.next()){
                String pname = rs.getString("pname");
                String pemail = rs.getString("email");
                playerEmails.add(pemail);
                Rectangle r = new Rectangle(350, 50);
                r.setFill(Color.web("#269e1e"));

                Label name = new Label(pname);
                name.setStyle("-fx-font-weight: bold ; -fx-font-size: 18;" );
                name.setTextFill(Color.WHITE);
                Label num = new Label("Player "+(i+1));
                num.setTextFill(Color.WHITE);
                Label type = new Label();
                if(!pemail.equals(booking.email))
                    type.setText("Player");
                else
                    type.setText("Host");
                type.setTextFill(Color.WHITE);
                Label email = new Label(pemail);
                email.setTextFill(Color.WHITE);

                StackPane s = new StackPane(r, name, num, type, email);
                s.setAlignment(name, Pos.TOP_LEFT);
                s.setAlignment(num, Pos.BOTTOM_LEFT);
                s.setAlignment(type, Pos.TOP_RIGHT);
                s.setAlignment(email, Pos.BOTTOM_RIGHT);

                final int x=i;
                i++;
                tile1.getChildren().add(s);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        Label empty = new Label("There are "+(hosting.maxPlayers-hosting.currentNo)+" more player(s) needed");
        StackPane sp = new StackPane(empty);
        sp.setAlignment(empty,Pos.CENTER_LEFT);
        tile1.getChildren().add(sp);
        Button bookButton = new Button("Join");
        bookButton.setStyle(cssStrings.buttonStyle(50,120));
        bookButton.setOnMouseEntered(event -> bookButton.setStyle(cssStrings.onButtonStyle(50,120)));
        bookButton.setOnMouseExited(event -> bookButton.setStyle(cssStrings.buttonStyle(50,120)));
        bookButton.setOnAction(event -> {
            if(playerEmails.contains(player.playerEmail)){
                MessageBox.show("You are already a part of this game!","Invalid entry",false);
                return;
            }
            try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx");)
            {
                PreparedStatement stmt=conn.prepareStatement("INSERT INTO hosting(bid,email,max_players) VALUES (?,?,?);");
                stmt.setInt(1,booking.bid);
                stmt.setString(2,player.playerEmail);
                stmt.setInt(3,hosting.maxPlayers);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            MessageBox.show("You have successfully joined this game!","Successful",true);
            MessageBox.show("You can now view details of this booking","Details",true);
            stage.close();
            confirmBooking(player,arena,court,booking);
        });

        ScrollPane spane = new ScrollPane(tile1);
        spane.setMinWidth(390);
        spane.setPrefWidth(390);
        spane.setMaxWidth(390);
        spane.setMinHeight(400);
        spane.setMaxHeight(400);
        spane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        StackPane stack = new StackPane(spane);
        stack.setMargin(spane, new Insets(20, 40, 20, 40));
        HBox hBox = new HBox(bookButton,backButton);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(stack,hBox);
        vBox.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(vBox, 400, 500,Color.WHITE));
        stage.showAndWait();
    }

    private void viewHosting(playerObject player){
        arenaObject arena = new arenaObject();
        courtObject court = new courtObject();
        bookingObject booking = new bookingObject();
        hostingObject hosting = new hostingObject();
        ArrayList<arenaObject> arenas = new ArrayList<arenaObject>();
        ArrayList<courtObject> courts = new ArrayList<courtObject>();
        ArrayList<bookingObject> bookings = new ArrayList<bookingObject>();
        ArrayList<hostingObject> hostings = new ArrayList<hostingObject>();
        TilePane tile1 = new TilePane();
        tile1.setHgap(10);
        tile1.setVgap(10);
        tile1.setPrefColumns(1);
        tile1.setPrefTileWidth(350);
        tile1.setPadding(new Insets(10, 10, 10, 10));
        Button backButton = new Button("Back");
        backButton.setStyle(cssStrings.buttonStyle(50,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(50,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(50,120)));
        backButton.setOnAction(event -> playerDashboard(player));
        int i=0;
        boolean flag=false;
        try(
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select bid, max_players from hosting where email='"+player.playerEmail+"';");
        ){
            while(rs.next()){
                hosting.bid = booking.bid = rs.getInt("bid");
                hosting.maxPlayers = rs.getInt("max_players");
                Statement stmt3 = conn.createStatement();
                ResultSet rs3 = stmt3.executeQuery("select count(*) from hosting group by bid having bid="+hosting.bid+";");
                rs3.next();
                hosting.currentNo = rs3.getInt("count(*)");
                Statement stmt2 = conn.createStatement();
                ResultSet rs2 = stmt2.executeQuery("select * from booking natural join court natural join arena where bid="+hosting.bid+";");
                rs2.next();
                arena.arenaCity=rs2.getString("city");
                arena.arenaSport=rs2.getString("sname");
                booking.bdate=LocalDate.parse(rs2.getString("bdate"));
                arena.arenaName=rs2.getString("aname");
                arena.openingTime= LocalTime.parse(rs2.getString("opening"));
                arena.arenaRegion=rs2.getString("region");
                arena.ownerMobile=rs2.getString("mobile");
                court.cNum = rs2.getInt("cnum");
                court.cost=rs2.getFloat("cost");
                booking.startSlot=rs2.getInt("start_slot");
                booking.endSlot=rs2.getInt("end_slot");
                booking.email=rs2.getString("email");
                arenas.add(new arenaObject(arena));
                courts.add(new courtObject(court));
                bookings.add(new bookingObject(booking));
                hostings.add(new hostingObject(hosting));

                Rectangle r = new Rectangle(350, 50);
                r.setFill(Color.web("#269e1e"));

                Label name = new Label(arena.arenaName);
                name.setStyle("-fx-font-weight: bold ; -fx-font-size: 18;" );
                name.setTextFill(Color.WHITE);
                Label time = new Label(arena.openingTime.plus((booking.startSlot-1),ChronoUnit.HOURS)+" to "+arena.openingTime.plus((booking.endSlot),ChronoUnit.HOURS));
                time.setTextFill(Color.WHITE);
                Label date = new Label(""+booking.bdate);
                date.setTextFill(Color.WHITE);
                Label courtLabel = new Label("Court Number: "+court.cNum);
                courtLabel.setTextFill(Color.WHITE);

                StackPane s = new StackPane(r, name, time, date, courtLabel);
                s.setAlignment(name, Pos.TOP_LEFT);
                s.setAlignment(time, Pos.BOTTOM_LEFT);
                s.setAlignment(date, Pos.TOP_RIGHT);
                s.setAlignment(courtLabel, Pos.BOTTOM_RIGHT);
                final int x=i;
                s.setOnMouseClicked(e -> showHostingDetails(player, arenas.get(x),courts.get(x),bookings.get(x),hostings.get(x)));
                i++;
                tile1.getChildren().add(s);
                flag=true;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        if(!flag) {
            Label empty = new Label("You haven't joined or hosted a game yet");
            StackPane sp = new StackPane(empty);
            sp.setAlignment(empty,Pos.CENTER_LEFT);
            tile1.getChildren().add(sp);
        }
        ScrollPane spane = new ScrollPane(tile1);
        spane.setMinWidth(390);
        spane.setPrefWidth(390);
        spane.setMaxWidth(390);
        spane.setMinHeight(400);
        spane.setMaxHeight(400);
        spane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        StackPane stack = new StackPane(spane);
        stack.setMargin(spane, new Insets(20, 40, 20, 40));
        VBox vBox = new VBox(stack,backButton);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox,400,500);
        stage.setScene(scene);
        stage.setTitle("Hostings you're part of");
        stage.show();
    }

    private  void showHostingDetails(playerObject player, arenaObject arena, courtObject court, bookingObject booking, hostingObject hosting){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Hosting players");
        stage.setMinWidth(400);
        stage.setMinHeight(500);
        TilePane tile1 = new TilePane();
        tile1.setHgap(10);
        tile1.setVgap(10);
        tile1.setPrefColumns(1);
        tile1.setPrefTileWidth(350);
        tile1.setPadding(new Insets(10, 10, 10, 10));

        ArrayList<String> playerEmails = new ArrayList<String>();

        Button backButton = new Button("Back");
        backButton.setStyle(cssStrings.buttonStyle(50,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(50,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(50,120)));
        backButton.setOnAction(event -> stage.close());
        int i=0;
        try(
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM hosting natural join player WHERE bid='"+hosting.bid+"';");
        ){
            while(rs.next()){
                String pname = rs.getString("pname");
                String pemail = rs.getString("email");
                playerEmails.add(pemail);
                Rectangle r = new Rectangle(350, 50);
                r.setFill(Color.web("#269e1e"));

                Label name = new Label(pname);
                name.setStyle("-fx-font-weight: bold ; -fx-font-size: 18;" );
                name.setTextFill(Color.WHITE);
                Label num = new Label("Player "+(i+1));
                num.setTextFill(Color.WHITE);
                Label type = new Label();
                if(!pemail.equals(booking.email))
                    type.setText("Player");
                else
                    type.setText("Host");
                type.setTextFill(Color.WHITE);
                Label email = new Label(pemail);
                email.setTextFill(Color.WHITE);

                StackPane s = new StackPane(r, name, num, type, email);
                s.setAlignment(name, Pos.TOP_LEFT);
                s.setAlignment(num, Pos.BOTTOM_LEFT);
                s.setAlignment(type, Pos.TOP_RIGHT);
                s.setAlignment(email, Pos.BOTTOM_RIGHT);

                final int x=i;
                i++;
                tile1.getChildren().add(s);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        Label empty = new Label("There are "+(hosting.maxPlayers-hosting.currentNo)+" more player(s) needed");
        StackPane sp = new StackPane(empty);
        sp.setAlignment(empty,Pos.CENTER_LEFT);
        tile1.getChildren().add(sp);
        Button bookButton = new Button("Details");
        bookButton.setStyle(cssStrings.buttonStyle(50,120));
        bookButton.setOnMouseEntered(event -> bookButton.setStyle(cssStrings.onButtonStyle(50,120)));
        bookButton.setOnMouseExited(event -> bookButton.setStyle(cssStrings.buttonStyle(50,120)));
        bookButton.setOnAction(event -> {
            stage.close();
            confirmBooking(player,arena,court,booking);
        });

        ScrollPane spane = new ScrollPane(tile1);
        spane.setMinWidth(390);
        spane.setPrefWidth(390);
        spane.setMaxWidth(390);
        spane.setMinHeight(400);
        spane.setMaxHeight(400);
        spane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        StackPane stack = new StackPane(spane);
        stack.setMargin(spane, new Insets(20, 40, 20, 40));
        HBox hBox = new HBox(bookButton,backButton);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(stack,hBox);
        vBox.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(vBox, 400, 500,Color.WHITE));
        stage.showAndWait();
    }

    private void ownerFirstPage() {
        stage.setTitle("Select Login/Sign-Up");
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);

        Label ownerLabel = new Label("Owner");

        ownerLabel.setTextFill(Color.web("#4dde0b", 0.8));
        ownerLabel.setStyle("-fx-font-weight: bold");
        ownerLabel.setFont(new Font("TimesRoman", 50));

        Button logInButton = new Button("Login");
        logInButton.setStyle(cssStrings.buttonStyle(60,120));
        logInButton.setOnAction(event -> ownerLoginPage());
        logInButton.setOnMouseEntered(event -> logInButton.setStyle(cssStrings.onButtonStyle(60,120)));
        logInButton.setOnMouseExited(event -> logInButton.setStyle(cssStrings.buttonStyle(60,120)));

        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(event -> ownerSignUp());
        signUpButton.setStyle(cssStrings.buttonStyle(60,120));
        signUpButton.setOnMouseEntered(event -> signUpButton.setStyle(cssStrings.onButtonStyle(60,120)));
        signUpButton.setOnMouseExited(event -> signUpButton.setStyle(cssStrings.buttonStyle(60,120)));

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> mainPage());
        backButton.setStyle(cssStrings.buttonStyle(60,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(60,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(60,120)));

        vbox.getChildren().addAll(ownerLabel,logInButton, signUpButton, backButton);

        stage.setScene(new Scene(vbox, 400, 500,Color.WHITE));
        stage.show();
    }

    private void ownerLoginPage() {
        stage.setTitle("Login");
        Label loginLabel = new Label("Owner-Login");
        loginLabel.setTextFill(Color.web("#4dde0b", 0.8));
        loginLabel.setStyle("-fx-font-weight: bold");
        loginLabel.setFont(new Font("TimesRoman", 50));

        Label mobileText = new Label("Mobile (+91) ");
        mobileText.setStyle("-fx-font-weight: bold");
        Label passwordText = new Label("Password");
        passwordText.setStyle("-fx-font-weight: bold");

        TextField mobileField = new TextField();
        PasswordField passwordField = new PasswordField();

        Button submitButton = new Button("Submit");
        submitButton.setStyle(cssStrings.buttonStyle(60,120));
        submitButton.setOnMouseEntered(event -> submitButton.setStyle(cssStrings.onButtonStyle(60,120)));
        submitButton.setOnMouseExited(event ->submitButton.setStyle(cssStrings.buttonStyle(60,120)));
        submitButton.setOnAction(event -> {
            String password = passwordField.getText();
            String mobile = mobileField.getText().toLowerCase();
            if(password.length()==0 || mobile.length()==0){
                MessageBox.show("Enter a valid mobile number and password!", "Invalid Entry",false);
                return;
            }
            ownerObject owner = new ownerObject();
            boolean flag=false;
            try(
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM owner WHERE mobile='"+mobile+"';")
            ){
                while(rs.next()){
                    flag=true;
                    owner.ownerName=rs.getString("oname");
                    owner.ownerMobile=rs.getString("mobile");
                    owner.gender=rs.getString("gender");
                    owner.ownerPassword=rs.getString("password");
                    owner.ownerDOB=rs.getObject("dob",LocalDate.class);
                }

            }catch (SQLException e) {
                e.printStackTrace();
            }
            if(!flag) {
                MessageBox.show("Mobile number does not exist! Sign up!", "Invalid Entry",false);
                return;
            }
            if(!password.equals(owner.ownerPassword)) {
                MessageBox.show("Incorrect password!", "Invalid Entry",false);
                return;
            }
            MessageBox.show("You have successfully logged in!","Approved",true);
            ownerDashboard(owner);
        });

        Button backButton = new Button("Back");
        backButton.setStyle(cssStrings.buttonStyle(60,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(60,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(60,120)));
        backButton.setOnAction(event -> ownerFirstPage());

        GridPane gridPane = new GridPane();

        gridPane.setMinSize(500, 500);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(loginLabel, 0, 0, 5, 1);
        gridPane.add(mobileText, 1, 4);
        gridPane.add(mobileField, 3, 4);
        gridPane.add(passwordText, 1, 5);
        gridPane.add(passwordField, 3, 5);
        gridPane.add(submitButton, 1, 6);
        gridPane.add(backButton, 3, 6);
        stage.setScene(new Scene(gridPane, 400, 500,Color.WHITE));
        stage.show();
    }

    private void ownerSignUp() {
        stage.setTitle("Sign Up");

        Label signUpLabel = new Label("Owner-Sign-Up");
        signUpLabel.setTextFill(Color.web("#4dde0b", 0.8));
        signUpLabel.setStyle("-fx-font-weight: bold");
        signUpLabel.setFont(new Font("TimesRoman", 50));

        Label nameText = new Label("Name");
        nameText.setStyle("-fx-font-weight: bold ;" );
        Label genderText = new Label("Gender");
        genderText.setStyle("-fx-font-weight: bold ;" );
        Label DOBText = new Label("Date of Birth");
        DOBText.setStyle("-fx-font-weight: bold ;" );
        Label mobileText = new Label("Mobile (+91) ");
        mobileText.setStyle("-fx-font-weight: bold ;" );
        Label passwordText = new Label("Password");
        passwordText.setStyle("-fx-font-weight: bold ;" );
        Label rePasswordText = new Label("Confirm Password");
        rePasswordText.setStyle("-fx-font-weight: bold ;" );

        TextField nameField = new TextField();
        TextField mobileField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField rePasswordField = new PasswordField();

        ToggleGroup groupGender = new ToggleGroup();
        RadioButton maleRadio = new RadioButton("Male");
        maleRadio.setToggleGroup(groupGender);
        RadioButton femaleRadio = new RadioButton("Female");
        femaleRadio.setToggleGroup(groupGender);
        maleRadio.setSelected(true);
        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(maleRadio,femaleRadio);

        DatePicker DOBDate = new DatePicker();

        Button submitButton = new Button("Sign Up");
        submitButton.setStyle(cssStrings.buttonStyle(60,120));
        submitButton.setOnMouseEntered(event -> submitButton.setStyle(cssStrings.onButtonStyle(60,120)));
        submitButton.setOnMouseExited(event -> submitButton.setStyle(cssStrings.buttonStyle(60,120)));

        submitButton.setOnAction(event -> {
            ownerObject owner = new ownerObject();

            owner.ownerName = nameField.getText();
            if (owner.ownerName.length() == 0) {
                MessageBox.show("Please Enter Your Name!", "Invalid Entry",false);
                return;
            }

            owner.gender = ((RadioButton) groupGender.getSelectedToggle()).getText();

            DOBDate.setValue(DOBDate.getConverter().fromString(DOBDate.getEditor().getText()));

            owner.ownerDOB = DOBDate.getValue();
            if (owner.ownerDOB == null) {
                MessageBox.show("Please Enter a Valid Date!", "Invalid Entry",false);
                return;
            }

            owner.ownerMobile = mobileField.getText();
            if (!Pattern.compile("^[0-9]{10}$").matcher(owner.ownerMobile).find()) {
                MessageBox.show("Please Enter a Valid 10 digit pan-India Mobile Number!", "Invalid Entry",false);
                return;
            }

            boolean flag=false;
            try(
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM owner WHERE mobile='"+owner.ownerMobile+"';")
            ){
                while(rs.next()){
                    flag=true;
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
            if(flag) {
                MessageBox.show("This mobile is already associated with another account; Please login", "Invalid Entry",false);
                return;
            }

            if(!passwordField.getText().equals(rePasswordField.getText())){
                MessageBox.show("Passwords do not match. Please recheck", "Invalid Entry",false);
                return;
            }
            owner.ownerPassword = passwordField.getText();
            if(owner.ownerPassword.length()<6) {
                MessageBox.show("Password is too short!", "Invalid Password",false);
                return;
            }
            if (!Pattern.compile("^(?=.*[0-9])").matcher(owner.ownerPassword).find()) {
                MessageBox.show("Password must contain a number!", "Invalid Password",false);
                return;
            }
            if (!Pattern.compile("^(?=.*[a-z])").matcher(owner.ownerPassword).find()) {
                MessageBox.show("Password must contain a lower-case alphabet!", "Invalid Password",false);
                return;
            }
            if (!Pattern.compile("^(?=.*[A-Z])").matcher(owner.ownerPassword).find()) {
                MessageBox.show("Password must contain an upper-case alphabet!", "Invalid Password",false);
                return;
            }
            if (!Pattern.compile("^(?=.*[-@#$%^&+=,!_])").matcher(owner.ownerPassword).find()) {
                MessageBox.show("Password must contain a special character!", "Invalid Password",false);
                return;
            }
            try(
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx")
            ){
                PreparedStatement stmt=  conn.prepareStatement("INSERT INTO owner VALUES (?,?,?,?,?);");
                stmt.setString(1,owner.ownerName);
                stmt.setString(2,owner.ownerMobile);
                stmt.setString(3,owner.ownerPassword);
                stmt.setObject(4,owner.ownerDOB);
                stmt.setString(5,owner.gender);
                stmt.executeUpdate();
            }
            catch(SQLException ex) {
                ex.printStackTrace();
            }
            MessageBox.show("You have successfully signed up!","Approved",true);

            ownerDashboard(owner);
        });
        Button backButton = new Button("Back");
        backButton.setStyle(cssStrings.buttonStyle(60,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(60,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(60,120)));

        backButton.setOnAction(event -> ownerFirstPage());

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(500, 500);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(signUpLabel,0,0,2,1);
        gridPane.add(nameText, 0, 1);
        gridPane.add(nameField, 1, 1);
        gridPane.add(genderText, 0, 2);
        gridPane.add(hBox, 1, 2);
        gridPane.add(DOBText, 0, 3);
        gridPane.add(DOBDate, 1, 3);
        gridPane.add(mobileText, 0, 4);
        gridPane.add(mobileField, 1, 4);
        gridPane.add(passwordText, 0, 5);
        gridPane.add(passwordField, 1, 5);
        gridPane.add(rePasswordText, 0, 6);
        gridPane.add(rePasswordField, 1, 6);
        gridPane.add(submitButton, 0, 7);
        gridPane.add(backButton, 1, 7);

        stage.setScene(new Scene(gridPane, 400, 500,Color.WHITE));
        stage.show();
    }

    private void ownerDashboard(ownerObject owner) {
        stage.setTitle("Welcome "+owner.ownerName);
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);

        Label dashboardLabel = new Label("Owner-Dashboard");
        dashboardLabel.setTextFill(Color.web("#4dde0b", 0.8));
        dashboardLabel.setStyle("-fx-font-weight: bold");
        dashboardLabel.setFont(new Font("TimesRoman", 40));

        Button addArenaButton = new Button("Add Arena");
        addArenaButton.setStyle(cssStrings.buttonStyle(60,160));
        addArenaButton.setOnMouseEntered(event -> addArenaButton.setStyle(cssStrings.onButtonStyle(60,160)));
        addArenaButton.setOnMouseExited(event -> addArenaButton.setStyle(cssStrings.buttonStyle(60,160)));
        addArenaButton.setOnAction(event -> addArena(owner));

        Button viewArenaButton = new Button("View Your Arenas");
        viewArenaButton.setStyle(cssStrings.buttonStyle(60,200));
        viewArenaButton.setOnMouseEntered(event -> viewArenaButton.setStyle(cssStrings.onButtonStyle(60,200)));
        viewArenaButton.setOnMouseExited(event -> viewArenaButton.setStyle(cssStrings.buttonStyle(60,200)));
        viewArenaButton.setOnAction(event -> viewArena(owner));

        Button backButton = new Button("Sign Out");
        backButton.setStyle(cssStrings.buttonStyle(60,160));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(60,160)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(60,160)));

        backButton.setOnAction(event -> mainPage());

        vbox.getChildren().addAll(dashboardLabel,addArenaButton, viewArenaButton, backButton);

        stage.setScene(new Scene(vbox, 400, 500,Color.WHITE));
        stage.show();
    }

    private void addArena(ownerObject owner){
        stage.setTitle("New Arena");
        Label arenaLabel = new Label("Arena Details");
        arenaLabel.setTextFill(Color.web("#4dde0b", 0.8));
        arenaLabel.setStyle("-fx-font-weight: bold");
        arenaLabel.setFont(new Font("TimesRoman", 50));

        Label nameText = new Label("Arena Name");
        nameText.setStyle("-fx-font-weight: bold");
        Label sportText = new Label("Select Sport ");
        sportText.setStyle("-fx-font-weight: bold");
        Label openText = new Label("Opening hour ");
        openText.setStyle("-fx-font-weight: bold");
        Label closeText = new Label("Closing hour ");
        closeText.setStyle("-fx-font-weight: bold");
        Label cityText = new Label("City");
        cityText.setStyle("-fx-font-weight: bold ;" );
        Label regionText = new Label("Select City Region");
        regionText.setStyle("-fx-font-weight: bold ;" );
        Label courtText = new Label("Number of Courts");
        courtText.setStyle("-fx-font-weight: bold ;" );

        TextField nameField = new TextField();
        TextField openField = new TextField();
        TextField closeField = new TextField();
        TextField courtField = new TextField();
        ChoiceBox sportChoiceBox = new ChoiceBox(FXCollections.observableArrayList(arenaObject.sports));
        sportChoiceBox.getSelectionModel().select(0);
        ChoiceBox cityChoiceBox = new ChoiceBox();
        cityChoiceBox.getItems().addAll("Bangalore", "Chennai", "Delhi", "Mumbai");
        cityChoiceBox.getSelectionModel().select(0);
        ChoiceBox regionChoiceBox = new ChoiceBox();
        regionChoiceBox.getItems().addAll("North", "South", "East", "West", "Central");
        regionChoiceBox.getSelectionModel().select(0);

        Button createButton = new Button("Add");
        createButton.setStyle(cssStrings.buttonStyle(60,120));
        createButton.setOnMouseEntered(event -> createButton.setStyle(cssStrings.onButtonStyle(60,120)));
        createButton.setOnMouseExited(event ->createButton.setStyle(cssStrings.buttonStyle(60,120)));
        createButton.setOnAction(event -> {
            arenaObject arena = new arenaObject();
            arena.arenaName = nameField.getText();
            if (arena.arenaName.length() == 0) {
                MessageBox.show("Please Enter The Arena Name!", "Invalid Entry",false);
                return;
            }

            String time = openField.getText();
            if (!Pattern.compile("^[0-9]+$").matcher(time).find()) {
                MessageBox.show("Please Enter a Valid 24hr format time (only hour)!", "Invalid Entry",false);
                return;
            }
            if(Integer.parseInt(time)>23||Integer.parseInt(time)<0){
                MessageBox.show("Please Enter a Valid 24hr format time (only hour)!", "Invalid Entry",false);
                return;
            }
            arena.openingTime = LocalTime.of(Integer.parseInt(time),0);
            time = closeField.getText();
            if (!Pattern.compile("^[0-9]+$").matcher(time).find()) {
                MessageBox.show("Please Enter a Valid 24hr format time (only hour)!", "Invalid Entry",false);
                return;
            }
            if(Integer.parseInt(time)>23||Integer.parseInt(time)<0){
                MessageBox.show("Please Enter a Valid 24hr format time (only hour)!", "Invalid Entry",false);
                return;
            }
            arena.closingTime = LocalTime.of(Integer.parseInt(time),0);
            if(arena.openingTime.compareTo(arena.closingTime)>=0){
                MessageBox.show("Make sure opening time is before closing time!", "Invalid Entry",false);
                return;
            }

            String court = courtField.getText();
            if (!Pattern.compile("^[0-9]+$").matcher(court).find()) {
                MessageBox.show("Please Enter a Valid Number of Courts", "Invalid Entry",false);
                return;
            }
            int courts = Integer.parseInt(court);
            if(courts<1){
                MessageBox.show("Arena must have at least one court", "Invalid Entry",false);
                return;
            }
            arena.arenaCity = (String) cityChoiceBox.getValue();
            arena.arenaRegion = (String) regionChoiceBox.getValue();
            arena.arenaSport = (String) sportChoiceBox.getValue();

            try(
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx")
            ){
                PreparedStatement stmt=  conn.prepareStatement("INSERT INTO arena(opening,closing,aname,city,region,sname,mobile) VALUES (?,?,?,?,?,?,?);");
                stmt.setObject(1,arena.openingTime);
                stmt.setObject(2,arena.closingTime);
                stmt.setString(3,arena.arenaName);
                stmt.setString(4,arena.arenaCity);
                stmt.setString(5,arena.arenaRegion);
                stmt.setObject(6,arena.arenaSport);
                stmt.setString(7,owner.ownerMobile);
                stmt.executeUpdate();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("select max(aid) from arena;");
                rs.next();
                arena.arenaID = rs.getInt("max(aid)");
            }
            catch(SQLException ex) {
                ex.printStackTrace();
            }
            MessageBox.show("You have successfully added this arena!","Approved",true);
            MessageBox.show("You now have to add the court details for this arena","Add Court Details",true);
            for(int i=0;i<courts;i++){
                MessageBox.show("You are currently adding details of Court "+(i+1),"Add Court"+(i+1),true);
                addCourts(arena,i);
            }
            ownerDashboard(owner);
        });

        Button backButton = new Button("Back");
        backButton.setStyle(cssStrings.buttonStyle(60,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(60,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(60,120)));
        backButton.setOnAction(event -> ownerDashboard(owner));

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(500, 500);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(arenaLabel, 0, 0, 5, 1);
        gridPane.add(nameText, 1, 4);
        gridPane.add(nameField, 3, 4);
        gridPane.add(sportText, 1, 5);
        gridPane.add(sportChoiceBox, 3, 5);
        gridPane.add(openText, 1, 6);
        gridPane.add(openField, 3, 6);
        gridPane.add(closeText, 1, 7);
        gridPane.add(closeField, 3, 7);
        gridPane.add(cityText, 1, 8);
        gridPane.add(cityChoiceBox, 3, 8);
        gridPane.add(regionText, 1, 9);
        gridPane.add(regionChoiceBox, 3, 9);
        gridPane.add(courtText, 1, 10);
        gridPane.add(courtField, 3, 10);
        gridPane.add(createButton, 1, 11);
        gridPane.add(backButton, 3, 11);
        stage.setScene(new Scene(gridPane, 400, 500,Color.WHITE));
        stage.show();
    }

    private void addCourts(arenaObject arena, int i){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Court");
        stage.setMinWidth(400);
        stage.setMinHeight(500);
        Label courtLabel = new Label("Court "+(i+1));
        courtLabel.setTextFill(Color.web("#4dde0b", 0.8));
        courtLabel.setStyle("-fx-font-weight: bold");
        courtLabel.setFont(new Font("TimesRoman", 50));

        Label typeText = new Label("Court Type");
        typeText.setStyle("-fx-font-weight: bold");
        Label costText = new Label("Court Cost ");
        costText.setStyle("-fx-font-weight: bold");

        TextField typeField = new TextField();
        TextField costField = new TextField();

        Button createButton = new Button("Add");
        createButton.setStyle(cssStrings.buttonStyle(60,120));
        createButton.setOnMouseEntered(event -> createButton.setStyle(cssStrings.onButtonStyle(60,120)));
        createButton.setOnMouseExited(event ->createButton.setStyle(cssStrings.buttonStyle(60,120)));
        createButton.setOnAction(event -> {
            String courtType = typeField.getText();
            if(courtType.length()==0){
                MessageBox.show("Please Enter a Valid Type/Comment", "Invalid Entry",false);
                return;
            }
            String courtCost = costField.getText();
            if (!Pattern.compile("[0-9]+(/.[0-9][0-9]?)?").matcher(courtCost).find()) {
                MessageBox.show("Please Enter a Valid Cost", "Invalid Entry",false);
                return;
            }
            float cost = Float.parseFloat(courtCost);
            if(cost<=0){
                MessageBox.show("Court must have at least some cost", "Invalid Entry",false);
                return;
            }
            try(
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx")
            ){
                PreparedStatement stmt=  conn.prepareStatement("INSERT INTO court(cost,type,aid,cnum) VALUES (?,?,?,?);");
                stmt.setFloat(1,cost);
                stmt.setString(2,courtType);
                stmt.setInt(3,arena.arenaID);
                stmt.setInt(4,i+1);
                stmt.executeUpdate();
            }
            catch(SQLException ex) {
                ex.printStackTrace();
            };
            MessageBox.show("You have successfully added this court!","Successful",true);
            stage.close();
        });
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(500, 500);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(courtLabel, 0, 0, 5, 1);
        gridPane.add(typeText, 1, 4);
        gridPane.add(typeField, 3, 4);
        gridPane.add(costText, 1, 5);
        gridPane.add(costField, 3, 5);
        gridPane.add(createButton, 1, 6);
        stage.setScene(new Scene(gridPane, 400, 500,Color.WHITE));
        stage.showAndWait();
    }

    private void viewArena(ownerObject owner) {
        TilePane tile1 = new TilePane();
        tile1.setHgap(10);
        tile1.setVgap(10);
        tile1.setPrefColumns(1);
        tile1.setPrefTileWidth(350);
        tile1.setPadding(new Insets(10, 10, 10, 10));
        Button backButton = new Button("Back");
        backButton.setStyle(cssStrings.buttonStyle(50,120));
        backButton.setOnMouseEntered(event -> backButton.setStyle(cssStrings.onButtonStyle(50,120)));
        backButton.setOnMouseExited(event -> backButton.setStyle(cssStrings.buttonStyle(50,120)));
        backButton.setOnAction(event -> ownerDashboard(owner));

        arenaObject arena=new arenaObject();
        boolean flag=false;
        try(
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playo", "myuser", "xxxx");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM arena WHERE mobile='"+owner.ownerMobile+"';")
        ){
            while(rs.next()){
                flag=true;
                arena.arenaID = rs.getInt("aid");
                arena.arenaName=rs.getString("aname");
                arena.openingTime= LocalTime.parse(rs.getString("opening"));
                arena.closingTime= LocalTime.parse(rs.getString("closing"));
                arena.arenaCity=rs.getString("city");
                arena.arenaRegion=rs.getString("region");
                arena.arenaSport=rs.getString("sname");

                Rectangle r = new Rectangle(350, 50);
                r.setFill(Color.web("#269e1e"));

                Statement stm = conn.createStatement();
                ResultSet rst = stm.executeQuery("SELECT count(*) FROM court WHERE aid='"+arena.arenaID+"';");
                rst.next();
                int nCourts=rst.getInt("count(*)");

                Label name = new Label(arena.arenaName);
                name.setStyle("-fx-font-weight: bold ; -fx-font-size: 18;" );
                name.setTextFill(Color.WHITE);
                Label time = new Label(arena.openingTime+"  to  "+arena.closingTime);
                time.setTextFill(Color.WHITE);
                Label sport = new Label(arena.arenaSport);
                sport.setTextFill(Color.WHITE);
                Label court = new Label("Number of courts: "+nCourts);
                court.setTextFill(Color.WHITE);

                StackPane s = new StackPane(r, name, time, sport, court);
                s.setAlignment(name, Pos.TOP_LEFT);
                s.setAlignment(time, Pos.BOTTOM_LEFT);
                s.setAlignment(sport, Pos.TOP_RIGHT);
                s.setAlignment(court, Pos.BOTTOM_RIGHT);
                tile1.getChildren().add(s);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        if(!flag) {
            MessageBox.show("No arena registered under this owner!", "Invalid Entry",false);
            return;
        }
        ScrollPane spane = new ScrollPane(tile1);
        spane.setMinWidth(390);
        spane.setPrefWidth(390);
        spane.setMaxWidth(390);
        spane.setMinHeight(400);
        spane.setMaxHeight(400);
        spane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        StackPane stack = new StackPane(spane);
        stack.setMargin(spane, new Insets(20, 40, 20, 40));
        VBox vBox = new VBox(stack,backButton);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox,400,500);
        stage.setScene(scene);
        stage.setTitle(owner.ownerName+"'s Arenas ");
        stage.show();
    }
}

class playerObject {
    String playerName;
    String playerEmail;
    String playerPassword;
    LocalDate playerDOB;
    String playerCity;
    String playerRegion;
    String gender;
}

class ownerObject {
    String ownerName;
    String ownerMobile;
    String ownerPassword;
    LocalDate ownerDOB;
    String gender;
}

class arenaObject {
    int arenaID;
    String arenaName;
    LocalTime openingTime;
    LocalTime closingTime;
    String arenaCity;
    String arenaRegion;
    String arenaSport;
    String ownerMobile;
    static String sports[] = new String[]{"Tennis", "Badminton", "Football", "Basketball", "Cricket", "Pool", "Swimming"};

    public arenaObject(arenaObject other) {
        this.arenaID = other.arenaID;
        this.arenaName = other.arenaName;
        this.openingTime = other.openingTime;
        this.closingTime = other.closingTime;
        this.arenaCity = other.arenaCity;
        this.arenaRegion = other.arenaRegion;
        this.arenaSport = other.arenaSport;
        this.ownerMobile = other.ownerMobile;
    }

    public arenaObject() {
    }
}

class courtObject {
    String type;
    int arenaID, courtID, cNum;
    float cost;

    public courtObject(courtObject other) {
        this.type = other.type;
        this.arenaID = other.arenaID;
        this.courtID = other.courtID;
        this.cNum = other.cNum;
        this.cost = other.cost;
    }

    public courtObject(){
    }
}

class bookingObject {
    int bid, startSlot, endSlot, cid;
    LocalDate bdate;
    boolean hosting;
    String email;

    public bookingObject(bookingObject other) {
        this.bid = other.bid;
        this.startSlot = other.startSlot;
        this.endSlot = other.endSlot;
        this.cid = other.cid;
        this.bdate = other.bdate;
        this.hosting = other.hosting;
        this.email = other.email;
    }

    public bookingObject() {
    }
}

class hostingObject {
    int bid, maxPlayers, currentNo;

    public hostingObject(hostingObject other) {
        this.bid = other.bid;
        this.maxPlayers = other.maxPlayers;
        this.currentNo = other.currentNo;
    }

    public hostingObject()  {
    }
}

class MessageBox {
    public static void show(String message, String title, boolean good) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(350);
        stage.setMinHeight(150);
        Label lbl = new Label();
        if(good)
            lbl.setTextFill(Color.GREEN);
        else
            lbl.setTextFill(Color.RED);
        lbl.setStyle("-fx-font-weight: bold");
        lbl.setFont(new Font("TimesRoman", 18));
        lbl.setText(message);

        Button btnOK = new Button();
        btnOK.setText("OK");
        btnOK.setStyle(cssStrings.buttonStyle(40,90));
        btnOK.setOnMouseEntered(event -> btnOK.setStyle(cssStrings.onButtonStyle(40,90)));
        btnOK.setOnMouseExited(event -> btnOK.setStyle(cssStrings.buttonStyle(40,90)));


        btnOK.setOnAction(e -> stage.close());
        VBox pane = new VBox(20);

        pane.getChildren().addAll(lbl, btnOK);
        pane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();
    }
}

class cssStrings {
    static String buttonStyle(int ht, int width){
        return "-fx-font-weight: bold ;-fx-text-fill: white;-fx-border-color:green;-fx-border: 42;-fx-background-color: #269e1e ;-fx-pref-height: "+ht+"px ;-fx-pref-width: "+width+"px; -fx-font-size:18";
    }
    static String onButtonStyle(int ht, int width){
        return "-fx-font-weight: bold ;-fx-text-fill: white;-fx-pref-height: "+ht+"px ;-fx-pref-width: "+width+"px; -fx-font-size:18;-fx-effect: dropshadow( three-pass-box, blue, 10.0, 0.0, 0.0, 0.0);-fx-background-color: #269e1e ";
    }
}