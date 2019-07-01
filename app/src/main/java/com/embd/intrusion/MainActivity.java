package com.embd.intrusion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    static String MQTTHOST = "m24.cloudmqtt.com:10149";
    static String USERNAME = "hqdirmym";
    static String PASSWORD = "Lv-1wC7uOMZE";
    String topicStr = "intrusion/embd";

    MqttAndroidClient client;
    MqttConnectOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
            Connection to server code
            To Do : loding while the connection is in progress,
                    if error informe user to virify his connection to the internet.s
        */
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST,clientId);

        options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());


        try{
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this, "connected!", Toast.LENGTH_LONG).show();
                    setSubscription();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this, "failed !!!!", Toast.LENGTH_LONG).show();
                }
            });
        }
        catch(MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                /*
                    hna aw ki yji message y'affichih f text field wela ON terja3 OFF wela daber rassek wech dir
                    ki ykoun on yvibri tlf w tatla3 notification
                */
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    /*
        Publish methode to publish the message to the alarm system
        "0" : ON
        "1" : OFF
    */
    public void publish(View v){
        String topic = topicStr;
        String message = "Hellow World!";
        try {
            client.publish(topic, message.getBytes(), 0, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void setSubscription(){
        try{
            client.subscribe(topicStr, 0);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    public void connect(View v){
        try{
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this, "connected!", Toast.LENGTH_LONG).show();
                    setSubscription();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this, "failed !!!!", Toast.LENGTH_LONG).show();
                }
            });
        }
        catch(MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(View v){
        try{
            IMqttToken token = client.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this, "disconnect!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this, "could not disconnect !!", Toast.LENGTH_LONG).show();
                }
            });
        }
        catch(MqttException e) {
            e.printStackTrace();
        }
    }
}
