package io.harness.rest.utils;

import io.harness.rest.matchers.Matcher;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class Retry<T> {
  private int maxRetryTimes = 2;
  private int retryDelayInMilliseconds = 500;
  private int retryCounter = 0;

  public Retry(int maxRetryTimes, int retryDelayInSec) {
    this.maxRetryTimes = maxRetryTimes;
    this.retryDelayInMilliseconds = retryDelayInSec;
  }

  public Retry(int maxRetryTimes) {
    this.maxRetryTimes = maxRetryTimes;
    this.retryDelayInMilliseconds = 500;
  }

  public T executeWithRetry(Supplier<T> function, Matcher<T> matcher, T expected) {
    return retry(function, matcher, expected);
  }

  private T retry(Supplier<T> function, Matcher<T> matcher, T expected) {
    {
      log.info("Execution will be retried : " + maxRetryTimes + " times.");
      retryCounter = 0;
      T actual;
      while (retryCounter < maxRetryTimes) {
        log.info("Retry Attempt : " + retryCounter);
        try {
          TimeUnit.MILLISECONDS.sleep(this.retryDelayInMilliseconds);
          actual = function.get();
          if (matcher.matches(actual, expected)) {
            return actual;
          }
        } catch (Exception ex) {
          log.info("Execution failed on retry " + retryCounter + " of " + maxRetryTimes + " error: " + ex);
          if (retryCounter >= maxRetryTimes) {
            log.warn("Max retries exceeded.");
            break;
          }
        }
        retryCounter++;
      }
      throw new RuntimeException("Command failed on all of " + maxRetryTimes + " retries");
    }
  }
}
