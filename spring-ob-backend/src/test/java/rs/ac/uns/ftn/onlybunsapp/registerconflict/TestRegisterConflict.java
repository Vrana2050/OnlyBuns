package rs.ac.uns.ftn.onlybunsapp.registerconflict;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rs.ac.uns.ftn.onlybunsapp.dto.UserRequest;
import rs.ac.uns.ftn.onlybunsapp.service.UserService;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootTest
public class TestRegisterConflict {

    @Autowired
    private UserService userService;

    @Test
    void concurrentRegistration_shouldPreventDuplicateUsername() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        String sharedUsername = "testUser";

        Callable<String> task = () -> {
            UserRequest request = new UserRequest();
            request.setUsername(sharedUsername);
            request.setPassword("pass123");
            request.setEmail("testuser" + "@email.com");
            request.setFirstname("Ime");
            request.setLastname("Prezime");
            request.setAddress("Adresa");

            try {
                userService.save(request);
                return "Success";
            } catch (Exception e) {
                return "Failed";
            }
        };

        List<Future<String>> results = executor.invokeAll(List.of(task, task));

        for (Future<String> result : results) {
            System.out.println("Thread result: " + result.get());
        }
    }

}
