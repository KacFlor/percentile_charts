module org.example.percentile_grids {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.jfree.jfreechart;
    requires java.logging;

    opens org.example.percentile_grids to javafx.fxml;
    exports org.example.percentile_grids;
}
