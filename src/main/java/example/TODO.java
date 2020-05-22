package example;

    /* 

      ! CHOOSE WISELY --> PICK TASKS THAT PUSH YOU TO LEARN
      ! CHOOSE WISELY --> PICK TASKS THAT PUSH YOU TO LEARN

      * outside MVP features
        ? - email confirmation before subscribing or sending notifications???
        - add other notification options (phone, etc)
        - create topics on the fly
        - notify only specific users

      ! CHOOSE WISELY --> PICK TASKS THAT PUSH YOU TO LEARN       
      ! CHOOSE WISELY --> PICK TASKS THAT PUSH YOU TO LEARN 

      * Refractor 
        ! NOTE: not all of this is necessary --> find tasks where you really learn
        - **** return proper info *** --> 
            - check if data should be returned
            - status code --> on failure return  & on success if no data is returned
        - make repeated vars as class fields --> DRY
        - read through fixme statements
        - think of ways to refractor code --> shouldn't have to refractor next time
        - add modifiers where necessary (final, private, protected)
        - find places for dependency injection
        - read through fixme(s)
        - return success/fail for subscribe
        - identify hard-coded areas --> refractor it
        - return item not found --> for getItem.java
        - throw no items found if none exist

      ! CHOOSE WISELY --> PICK TASKS THAT PUSH YOU TO LEARN
      ! CHOOSE WISELY --> PICK TASKS THAT PUSH YOU TO LEARN

      TODO: 
      - Tasks
      - check google docs for tasks outside of MVP
      - make spring routes for api gateway 
      - things to consider for API 
        - performance --> caching, load balancing for more traffic 
        - security --> authorization & authintication 
        - testability --> write unit tests for routes --> helps you discover edge cases

      !EFFICIENCY TACTICS
       - avoid repeatitive patterns --> deploy, test, deploy, test, deploy, test
       - chunk everything --> deploy once & test everything all at once

       * TEST: cases to test for 
       * - subscribe & unsubscribe email input
       * - DB read & write
       * - send notification function
    
      !COMMIT MESSAGES
        - added request parameters 
        - removed redundant code
        - moved notifications in its own class
        - added static methods where object instance was unnecessary

    */


public class TODO {
    
}