package com.elimunk.coupons.interfaces;

import com.elimunk.coupons.beans.PostLoginUserData;

public interface ICacheManager {

	public void put(Object key, PostLoginUserData value);
	public Object get(Object key);
}
