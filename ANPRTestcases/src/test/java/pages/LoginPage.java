package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

public class LoginPage {
    WebDriver driver;

    @FindBy(id = "username") WebElement usernameField;
    @FindBy(id = "password") WebElement passwordField;
    @FindBy(id = "loginBtn") WebElement loginButton;
    @FindBy(id = "eyeIcon") WebElement eyeIcon;
    @FindBy(id = "errorMsg") WebElement errorMessage; // Add locator if error message shows

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void enterUsername(String username) {
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLogin() {
        loginButton.click();
    }

    public String getPasswordFieldType() {
        return passwordField.getAttribute("type");
    }

    public void togglePasswordVisibility() {
        eyeIcon.click();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
