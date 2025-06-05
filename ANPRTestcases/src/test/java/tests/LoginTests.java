package tests;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;
import utils.BaseTest;

public class LoginTests extends BaseTest {
    LoginPage loginPage;

    @BeforeMethod
    public void setUpTest() {
        driver.get("http://localhost:4777/login"); 
        loginPage = new LoginPage(driver);
    }

    @Test
    public void loginWithValidUsernameAndInvalidPassword() {
        loginPage.enterUsername("admin");
        loginPage.enterPassword("wrongPassword");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Should show error for invalid password");
    }

    @Test
    public void loginWithInvalidUsernameAndValidPassword() {
        loginPage.enterUsername("wronguser");
        loginPage.enterPassword("Admin@12345");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Should show error for invalid username");
    }

    @Test
    public void loginOnlyWithUsername() {
        loginPage.enterUsername("admin");
        loginPage.enterPassword("");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Should show error for missing password");
    }

    @Test
    public void loginOnlyWithPassword() {
        loginPage.enterUsername("");
        loginPage.enterPassword("Admin@12345");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Should show error for missing username");
    }

    @Test
    public void passwordVisibilityToggleDoesNotWork() {
        loginPage.enterPassword("Admin@12345");
        String typeBefore = loginPage.getPasswordFieldType(); // should be "password"
        loginPage.togglePasswordVisibility();
        String typeAfter = loginPage.getPasswordFieldType(); // remains "password"
        Assert.assertEquals(typeAfter, "password", "Password visibility toggle is not working");
    }

    @Test
    public void loginWithUnregisteredUserAndRandomPassword() {
        loginPage.enterUsername("ghost123");
        loginPage.enterPassword("abc@123");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Should show error for unregistered user");
    }

    @Test
    public void loginWithInvalidCharactersInUsername() {
        loginPage.enterUsername("@@@###");
        loginPage.enterPassword("Admin@12345");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Invalid characters in username should be rejected");
    }

    @Test
    public void accountShouldLockAfterMultipleFailedLogins() {
        for (int i = 0; i < 5; i++) {
            loginPage.enterUsername("admin");
            loginPage.enterPassword("wrongPass" + i);
            loginPage.clickLogin();
        }
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Should show lockout or error after repeated failures");
    }

    @Test
    public void forgotPasswordLinkShouldBePresent() {
        Assert.assertTrue(driver.getPageSource().contains("Forgot Password"), "Forgot Password link is missing");
    }

    @Test
    public void loginButtonNotClickableWithEmptyFields() {
        loginPage.enterUsername("");
        loginPage.enterPassword("");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Login should not proceed with empty fields");
    }

    @Test
    public void validatePasswordFieldLength() {
        String longPassword = "a".repeat(300);
        loginPage.enterUsername("admin");
        loginPage.enterPassword(longPassword);
        loginPage.clickLogin();
        Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"), "App incorrectly accepts long password");
    }

    @Test
    public void loginUsingEmailInsteadOfUsername() {
        loginPage.enterUsername("admin@example.com");
        loginPage.enterPassword("Admin@12345");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Should not accept email in username field");
    }

    @Test
    public void caseSensitivityCheck() {
        loginPage.enterUsername("Admin"); // should be "admin"
        loginPage.enterPassword("Admin@12345");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Login should be case-sensitive");
    }

    @Test
    public void loginWithLongStringsAsInput() {
        String longString = "a".repeat(1000);
        loginPage.enterUsername(longString);
        loginPage.enterPassword(longString);
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Should reject excessively long input strings");
    }

    @Test
    public void loginWithHtmlTagsAsInput() {
        loginPage.enterUsername("<script>alert(1)</script>");
        loginPage.enterPassword("<b>test</b>");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "HTML tags in inputs should not be accepted");
    }

    @Test
    public void loginWithExpiredAccount() {
        loginPage.enterUsername("expiredUser");  
        loginPage.enterPassword("Admin@12345");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Expired account should not be able to log in");
    }

    @Test
    public void checkTypographyOfLoginPage() {
        String fontFamily = driver.findElement(By.id("username")).getCssValue("font-family");
        Assert.assertTrue(fontFamily.contains("Arial") || fontFamily.contains("Roboto"),
                "Typography is not as expected");
    }
}
