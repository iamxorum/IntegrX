module integrix {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.controlsfx.controls;
	requires com.dlsc.formsfx;
	requires org.kordamp.bootstrapfx.core;
	requires engine;
    requires jlatexmath;
    requires java.desktop;

    opens home to javafx.fxml;
	exports home;
	exports home.controllers;
	opens home.controllers to javafx.fxml;
}