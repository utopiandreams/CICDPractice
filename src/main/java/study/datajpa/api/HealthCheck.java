package study.datajpa.api;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheck {

    private final DataSourceProperties dataSourceProperties;

    @GetMapping("/")
    public String healthCheck() {
        return "It's working";
    }

    @GetMapping("/db")
    public String DBCheck() {
        String driverClassName = dataSourceProperties.getDriverClassName();
        String url = dataSourceProperties.getUrl();
        return "DBtype = " + driverClassName + "\n" +
                "url = " + url;
    }
}
