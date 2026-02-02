package andrey.starter;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {
    private final AppStarter appStarter;

    @Override
    public void run(String... args) {
        appStarter.start();
    }
}