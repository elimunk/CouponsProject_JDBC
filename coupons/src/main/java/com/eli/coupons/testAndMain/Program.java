package com.eli.coupons.testAndMain;

public class Program {

	public static void main(String[] args) throws Exception {

		try {
			Test.testAll();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

}
