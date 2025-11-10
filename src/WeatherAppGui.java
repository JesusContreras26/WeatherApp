import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
        setSize(950, 650);

        //Load the GUI at the center of the screen
        setLocationRelativeTo(null);

        //Layout null to be able to manually position our components
        setLayout(null);

        //Prevent resize 
        setResizable(false);

        
        getContentPane().setBackground(new Color(201, 218, 255));

        addGuiComponents();

    }

    private void addGuiComponents(){
        //Get user city through their IP using the function getUserCurrentCity from the backend part
        String userActualCity = WeatherApp.getUserCurrentCity().toLowerCase();
        //Get weather information with the user city
        JSONObject currentCityWeatherData = WeatherApp.getWeatherData(userActualCity);
        //Some cities recommendation
        ArrayList<String> citiesRecom = new ArrayList<>();
        citiesRecom.add("Las Vegas");
        citiesRecom.add("Bufalo");
        citiesRecom.add("Tokyo");
        citiesRecom.add("New York");
        citiesRecom.add("Paris");

        // search field
        JLabel cityLabel = new JLabel("Introduce a City");
        cityLabel.setBounds(20, 0, 400, 30 );
        cityLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        add(cityLabel);
        JTextField searchField = new JTextField();

        // set the location and size of the components
        searchField.setBounds(20, 30, 850, 40);
        
        //change the font size and style
        searchField.setFont(new Font("Dialog", Font.PLAIN, 24) ); 
        add(searchField);

        JLabel cityRecommenLabel = new JLabel();

        for (int i = 0; i < citiesRecom.size(); i++) {
            if (i == 0) {
                cityRecommenLabel.setText("Some recommendations: " + citiesRecom.get(i) + ", ");
            }else if(i == citiesRecom.size()){
                cityRecommenLabel.setText(cityRecommenLabel.getText() + ", " + citiesRecom.get(i));
            }else {
                cityRecommenLabel.setText(cityRecommenLabel.getText() + citiesRecom.get(i) + ", ");
            }    
        }

        cityRecommenLabel.setBounds(20,60,600,40);
        cityRecommenLabel.setFont(new Font("Dialog", Font.BOLD, 15));
        add(cityRecommenLabel);

        JLabel cityName = new JLabel(userActualCity.substring(0,1).toUpperCase() + userActualCity.substring(1));
        cityName.setBounds(360,100,200, 30);
        cityName.setFont(new Font("Dialog", Font.BOLD, 20));
        cityName.setHorizontalAlignment(SwingConstants.CENTER);
        add(cityName);

        //Today Weather

        //today label
        JLabel todayLabel = new JLabel("Today Weather");
        todayLabel.setBounds(0, 120, 450, 217);
        todayLabel.setFont(new Font("Dialog", Font.BOLD, 30));
        todayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(todayLabel);

        //weather image
        JLabel weatherConditionImage = new JLabel(loadImage(weatherConditionImg(currentCityWeatherData.get("weather_condition").toString())));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        add(weatherConditionImage);

        //temperature text
        JLabel temperatureText = new JLabel(currentCityWeatherData.get("temperature").toString() + " C");
        temperatureText.setBounds(0, 350, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));

        //center the text
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        //weather description
        JLabel weatherDescription = new JLabel(currentCityWeatherData.get("weather_condition").toString());
        weatherDescription.setBounds(0, 405, 450, 36);
        weatherDescription.setFont(new Font("Dialog", Font.PLAIN, 25));
        weatherDescription.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherDescription);

        //Humidity Image
        JLabel humidityImage = new JLabel(loadImage("src\\assets\\humidity.png"));
        humidityImage.setBounds(15, 500, 74, 66);
        add(humidityImage);

        //Humidity Text
        JLabel humidityText = new JLabel("<html><b>Humidity</b> " + currentCityWeatherData.get("humidity") + "%</html>");
        
        humidityText.setBounds(90, 500, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        //Windspeed Image
        JLabel windSpeedImage = new JLabel(loadImage("src\\assets\\windspeed.png"));
        windSpeedImage.setBounds(220, 500, 74, 66);
        add(windSpeedImage);

        //Windspeed text
        JLabel windSpeedText = new JLabel("<html><b>Windspeed</b> " + currentCityWeatherData.get("wind_speed") + "km</html>");
        windSpeedText.setBounds(310, 500, 85, 55);
        windSpeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windSpeedText);

        //tomorrow's weather forecast
        //today label
        JLabel tomorrowLabel = new JLabel("Tomorrow Forecast");
        tomorrowLabel.setBounds(490, 120, 450, 217);
        tomorrowLabel.setFont(new Font("Dialog", Font.BOLD, 30));
        tomorrowLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(tomorrowLabel);

        //weather image
        JLabel tmWeatherConditionImage = new JLabel(loadImage(weatherConditionImg(currentCityWeatherData.get("tmweather_condition").toString())));
        tmWeatherConditionImage.setBounds(490, 125, 450, 217);
        add(tmWeatherConditionImage);

        //temperature text
        JLabel tmTemperatureText = new JLabel(currentCityWeatherData.get("tmtemperature").toString() + " C");
        tmTemperatureText.setBounds(490, 350, 450, 54);
        tmTemperatureText.setFont(new Font("Dialog", Font.BOLD, 54));

        //center the text
        tmTemperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(tmTemperatureText);

        //weather description
        JLabel tmWeatherDescription = new JLabel(currentCityWeatherData.get("tmweather_condition").toString());
        tmWeatherDescription.setBounds(490, 405, 450, 36);
        tmWeatherDescription.setFont(new Font("Dialog", Font.PLAIN, 25));
        tmWeatherDescription.setHorizontalAlignment(SwingConstants.CENTER);
        add(tmWeatherDescription);

        //Humidity Image
        JLabel tmHumidityImage = new JLabel(loadImage("src\\assets\\humidity.png"));
        tmHumidityImage.setBounds(505, 500, 74, 66);
        add(tmHumidityImage);

        //Humidity Text
        JLabel tmHumidityText = new JLabel("<html><b>Humidity</b> " + currentCityWeatherData.get("tmhumidity") + "%</html>");
        tmHumidityText.setBounds(580, 500, 85, 55);
        tmHumidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(tmHumidityText);

        //Windspeed Image
        JLabel tmWindSpeedImage = new JLabel(loadImage("src\\assets\\windspeed.png"));
        tmWindSpeedImage.setBounds(730, 500, 74, 66);
        add(tmWindSpeedImage);

        //Windspeed text
        JLabel tmWindSpeedText = new JLabel("<html><b>Windspeed</b> " + currentCityWeatherData.get("tmwind_speed") + "km</html>");
        tmWindSpeedText.setBounds(810, 500, 85, 55);
        tmWindSpeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(tmWindSpeedText);


        //search button 
        JButton searchButton = new JButton(loadImage("src\\assets\\search.png"));

        //change the cursor when hovering over the button to be a hand
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(875, 25, 47, 45);
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
                cityName.setText(userInput.substring(0,1).toUpperCase() + userInput.substring(1));
                //first with today's weather information

                //Update weather image
                String weatherCondition = (String) weatherData.get("weather_condition");
                //depending of the condition one image will be displayed
                weatherConditionImage.setIcon(loadImage(weatherConditionImg(weatherCondition)));

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
                windSpeedText.setText("<html><b>Windspeed</b> " + windSpeed + "km/h</html>");


                //next with tomorrow's weather information

                //Update weather image
                String twWeatherCondition = (String) weatherData.get("tmweather_condition");
                //depending of the condition one image will be displayed
                tmWeatherConditionImage.setIcon(loadImage(weatherConditionImg(twWeatherCondition)));


                // update temperature text
                double tmTemperature = (double) weatherData.get("tmtemperature");
                tmTemperatureText.setText(tmTemperature + " C");

                // update weather condition text
                tmWeatherDescription.setText(twWeatherCondition);

                // update humidity text
                long tmHumidity = (long) weatherData.get("tmhumidity");
                tmHumidityText.setText("<html><b>Humidity</b> " + tmHumidity + "%</html>");

                // update windspeed text
                double tmWindSpeed = (double) weatherData.get("tmwind_speed");
                tmWindSpeedText.setText("<html><b>Windspeed</b> " + tmWindSpeed + "km/h</html>");
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

    private String weatherConditionImg(String weatherConditon){
        String imgPath = "";
        switch (weatherConditon) {
                    case "Clear":
                        imgPath = "src\\assets\\clear.png";
                        break;
                    case "Cloudy":
                        imgPath = "src\\assets\\cloudy.png";
                        break;
                    case "Rain":
                        imgPath = "src\\assets\\rain.png";
                        break;
                    case "Snow":
                        imgPath="src\\assets\\snow.png";
                        break;
                    case "Foggy":
                        imgPath="src\\assets\\foggy.png";
                        break;
                }
        return imgPath;
    }
}

