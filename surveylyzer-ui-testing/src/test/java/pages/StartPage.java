package pages;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@DefaultUrl("page:app.url")
public class StartPage extends PageObject {

    public void click_result() {
        WebElement viewResults = find(By.cssSelector("[href='/result']"));
        waitFor(viewResults);
        viewResults.click();
    }

    public void i_click_start_cal() {
        WebElement submit = find(By.cssSelector("#submit"));
        waitFor(submit);
        submit.click();
    }

    public void i_click_upload_id() {
        WebElement uploadId = find(By.cssSelector("#upload_id"));
        waitFor(uploadId);
        uploadId.click();
        waitABit(1000);
    }

    public void i_insert_id(String id) {
        element(By.cssSelector(("ion-input[name='surveyId'] input"))).sendKeys(id);
    }

    public void click_help_icon() {
        WebElement helpIcon = find(By.cssSelector("#help_icon"));
        waitFor(helpIcon);
        helpIcon.click();
        waitABit(1000);
    }

    public void seeAlert(String alertSubtitle, String alertMessage, String num) {
        WebElement alertContainer = find(By.cssSelector(".alert-wrapper"));
        waitFor(alertContainer);
        String currentSubtitle = alertContainer.findElement(By.cssSelector("#alert-" + num + "-sub-hdr")).getText();
        String currentMessage = alertContainer.findElement(By.cssSelector("#alert-" + num + "-msg")).getText();

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(currentSubtitle).isEqualTo(alertSubtitle);
        soft.assertThat(currentMessage).isEqualTo(alertMessage);
        soft.assertAll();
    }


}

