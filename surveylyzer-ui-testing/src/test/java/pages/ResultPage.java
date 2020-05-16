package pages;

import net.serenitybdd.core.pages.PageObject;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ResultPage extends PageObject {

    public void seeChartResults() {
        WebElement charts = find(By.cssSelector("#reactgooglegraph-1"));
        waitABit(500);
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(charts).isNotNull();
        soft.assertAll();

    }

    public void i_click_raw_data_link() {
        WebElement rawDataLink = find(By.cssSelector("#raw_data_link"));
        rawDataLink.click();
        waitABit(1000);
    }
}
