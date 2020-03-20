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

    public void should_see_results(String results) {
        WebElement resultsText = find(By.cssSelector(".item.md.ion-focusable.hydrated"));
        waitFor(resultsText);
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(resultsText.getText()).isEqualTo(results);
        soft.assertAll();
    }
}

