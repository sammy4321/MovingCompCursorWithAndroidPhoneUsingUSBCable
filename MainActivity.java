package com.example.samarth.server_example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    TextView tv ;
    ServerSocket serverSocket;
    Button but;
    Button clicker_1,clicker_2;
    PrintWriter printWriter;
    Socket socket;
    Boolean keepSending = true;
    float x,y,z=0;
    float diff_x;float diff_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.textView);
        tv.setText("Hey Man");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               tv.setText("Hey Jack !!");
            }
        });
        x = -1;y = -1;
        but = findViewById(R.id.send);
        clicker_1 = findViewById(R.id.left);
        clicker_2 = findViewById(R.id.right);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keepSending = false;
                tv.setText("Connection Stopped");
            }
        });
        clicker_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                z = -1;
            }
        });
        clicker_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                z = 1;
            }
        });
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //x = event.getX();
        //y = event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //x = event.getX();
                //y = event.getY();
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                diff_x = event.getX() - x;
                x = event.getX();
                diff_y = event.getY() - y;
                y = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                diff_x = 0;
                diff_y = 0;
                break;
            default:
                break;


        }
        return false ;
    }
    private class SocketServerThread extends Thread
    {
        static final int SocketServerPORT = 8081;
        @Override
        public void run() {
              try
              {
                  serverSocket = new ServerSocket(SocketServerPORT);
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          tv.setText("Waiting Connection");
                      }
                  });
                  socket = serverSocket.accept();

                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          tv.setText("Connection Established");
                      }
                  });

                  but = findViewById(R.id.send);
                  printWriter = new PrintWriter(socket.getOutputStream());
                  while(keepSending)
                  {
                      String sending_string = "x:"+Float.toString(diff_x)+" y:"+Float.toString(diff_y)+" z:"+Float.toString(z);
                      printWriter.println(sending_string);
                      printWriter.flush();
                      if(z!=0)
                      {
                         z=0;
                      }
                      Thread.sleep(25);

                  }

                  printWriter.println("Transmission Done");
                  printWriter.flush();
                  printWriter.close();


              }catch (Exception e)
              {
                  tv.setText("Error Recognised");
                  e.printStackTrace();
              }
        }
    }
}

