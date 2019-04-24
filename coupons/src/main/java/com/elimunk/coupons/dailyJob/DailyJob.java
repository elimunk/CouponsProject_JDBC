package com.elimunk.coupons.dailyJob;

import java.util.Timer;
import java.util.TimerTask;

import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.logic.CouponsController;

public class DailyJob extends TimerTask {

	private Timer timer = new Timer();
	private CouponsController couponController = new CouponsController();
	
	// run is function that defined for the task 
	// that will be start by the timer every time that defined at the timer 
	@Override
	public void run() {
		try {
			// Delete all expired coupons
			couponController.deleteExpiredCoupon();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
	
	// this function start the job , contains the timer for schedule the task
	public void startDailyJob() {
		// Define the task with the job 
		TimerTask timerTask = new DailyJob();

		// Creating a timer
		this.timer = new Timer();

		// Tell the timer to run the task every 24 hours, starting now
		this.timer.scheduleAtFixedRate(timerTask, 0, 1000*60*60*24);

		System.out.println("~Daily task started~");
	}
	
	// this function stop the job by the cancel the timer
	public void stopDailyJob() {
		// stop the task
		this.timer.cancel();
		System.out.println("~Daily task cancelled~");
	}
}
