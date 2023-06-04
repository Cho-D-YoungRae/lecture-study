package hello;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandLineBean {

    private final ApplicationArguments arguments;

    @PostConstruct
    public void init() {
        log.info("source {}", Arrays.toString(arguments.getSourceArgs()));
        log.info("optionNames {}", arguments.getOptionNames());
        for (String optionName : arguments.getOptionNames()) {
            log.info("option args {} = {}", optionName, arguments.getOptionValues(optionName));
        }
    }
}
