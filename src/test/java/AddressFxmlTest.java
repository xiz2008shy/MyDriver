import com.tom.component.setting.MySetting;
import com.tom.controller.AddressPaneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class AddressFxmlTest extends Application {

    @Test
    public void test() {
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        MySetting.initSetting(getParameters());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AddressPaneController.class.getResource("/fxml/AddressPane.fxml"));
        AnchorPane fx = loader.load();
        Scene scene = new Scene(fx,800,600);
        stage.setScene(scene);
        stage.setTitle("AddressFxml");
        stage.show();
    }
}