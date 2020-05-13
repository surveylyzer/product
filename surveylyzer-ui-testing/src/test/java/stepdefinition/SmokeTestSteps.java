package stepdefinition;

import net.thucydides.core.annotations.Step;
import pages.ResultPage;
import pages.StartPage;

public class SmokeTestSteps {

    private StartPage startPage;
    private ResultPage resultPage;

    @Step
    public void i_open_start_page() {
        startPage.open();
    }

    @Step
    public void i_click_start_calculation() {
        startPage.i_click_start_cal();
    }

    @Step
    public void i_click_upload_id() {
        startPage.i_click_upload_id();
    }

    @Step
    public void i_insert_id(String id) {
        startPage.i_insert_id(id);
    }

    @Step
    public void i_click_raw_data_link() {
        resultPage.i_click_raw_data_link();
    }

    @Step
    public void click_help_icon() {
        startPage.click_help_icon();
    }
}

