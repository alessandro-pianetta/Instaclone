package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.cityName) EditText cityName;
    @BindView(R.id.resultsTextView) TextView resultsTextView;

    @OnClick(R.id.button)
    public void findWeather(View view) {

        DownloadTask task = new DownloadTask();
        String result = null;

        try {

            result = task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + cityName.getText().toString() + ",us&appId=84b10b2fa56229a5787b446b15c1e24c").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public String convert(String str) {

        // Create a char array of given String
        char ch[] = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {

            // If first character of a word is found
            if (i == 0 && ch[i] != ' ' ||
                    ch[i] != ' ' && ch[i - 1] == ' ') {

                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {

                    // Convert into Upper-case
                    ch[i] = (char)(ch[i] - 'a' + 'A');
                }
            }

            // If apart from first character
            // Any one is in Upper-case
            else if (ch[i] >= 'A' && ch[i] <= 'Z')

                // Convert into Lower-Case
                ch[i] = (char)(ch[i] + 'a' - 'A');
        }

        // Convert the char array to equivalent String
        String st = new String(ch);
        return st;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch(Exception e) {
                e.printStackTrace();
                return "Failed";
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                String message = "";

                JSONObject jsonObject = new JSONObject(result);
                String weather = jsonObject.getString("weather");
                JSONArray currentWeather = new JSONArray(weather);

//                for (int i = 0; i < currentWeather.length(); i++) {
                for (int i = 0; i < 1; i++) {
                    JSONObject dataPoint = currentWeather.getJSONObject(i);


                    String main = "";
                    String description = "";

                    main = dataPoint.getString("main");
                    description = dataPoint.getString("description");

                    if (main != "" && description != "") {

                        message += main + ":" + "\r\n" + convert(description);

                    }

                    if (message != "") {
                        resultsTextView.setText(message);
                    }

                }

            } catch(Exception e) {
                e.printStackTrace();
            }

        }
    }

    static void main(String[] args){
        System.out.println("Hello World");

    }

}
