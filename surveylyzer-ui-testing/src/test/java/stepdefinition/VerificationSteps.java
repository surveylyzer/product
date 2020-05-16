package stepdefinition;

import net.thucydides.core.annotations.Step;
import pages.HelpPage;
import pages.RawDataPage;
import pages.ResultPage;
import pages.StartPage;

public class VerificationSteps {

    private StartPage startPage;
    private ResultPage resultPage;
    private RawDataPage rawDataPage;
    private HelpPage helpPage;

    @Step
    public void i_see_alert(String alertSubtitle, String alertMessage, String num) {
        startPage.seeAlert(alertSubtitle, alertMessage, num);
    }

    @Step
    public void i_see_chart_results() {
        resultPage.seeChartResults();
    }

    @Step
    public void i_see_data_in_json_format() {
        rawDataPage.i_see_data_in_json_format();
    }

    @Step
    public void i_see_help_page(String pageTitle) {
        helpPage.i_see_help_page(pageTitle);
    }
}
