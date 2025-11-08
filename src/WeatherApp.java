//Backend logic to connect with the API

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherApp {
    //fetch weather data for a location
    @SuppressWarnings("unchecked")
    public static JSONObject getWeatherData(String locationName){
        JSONArray locationData = getLocationData(locationName);

        // extract latitude and longitude, the API returns several cities with the same name
        //however almost always the first one is our city so we are going to work with it
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        //API request URL with location coordinates
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,relativehumidity_2m,weather_code,windspeed_10m&timezone=America%2FLos_Angeles";

        try {
            //call API and get a response
            HttpURLConnection conn = fetchApiResponse(urlString);

            //check response status
            if (conn.getResponseCode() != 200) {
                System.out.println("Error connecting");
                return null;
            }

            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }

            //close scanner and connection
            scanner.close();
            conn.disconnect();

            //parse data
            JSONParser parser = new JSONParser();
            JSONObject resuJsonObject = (JSONObject) parser.parse(String.valueOf(resultJson));

            //retrieve hourly data
            JSONObject hourly = (JSONObject) resuJsonObject.get("hourly");

            //get hourly index data of today and tomorrow
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexCurrentTime(time, false);
            int twIndex = findIndexCurrentTime(time, true);

            //get today weather information

            // get temperature
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            //get weather code
            JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
            //Convert the weather code to something more readable
            String weatherCondition = convertCode((long) weatherCode.get(index));

            //get Humidity
            JSONArray humidityData = (JSONArray) hourly.get("relativehumidity_2m");
            long humidity = (long) humidityData.get(index);

            //get wind speed
            JSONArray windData = (JSONArray) hourly.get("windspeed_10m");
            double windSpeed = (double) windData.get(index);

            //get tomorrow weather information

            // get tomorrow temperature
            JSONArray tmTemperatureData = (JSONArray) hourly.get("temperature_2m");
            double tmTemperature = (double) tmTemperatureData.get(twIndex);

            //get tomorrow weather code
            JSONArray tmWeatherCode = (JSONArray) hourly.get("weather_code");
            //Convert the weather code to something more readable
            String tmWeatherCondition = convertCode((long) tmWeatherCode.get(twIndex));

            //get tomorrow Humidity
            JSONArray tmHumidityData = (JSONArray) hourly.get("relativehumidity_2m");
            long tmHumidity = (long) tmHumidityData.get(twIndex);

            //get tomorrow wind speed
            JSONArray tmWindData = (JSONArray) hourly.get("windspeed_10m");
            double tmWindSpeed = (double) tmWindData.get(twIndex);


            //build the weather json data object that is going to be access in the GUI 
            //first with today weather
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("wind_speed", windSpeed);

            //next with tomorrow weather
            weatherData.put("tmtemperature", tmTemperature);
            weatherData.put("tmweather_condition", tmWeatherCondition);
            weatherData.put("tmhumidity", tmHumidity);
            weatherData.put("tmwind_speed", tmWindSpeed);

            return weatherData;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
    
    //get user city
    public static String getUserCurrentCity(){
        try {
            URL url = new URL("http://ip-api.com/json/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream inputStream = conn.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A"); // Read entire stream as one token
            String response = scanner.hasNext() ? scanner.next() : "";
            scanner.close();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(String.valueOf(response));
            return json.get("city").toString();


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "Unknown City";

        }
    }

    //retrieve coordinates for a given location
    public static JSONArray getLocationData(String locationName){
        locationName = locationName.replace(" ", "+");

        //build API url with location parameter
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";

        //try to connect with the API and get a response
        try {
            //another function is called because this API connection will be made several times
            //through the app
            HttpURLConnection conn = fetchApiResponse(urlString);

            //check response status
            if (conn.getResponseCode() != 200) {
                System.out.println("Error making the connection");
                return null;
            }else{
                //Store API response
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                
                //Store the resulting json data into the string builder
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }

                //close scanner and connection to save resources
                scanner.close();
                conn.disconnect();

                //Parse JSON string to a JSON object
                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObject = (JSONObject) parser.parse(String.valueOf(resultJson));

                JSONArray locationData = (JSONArray) resultsJsonObject.get("results");
                return locationData;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString){
        try {
            //attemp to connect
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //set request method to get
            conn.setRequestMethod("GET");

            //connect to the API
            conn.connect();
            return conn;
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        //when a connection can't be made we return null
        return null;
    }

    private static int findIndexCurrentTime(JSONArray timeList, boolean tomorrow){
        String currentTime = getCurrentTime(tomorrow);

        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)){
                return i;
            }
        }
        return 0;
    }

    public static String getCurrentTime(boolean tomorrow){
        //get current time
        LocalDateTime DateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        String formattedDateTime;
        if (tomorrow) {
            LocalDateTime tomorrowDate = DateTime.plusDays(1);
            formattedDateTime = tomorrowDate.format(formatter);
        } else {
            //Format the date to be alike the API date
            formattedDateTime = DateTime.format(formatter);
        }

        return formattedDateTime;
        
    }

    private static String convertCode(long weatherCode){
        String weatherCondition = "";
        if (weatherCode == 0L) {
            weatherCondition = "Clear";
        }else if(weatherCode>0L && weatherCode<=3L){
            weatherCondition = "Cloudy";
        }else if((weatherCode >= 51L && weatherCode <= 67L) || (weatherCode >= 80L && weatherCode <= 99L)){
            weatherCondition = "Rain";
        }else if(weatherCode >= 71L && weatherCode <= 77L){
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }
}
