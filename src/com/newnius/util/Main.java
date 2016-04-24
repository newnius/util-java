package com.newnius.util;

import java.util.Properties;


/*
 * 
 * demo
 * 
 * */


public class Main {
	static int cnt = 0;
	public static synchronized int getCnt(){
		return cnt++;
	}
	
	public static void main(String[] args){
		CRWhatToDo whatToDo = new CRWhatToDo() {
			
			@Override
			public CRMsg doThis() {
				String[] args= {getCnt()+"" , "ha" };
				try {
					CRDAO.getInstance().executeUpdate("insert into test(id, col) values(?, ?)", args);
				} catch (Exception e) {
					e.printStackTrace();
				}
				CRMsg msg = new CRMsg(Integer.parseInt((System.currentTimeMillis()%1000000)+""));
				return msg;
			}
		};
		
		CRCallback callback = new CRCallback() {
			
			@Override
			public void callback(CRMsg msg) {
				System.out.println(msg.getErrno()+"");
			}
		};
		
		String confFile = "dbcp.properties";
		String configFile = confFile;
		Properties dbProperties = new Properties();
		try {
			dbProperties.load(CRDAO.class.getClassLoader().getResourceAsStream(configFile));
			CRDAO.init(dbProperties);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		CRBackgroundTask.init(5);
		
		for(int i=0; i<100 ;i++){
			new CRBackgroundTask(whatToDo, callback).doInBackground();
		}
		
	}
}
