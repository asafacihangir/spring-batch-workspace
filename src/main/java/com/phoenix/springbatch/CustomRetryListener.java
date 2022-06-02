package com.phoenix.springbatch;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

public class CustomRetryListener implements RetryListener {

  @Override
  public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {

    if(context.getRetryCount() > 0) {
      System.out.println("Attempting retry");
    }
    return true;
  }

  @Override
  public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
      Throwable throwable) {
    // TODO Auto-generated method stub

  }

  @Override
  public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
      Throwable throwable) {
    if(context.getRetryCount() > 0) {
      System.out.println("Failure occurred requiring a retry");
    }

  }

}
