package com.eli.coupons.interfaces;

import com.eli.coupons.beans.PostLoginUserData;

public interface ICacheManager {

	public void put(Object key, PostLoginUserData value);
	public Object get(Object key);
}
