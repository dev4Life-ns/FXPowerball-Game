package com.game.fxpowerballgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PowerBallApplication extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(PowerBallApplication.class.getResource("powerball-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 850, 640);
		stage.setTitle("Power Ball Game!");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}