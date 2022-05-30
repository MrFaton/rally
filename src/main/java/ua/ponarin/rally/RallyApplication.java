package ua.ponarin.rally;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.ponarin.rally.service.RallyService;
import ua.ponarin.rally.service.UserInputService;

@SpringBootApplication
@Log4j2
@RequiredArgsConstructor
public class RallyApplication implements CommandLineRunner {
	private final UserInputService userInputService;
	private final RallyService rallyService;

	public static void main(String[] args) {
		log.info("STARTING THE APPLICATION");
		SpringApplication.run(RallyApplication.class, args);
		log.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) {
		var done = true;
		do {
			var userOption = userInputService.requestUserOption();
			switch (userOption) {
				case 0:
					done = false;
					break;
				case 1:
					rallyService.printUserStoriesWithDifferentIterations();
					break;
				case 2:
					rallyService.propagateIterationToPipIteration();
					break;
				case 3:
					rallyService.propagatePipIterationToIteration();
			}
		} while (done);
	}
}
