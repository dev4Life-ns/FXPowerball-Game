module com.game.fxpowerballgame {
	requires javafx.controls;
	requires javafx.fxml;
	requires com.jfoenix;
	requires org.apache.commons.lang3;
	requires org.junit.platform.testkit;
	requires org.mockito;
	requires org.junit.platform.commons;
	requires org.junit.jupiter.api;


	opens com.game.fxpowerballgame to javafx.fxml;
	exports com.game.fxpowerballgame;
}