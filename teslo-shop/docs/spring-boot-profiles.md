## [Spring Boot Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html#features.profiles) 🔗

The `@Profile` annotation in Spring is used to enable or disable beans based on the active profile.

Profiles are a way of grouping configurations and beans for different environments, such as _dev_, _prod_, _test_, etc.

### How do profiles work?

Profiles are enabled through configuration properties, either in an `application.properties` or `application.yml` file, or through environment variables and command line arguments.

1.  **Configuration file**:

    ```properties
    spring.profiles.active=dev
    ```

    ```yaml
    spring:
      profiles:
        active:
          - dev
    ```

2.  **Environment variables**. Set env `SPRING_PROFILES_ACTIVE` before run application:

    ```sh
    export SPRING_PROFILES_ACTIVE=prod

    java -jar myapp.jar
    ```

3.  **Command line arguments**. You can switch the active profile when you start the application:

    ```sh
    java -jar myapp.jar --spring.profiles.active=prod
    ```

### Example with _dev_ and _prod_ profiles

We will have a service that acts differently depending on the active profile.

1.  **Configure profiles:**

    ```properties
    # application-dev.properties
    spring.profiles.active=dev
    ```

    ```properties
    # application-prod.properties
    spring.profiles.active=prod
    ```

2.  **Create the service with different implementations for each profile:**

    ```java
    public interface MessageService {
        String getMessage();
    }

    @Profile("dev")
    @Service
    public class DevMessageService implements MessageService {
        @Override
        public String getMessage() {
            return "Development environment message";
        }
    }

    @Profile("prod")
    @Service
    public class ProdMessageService implements MessageService {
        @Override
        public String getMessage() {
            return "Production environment message";
        }
    }
    ```

3.  **Use service:**

    ```java
    @RestController
    public class MessageController {

        private final MessageService messageService;

        public MessageController(MessageService messageService) {
            this.messageService = messageService;
        }

        @GetMapping("/message")
        public ResponseEntity<String> getMessage() {
            return ResponseEntity.ok(messageService.getMessage());
        }
    }
    ```

<br/>

[Go back](../readme.md)
