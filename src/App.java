import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                //display weather app GUI
                new WeatherAppGui().setVisible(true);
            }
        });
    }
}
