import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.io.*;
import java.awt.Robot;
import java.awt.event.InputEvent;




public class CursorMover {
	private static Socket s;
    private static PrintWriter printWriter;

    private static String message = "";
    private static String ip = "127.0.0.1";

    private static BufferedReader br;
	private static InputStreamReader isr;
	private static int x = 0;
	private static int y = 0;
	private static int z = 0;
	private static String arr[];
	private static Robot robot;
	private static int cur_x = 0;
	private static int cur_y = 0;


	public static void main(String args[])
	{
	
	try{
		robot = new Robot();
		//System.out.println("Hello World");
		robot.mouseMove(cur_x,cur_y);
		message = "Hi Samarth";
		s = new Socket(ip, 8081);
		System.out.println("Connection Established");
		isr = new InputStreamReader(s.getInputStream());
		br = new BufferedReader(isr);
		while(true)
		{
			message = br.readLine();
			if(message.equals("Transmission Done"))
				{break;}
			arr = message.split(" ",3);
			//System.out.println(arr[0].split(":",2)[1]);
			//System.out.println(arr[1].split(":",2)[1]);
			x = Math.round(Float.parseFloat(arr[0].split(":",2)[1]));
			y = Math.round(Float.parseFloat(arr[1].split(":",2)[1]));
			cur_x = cur_x + x;
			cur_y = cur_y + y;
			robot.mouseMove(cur_x,cur_y);
			
			z = Math.round(Float.parseFloat(arr[2].split(":",2)[1]));
			if (z==-1) {
				robot.mousePress(InputEvent.BUTTON3_MASK);
				robot.mouseRelease(InputEvent.BUTTON3_MASK);
			}else if (z==1) {
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
			}
			//System.out.println(message);
		}
		

		isr.close();
		br.close();
		s.close();


	}catch(Exception e)
	{
		e.printStackTrace();
	}
	System.out.println("Done");

	}
}