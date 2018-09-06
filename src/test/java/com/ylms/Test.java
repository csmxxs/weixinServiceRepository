package com.ylms;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Test {
   public static List<String> lock=new ArrayList<>();
    
    private static void add(List<String> lock) {
    	//添加完成后, 自动释放锁
    	lock.add("hello");
    }
    public static void State() {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				public void run() {
					synchronized (lock) {
						lock.clear();
						try {
							Thread.sleep(1000);
							add(lock);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
			//timer.schedule(task,0); // delay 为long类型：从现在起过delay毫秒之后执行一次（不周期）
			timer.schedule(task, 0,10);// 从现在起，每隔3秒执行一次
    }
    public static void main(String[] args) throws InterruptedException {
		Test.State();
		while(true) {
			if(Test.lock.size()>0) {
				System.out.println("88888888888888888888888888跳出循環88888888888888888888"+Test.lock.size());
			}else {
				System.out.println("获取不到数据");
			}
		}
		
	}
}
