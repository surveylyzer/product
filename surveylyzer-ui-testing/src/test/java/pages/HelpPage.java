package pages;

import net.serenitybdd.core.pages.PageObject;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HelpPage extends PageObject {


    public void i_see_help_page(String pageTitle) {
        WebElement currentPageTitle = find(By.cssSelector("#page_title"));
        waitFor(currentPageTitle);
        waitABit(1000);
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(currentPageTitle.getText()).isEqualTo(pageTitle);
        soft.assertAll();
    }
}
