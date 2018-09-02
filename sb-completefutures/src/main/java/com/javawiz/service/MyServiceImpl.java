package com.javawiz.service;

import static java.lang.Thread.sleep;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MyServiceImpl implements MyService {
	
	@Value("#{'${oxmore}'.split(',')}")
	private List<String> oxHosts;
	
	@Value("#{'${shoreview}'.split(',')}")
	private List<String> svHosts;

	public Future<String> concatenateAsync(String input) {
		if("oxmore".equals(input)) {
			return processHosts(oxHosts);
		} else if("shoreview".equals(input)) {
			return processHosts(svHosts);
		}
		return null;
	  }
	
	private Future<String> processHosts(List<String> input) {
		// Create the collection of futures.
	    List<Future<String>> futures =
	    	input.stream()
	        .map(str -> supplyAsync(() -> callApi(str)))
	        .collect(toList());

	    // Restructure as varargs because that's what CompletableFuture.allOf requires.
	    CompletableFuture<?>[] futuresAsVarArgs = futures
	      .toArray(new CompletableFuture[futures.size()]);

	    // Create a new future that completes once once all of the previous futures complete.
	    CompletableFuture<Void> jobsDone = CompletableFuture.allOf(futuresAsVarArgs);

	    CompletableFuture<String> output = new CompletableFuture<>();

	    // Once all of the futures have completed, build out the result string from results.
	    jobsDone.thenAccept(ignored -> {
	      StringBuilder stringBuilder = new StringBuilder();
	      futures.forEach(f -> {
	        try {
	          stringBuilder.append(f.get());
	        } catch (Exception e) {
	          output.completeExceptionally(e);
	        }
	      });
	      output.complete(stringBuilder.toString());
	    });

	    return output;
	}

	  private String callApi(String str) {
	    try {
	      // restTemplate.invoke(...)
	      sleep(1000);
	    } catch (Exception e) {
	    }
	    return str.toUpperCase();
	  }
}
