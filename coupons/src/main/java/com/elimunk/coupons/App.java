package com.elimunk.coupons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.elimunk.coupons.beans.User;
import com.elimunk.coupons.dailyJob.DailyJob;
import com.elimunk.coupons.enums.ClientType;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.logic.UsersController;

@SpringBootApplication
public class App {
	public static void main(String[] args) throws ApplicationException {
//		DailyJob job = new DailyJob();
//		job.startDailyJob();
//		job.stopDailyJob();
		SpringApplication.run(App.class, args);		
		
	}
}
