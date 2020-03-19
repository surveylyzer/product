import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import stepdefinition.SmokeTestSteps;

@RunWith(SerenityRunner.class)
public class SmokeTest {

    @Managed
    WebDriver driver;

    @Steps
    SmokeTestSteps smokeSteps;


    @Test
    public void sample_test() {
        smokeSteps.i_open_start_page();
        smokeSteps.i_should_see_header_text("Hello, I'm SURVEYLYZER!");
    }
}

