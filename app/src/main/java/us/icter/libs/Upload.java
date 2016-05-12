package us.icter.libs;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by jamp on 6/5/16.
 */
public class Upload {
    HttpURLConnection conn = null;
    DataOutputStream dos = null;
    DataInputStream inStream = null;
    //String urlString = "http://192.168.100.16:3000/api/";
    String urlString = "https://bronwsea-test.herokuapp.com/api/";
    String fileName = null;
    boolean resultado = true;

    public Upload(String codigo, String patrulla, int punto, int type, String file){
        fileName = file;

        if (type == 2)
            urlString += "photo/";
        else
            urlString += "video/";

        try {
            urlString += "?codigo=" + URLEncoder.encode(codigo, "UTF-8");
            urlString += "&patrulla=" + URLEncoder.encode(patrulla, "UTF-8");
            urlString += "&punto=" + String.valueOf(punto);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.d("URL", urlString);
    }

    public boolean doFileUpload() {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(fileName));
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            Log.d("Debug", "File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            Log.e("Debug", "error: " + ex.getMessage(), ex);
            resultado = false;
        } catch (IOException ioe) {
            Log.e("Debug", "error: " + ioe.getMessage(), ioe);
            resultado = false;
        }

        try {

            inStream = new DataInputStream(conn.getInputStream());
            String str;
            Log.d("RECEIVE", inStream.readLine());
            while ((str = inStream.readLine()) != null) {
                JSONObject obj = new JSONObject(str);
                if (obj.getInt("status") != 1) {
                    resultado = false;
                }
                Log.d("Debug", "Server Response " + obj.getString("msg"));
            }
            inStream.close();

        } catch (IOException ioex) {
            Log.e("Debug", "error: " + ioex.getMessage(), ioex);
            resultado = false;
        } catch (JSONException jsex) {
            jsex.printStackTrace();
            resultado = false;
        } catch (Exception ex) {
            ex.printStackTrace();
            resultado = false;
        }

        return resultado;
    }
}
