package com.elimunk.coupons.logic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.elimunk.coupons.beans.PostLoginUserData;
import com.elimunk.coupons.interfaces.ICacheManager;

@Component
public class CacheManager implements ICacheManager{
		
		private Map<Object , PostLoginUserData> map;
		
		public CacheManager() {
			this.map = new HashMap<>();
		}

		@Override
		public void put(Object token, PostLoginUserData value) {
			this.map.put(token,  value);
		}

		@Override
		public Object get(Object token) {
			return this.map.get(token);
		}
}
