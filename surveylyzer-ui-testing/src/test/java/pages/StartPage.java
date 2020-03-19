package pages;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@DefaultUrl("page:app.url")
public class StartPage extends PageObject {

    public void should_see_header_text(String title) {
        WebElement titleCurrent = find(By.cssSelector("h2"));
        waitFor(titleCurrent);
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(titleCurrent.getText()).isEqualTo(title);
        soft.assertAll();
    }
}

