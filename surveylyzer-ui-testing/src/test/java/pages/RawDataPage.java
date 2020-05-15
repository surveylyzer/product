package pages;

import net.serenitybdd.core.pages.PageObject;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RawDataPage extends PageObject {


    public void i_see_data_in_json_format() {
        WebElement json = find(By.cssSelector("#json"));
        waitABit(1000);
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(json).isNotNull();
        soft.assertAll();

    }
}
