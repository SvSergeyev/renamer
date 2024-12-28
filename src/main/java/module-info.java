module tech.sergeyev.renamer {
    requires javafx.controls;
    requires javafx.fxml;


    opens tech.sergeyev.renamer to javafx.fxml;
    exports tech.sergeyev.renamer;
    exports tech.sergeyev.renamer.api;
    opens tech.sergeyev.renamer.api to javafx.fxml;
}