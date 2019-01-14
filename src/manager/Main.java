package manager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * HBase客户端管理程序（主入口）
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("HBase客户端管理程序 v1.0.0");
        //全屏显示
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        //分组
        Group root = new Group();
        Scene scene = new Scene(root, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight(), Color.WHITE);
        //菜单栏
        MenuBar menuBar = new MenuBar();
        EventHandler<ActionEvent> menuAction = changeMenu();
        //菜单
        Menu menu = new Menu("连接到HBase");
        MenuItem connection = new MenuItem("  连接  ");
        connection.setOnAction(menuAction);
        menu.getItems().add(connection);
        //菜单
        MenuItem exit = new MenuItem("  退出  ");
        exit.setOnAction(menuAction);
        menu.getItems().add(exit);
        menuBar.getMenus().add(menu);

        //左侧树形
        TreeItem<String> treeItem = new TreeItem<>("Root");
        treeItem.setExpanded(true);
        TreeItem<String> item = new TreeItem<>("A");
        treeItem.getChildren().add(item);
        item = new TreeItem<>("B");
        treeItem.getChildren().add(item);
        TreeView<String> tree = new TreeView<>(treeItem);

        //中间区域
        BorderPane centerBorderPane = new BorderPane();
        FlowPane top = new FlowPane();
        top.setHgap(10);
        top.setVgap(20);
        top.setPadding(new Insets(15, 15, 15, 15));
        Label label1 = new Label("条件1");
        top.getChildren().add(label1);
        TextField textField1 = new TextField();
        textField1.setPrefWidth(200);
        top.getChildren().add(textField1);
        Label label2 = new Label("条件2");
        top.getChildren().add(label2);
        TextField textField2 = new TextField();
        textField2.setPrefWidth(200);
        top.getChildren().add(textField2);
        Label label3 = new Label("条件3");
        top.getChildren().add(label3);
        TextField textField3 = new TextField();
        textField3.setPrefWidth(200);
        top.getChildren().add(textField3);

        Pagination pagination = new Pagination(28, 0);
        pagination.setStyle("-fx-border-color:#2529ff;");
        pagination.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
        AnchorPane anchor = new AnchorPane();
        AnchorPane.setTopAnchor(pagination, 10.0);
        AnchorPane.setRightAnchor(pagination, 10.0);
        AnchorPane.setBottomAnchor(pagination, 10.0);
        AnchorPane.setLeftAnchor(pagination, 10.0);
        anchor.getChildren().addAll(pagination);

        centerBorderPane.setTop(top);
        centerBorderPane.setCenter(pagination);

        //布局
        BorderPane rootBorderPane = new BorderPane();
        rootBorderPane.prefHeightProperty().bind(scene.heightProperty());
        rootBorderPane.prefWidthProperty().bind(scene.widthProperty());
        rootBorderPane.setTop(menuBar);
        rootBorderPane.setLeft(tree);
        rootBorderPane.setCenter(centerBorderPane);
        root.getChildren().add(rootBorderPane);
        primaryStage.setScene(scene);
        //显示
        primaryStage.show();
    }

    /**
     * 菜单事件
     *
     * @return
     */
    private EventHandler<ActionEvent> changeMenu() {
        return new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                MenuItem menuItem = (MenuItem) event.getSource();
                String text = menuItem.getText().trim();
                if (text.equals("连接")) {
                    Stage window = new Stage();
                    window.setTitle("连接管理");
                    window.initModality(Modality.APPLICATION_MODAL);
                    window.setMaxWidth(650);
                    window.setMaxHeight(380);
                    window.setResizable(false);
                    BorderPane layout = new BorderPane();
                    Button button1 = new Button("新建");
                    button1.setMaxHeight(50);
                    button1.setMinWidth(80);
                    button1.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            newConnection();
                        }
                    });
                    Button button2 = new Button("编辑");
                    button2.setMaxHeight(50);
                    button2.setMinWidth(80);
                    Button button3 = new Button("删除");
                    button3.setMaxHeight(50);
                    button3.setMinWidth(80);
                    Button button4 = new Button("克隆");
                    button4.setMaxHeight(50);
                    button4.setMinWidth(80);
                    HBox hbox = new HBox(10);
                    hbox.setPadding(new Insets(10, 12, 15, 12));
                    hbox.getChildren().addAll(button1, button2, button3, button4);
                    hbox.setAlignment(Pos.CENTER);
                    layout.setTop(hbox);

                    TableView table = new TableView();
                    TableColumn column1 = new TableColumn("名称");
                    column1.setMinWidth(200);
                    column1.setSortable(false);
                    TableColumn column2 = new TableColumn("地址");
                    column2.setMinWidth(200);
                    column2.setSortable(false);
                    TableColumn column3 = new TableColumn("密码");
                    column3.setMinWidth(200);
                    column3.setSortable(false);
                    table.getColumns().addAll(column1, column2, column3);
                    layout.setCenter(table);

                    Scene scene = new Scene(layout);
                    window.setScene(scene);
                    window.showAndWait();
                } else if (text.equals("退出")) {

                }
            }
        };
    }

    public int itemsPerPage() {
        return 8;
    }

    public VBox createPage(int pageIndex) {
        VBox box = new VBox(5);
        int page = pageIndex * itemsPerPage();
        for (int i = page; i < page + itemsPerPage(); i++) {
            VBox element = new VBox();
            Hyperlink link = new Hyperlink("Item " + (i + 1));
            link.setVisited(true);
            Label text = new Label("Search results\nfor " + link.getText());
            element.getChildren().addAll(link, text);
            box.getChildren().add(element);
        }
        return box;
    }

    private void newConnection() {
        Stage window = new Stage();
        window.setTitle("新建连接");
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMaxWidth(700);
        window.setMaxHeight(500);
        window.setResizable(false);
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);

        FlowPane top = new FlowPane();
        top.setHgap(10);
        top.setVgap(20);
        top.setPadding(new Insets(15, 15, 15, 15));
        Label label1 = new Label("主机：");
        top.getChildren().add(label1);
        TextField textField1 = new TextField();
        textField1.setPrefWidth(200);
        top.getChildren().add(textField1);
        Label label2 = new Label("端口：");
        top.getChildren().add(label2);
        TextField textField2 = new TextField();
        textField2.setPrefWidth(100);
        top.getChildren().add(textField2);

        Button test = new Button("测试连接");
        top.getChildren().add(test);
        Button save = new Button("保存");
        top.getChildren().add(save);
        Button cancel = new Button("取消");
        top.getChildren().add(cancel);

        layout.getChildren().addAll(top);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
