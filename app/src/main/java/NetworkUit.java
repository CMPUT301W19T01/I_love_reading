import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUit {
    private static String BOOk_BASE_URI = "https://www.googleapis.com/books/v1/volumes?";
    private static String QUERY_type = "q";
    private static String QUERY_par = "=isbn:";
    private static final String PRINT_base = "printType";
    private static final String PRINT_type = "books";
    private static final String method = "GET";

    public static String getBook ( String ISBN ){
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String bookDescription = null;
        StringBuffer Buffer = new StringBuffer();

        try{
            Uri uri = Uri.parse(BOOk_BASE_URI).buildUpon()
                    .appendQueryParameter(QUERY_type,QUERY_par+ISBN)
                    .appendQueryParameter(PRINT_base,PRINT_type)
                    .build();

            URL url = new URL(uri.toString());

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            if (inputStream == null){
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String output = bufferedReader.readLine();
            if (output == null || output.length() == 0){
                return null;
            }

            Buffer.append(output+"/n");
            bookDescription = Buffer.toString();
            Log.d("CURRENT BOOK JASON",bookDescription);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }

            if(bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            return bookDescription;
        }
    }



}
