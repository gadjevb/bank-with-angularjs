package com.clouway.adapter.sitebricks;

import com.google.sitebricks.headless.Reply;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class SitebricksMatchers {

  public static Matcher<Reply<?>> isOk() {
    return new TypeSafeMatcher<Reply<?>>() {
      @Override
      protected boolean matchesSafely(Reply<?> reply) {
        int actualStatus = getStatus(reply);
        return HttpURLConnection.HTTP_OK == actualStatus;
      }

      private int getStatus(Reply<?> reply) {
        try {
          Field status = reply.getClass().getDeclaredField("status");
          status.setAccessible(true);
          return (int) status.get(reply);
        } catch (NoSuchFieldException | IllegalAccessException e) {
          throw new IllegalStateException("Reply has no status information");
        }
      }

      @Override
      protected void describeMismatchSafely(Reply<?> item, Description description) {
        description.appendText("was ");
        description.appendValue(getStatus(item));
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("status code to be 200");
      }
    };
  }

  public static Matcher<Reply<?>> sameAs(Object object) {
    return  new TypeSafeMatcher<Reply<?>>() {
      @Override
      protected boolean matchesSafely(Reply<?> reply) {
        Object replyObject = getObject(reply);
        return replyObject.equals(object);
      }

      private Object getObject(Reply<?> reply) {
        try {
          Field field = reply.getClass().getDeclaredField("entity");
          field.setAccessible(true);
          return field.get(reply);
        } catch (NoSuchFieldException | IllegalAccessException e) {
          throw new IllegalStateException("Reply has no entity information");
        }
      }

      @Override
      protected void describeMismatchSafely(Reply<?> item, Description description) {
        description.appendText("was ");
        description.appendValue(getObject(item));
      }

      @Override
      public void describeTo(Description description) {
        description.appendText(" is " + object);
      }
    };
  }
}
