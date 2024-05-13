package com.tom.general;

import com.tom.utils.ImageUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TipBlock {

    public static void showDialog(String msg, String title, Stage ownerStage){
        Stage dialogStage = new Stage();

        DialogPane dialog=new DialogPane();
        Scene scene = new Scene(dialog);
        dialogStage.setScene(scene);
        //设置各区域显示内容
        dialog.setHeaderText(title);
        dialog.setContentText(msg);
        ImageView imageView = ImageUtils.getImageViewFromResources("/img/warning.png", 64, 64);
        dialog.setGraphic(imageView);
        dialog.getButtonTypes().addAll(ButtonType.CLOSE);

        Button ok = (Button)dialog.lookupButton(ButtonType.CLOSE);
        ok.setOnAction(_ -> dialogStage.close());

        dialog.setPrefSize(330,200);
        dialog.setPadding(new Insets(20,20,20,20));

        dialogStage.initOwner(ownerStage);
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        //dialogStage.setAlwaysOnTop(true);
        dialogStage.setResizable(true);
        dialogStage.show();

    }
}
