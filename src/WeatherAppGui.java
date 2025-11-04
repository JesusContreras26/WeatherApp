import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;

public class WeatherAppGui extends JFrame{

    private JSONObject weatherData;
    public WeatherAppGui(){
        //Setup our gui and add a title to it
        super("Weather App");

        //Configure gui to end the program's process once the user close it
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Set the size of our GUI in pixels
        setSize(450, 650);

        //Load the GUI at the center of the screen
        setLocationRelativeTo(null);

        //Layout null to be able to manually position our components
        setLayout(null);

        //Prevent resize 
        setResizable(false);

        addGuiComponents();
    }

    private void addGuiComponents(){
        // search field
        JTextField searchField = new JTextField();

        // set the location and size of the components
        searchField.setBounds(15, 15, 351, 45);
        
        //change the font size and style
        searchField.setFont(new Font("Dialog", Font.PLAIN, 24) ); 
        add(searchField);

        //weather image
        JLabel weatherConditionImage = new JLabel(loadImage("src\\assets\\cloudy.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        add(weatherConditionImage);

        //temperature text
        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setBounds(0, 350, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));

        //center the text
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        //weather description
        JLabel weatherDescription = new JLabel("Cloudy");
        weatherDescription.setBounds(0, 405, 450, 36);
        weatherDescription.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherDescription.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherDescription);

        //Humidity Image
        JLabel humidityImage = new JLabel(loadImage("src\\assets\\humidity.png"));
        humidityImage.setBounds(15, 500, 74, 66);
        add(humidityImage);

        //Humidity Text
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90, 500, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        //Windspeed Image
        JLabel windSpeedImage = new JLabel(loadImage("src\\assets\\windspeed.png"));
        windSpeedImage.setBounds(220, 500, 74, 66);
        add(windSpeedImage);

        //Windspeed text
        JLabel windSpeedText = new JLabel("<html><b>Windspeed</b> 15km</html>");
        windSpeedText.setBounds(310, 500, 85, 55);
        windSpeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windSpeedText);

        //search button 
        JButton searchButton = new JButton(loadImage("src\\assets\\search.png"));

        //change the cursor when hovering over the button to be a hand
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 13, 47, 45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                // get user's location
                String userInput = searchField.getText();

                //remove whitespace
                if (userInput.replaceAll("\\s", "").length() <= 0) {
                    return;
                } 

                //weather data
                weatherData = WeatherApp.getWeatherData(userInput);

                //update GUI with the data from the API

                //Update weather image
                String weatherCondition = (String) weatherData.get("weather_condition");

                //depending of the condition one image will be displayed
                switch (weatherCondition) {
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src\\assets\\clear.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src\\assets\\cloudy.png")); 
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src\\assets\\rain.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src\\assets\\snow.png"));
                        break;
                }

                // update temperature text
                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + " C");

                // update weather condition text
                weatherDescription.setText(weatherCondition);

                // update humidity text
                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                // update windspeed text
                double windSpeed = (double) weatherData.get("wind_speed");
                windSpeedText.setText("<html><b>Humidity</b> " + windSpeed + "km/h</html>");
            }
        });
        add(searchButton);
    }

    // used to create images in our gui components
    private ImageIcon loadImage(String resourcePath){
        try {
            //read the image file from the path given
            BufferedImage image = ImageIO.read(new File(resourcePath));

            // returns an image icon that can be render
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle exception
        }

        System.out.println("Could not find resource");
        return null;
    }
}
