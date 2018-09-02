package com.javawiz.service;

import java.util.concurrent.Future;

public interface MyService {
	public Future<String> concatenateAsync(String input);
}
