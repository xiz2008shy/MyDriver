import com.tom.component.setting.MySetting;
import com.tom.controller.AddressPaneController;
import com.tom.controller.FileContentPaneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class FileContentFxmlTest extends Application {

    @Test
    public void test() {
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        MySetting.initSetting(getParameters());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/FileContentPane.fxml"));
        ScrollPane fx = loader.load();
        Scene scene = new Scene(fx,800,600);
        stage.setScene(scene);
        stage.setTitle("FileContentFxml");
        stage.show();
    }
}