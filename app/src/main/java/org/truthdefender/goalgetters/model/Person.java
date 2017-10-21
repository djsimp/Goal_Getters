package org.truthdefender.goalgetters.model;

/**
 * Created by dj on 10/17/17.
 */

public class Person {
   String username;

   public Person(String username) {
      this.username = username;
   }

   public Person(){}

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }
}
