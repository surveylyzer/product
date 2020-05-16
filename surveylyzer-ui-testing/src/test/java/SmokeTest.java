import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import stepdefinition.SmokeTestSteps;
import stepdefinition.VerificationSteps;

@RunWith(SerenityRunner.class)
public class SmokeTest {

    @Managed
    WebDriver driver;

    @Steps
    SmokeTestSteps smokeSteps;

    @Steps
    VerificationSteps verificationSteps;


    @Test
    public void alert_no_uploaded_file_test() {
        String alertSubtitle = "Template Error";
        String alertMessage = "Template file has not been uploaded!";
        smokeSteps.i_open_start_page();
        smokeSteps.i_click_start_calculation();
        verificationSteps.i_see_alert(alertSubtitle, alertMessage, "1");
    }

    @Test
    public void alert_no_id_inserted_test() {
        String alertSubtitle = "ID Error";
        String alertMessage = "'Results ID' Input is empty, please enter a valid ID!";
        smokeSteps.i_open_start_page();
        smokeSteps.i_click_upload_id();
        verificationSteps.i_see_alert(alertSubtitle, alertMessage, "1");
    }

    @Test
    public void upload_existing_id_test() {
        String id = "420beefe-4643-4e7d-88ff-171ae1e2e73b";
        smokeSteps.i_open_start_page();
        smokeSteps.i_insert_id(id);
        smokeSteps.i_click_upload_id();
        verificationSteps.i_see_chart_results();
    }

    @Test
    public void check_raw_data_link_test() {
        String id = "420beefe-4643-4e7d-88ff-171ae1e2e73b";
        smokeSteps.i_open_start_page();
        smokeSteps.i_insert_id(id);
        smokeSteps.i_click_upload_id();
        smokeSteps.i_click_raw_data_link();
        verificationSteps.i_see_data_in_json_format();
    }

    @Test
    public void open_help_page_test() {
        String pageTitle = "Surveylyzer - Help";
        smokeSteps.i_open_start_page();
        smokeSteps.click_help_icon();
        verificationSteps.i_see_help_page(pageTitle);
    }

}

