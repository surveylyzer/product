package stepdefinition;

import net.thucydides.core.annotations.Step;
import pages.StartPage;

public class SmokeTestSteps {

    private StartPage startPage;

    @Step
    public void i_open_start_page() {
        startPage.open();
    }

    public void i_click_result() {
        startPage.click_result();
    }

    public void i_should_see_results(String results) {
        startPage.should_see_results(results);
    }
}

