import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    //9680c260829a46200de332d0dc184ce3
    public static String getWeather(String message, Model model) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=9680c260829a46200de332d0dc184ce3");
        Scanner in = new Scanner((InputStream) url.getContent());
        String res = "";
        while (in.hasNext()) {
            res += in.nextLine();
        }

        JSONObject object = new JSONObject(res);        //главный обьект
        model.setName(object.getString("name"));
        JSONObject main = object.getJSONObject("main"); //обьект из главного обьекта
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i=0; i<getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon((String) obj.get("icon"));
            model.setMain((String) obj.get("main"));
        }

        return "City: " + model.getName() + "\n" +
                "Temp: " + model.getTemp() + "C" +"\n" +
                "Humidity: " + model.getHumidity() + "%" + "\n" +
                "Main: " + model.getMain() + "\n" +
                "http://openweathermap.org/img/wn/" + model.getIcon() + ".png";
    }
}
