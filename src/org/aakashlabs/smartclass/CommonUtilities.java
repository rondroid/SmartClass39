package org.aakashlabs.smartclass;

import java.net.InetAddress;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class CommonUtilities {
	
	public static final int SERVERPORT = 4449;
	static int widthvalue=3;
	static int selectedcolour=Color.BLACK;
	public static int mode=1;
	//Mode 0-Teacher,Mode 1- Student
	static boolean message_received=false;
	static String message="";
	static String nickname;
	static InetAddress broadcastaddr;
	static String recipient = "public";
	static String teachername = " ";
	static String teacheraddr = " ";
	static String name = "";
	static int start_board=0;
	static boolean message_exceed=false,message_show=false;


}