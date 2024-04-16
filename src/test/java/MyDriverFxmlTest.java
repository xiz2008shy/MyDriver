import com.tom.component.setting.MySetting;
import com.tom.controller.MyDriverPaneController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class MyDriverFxmlTest extends Application {

    @Test
    public void test() {
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        MySetting.initSetting(getParameters());
        String basePath = MySetting.getConfig().getBasePath();
        File file = new File(basePath);
        MyDriverPaneController myDriverPaneController = new MyDriverPaneController(file);
        Scene scene = new Scene(myDriverPaneController,800,600);
        stage.setScene(scene);
        stage.setTitle("MyDriverPane");
        stage.show();
    }
}
