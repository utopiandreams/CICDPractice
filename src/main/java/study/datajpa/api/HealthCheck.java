package study.datajpa.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
@RequiredArgsConstructor
public class HealthCheck {

    private final DataSource dataSource;

    @GetMapping("/")
    public String healthCheck() {
        return "It's working";
    }

    @GetMapping("/db")
    public String DBCheck() {
        return dataSource.toString();
    }
}
