import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StageReader {
    public static Stage readStage(String path) throws IOException {
        Stage stage = new Stage();
        Properties props = (new Properties());
        props.load(new FileInputStream(path));
        for (String key : props.stringPropertyNames()) {
            System.out.println(key);
            String value = props.getProperty(key);
            Pattern cell = Pattern.compile("(.)(\\d+)");
            List<Cell> cellsInQuestion = new ArrayList<Cell>();
            Matcher cellMatcher = cell.matcher(key);
            if (cellMatcher.matches()) {
                System.out.println(cellMatcher);
                char col = cellMatcher.group(1).charAt(0);
                int row = Integer.parseInt(cellMatcher.group(2));
                stage.grid.cellAtColRow(col, row).ifPresent(cellsInQuestion::add);
            } else {
                System.out.println("no match");
            }
            System.out.println(cellsInQuestion);
            for (Cell c : cellsInQuestion) {
                System.out.println(c);
                if (value.equals("train")) {
                    stage.actors.add(new Train(c));
                } else if (value.equals("car")) {
                    stage.actors.add(new Car(c));
                } else if (value.equals("boat")) {
                    stage.actors.add(new Boat(c));
                }
            }
        }
        return stage;
    }
}