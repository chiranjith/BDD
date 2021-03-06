package com.mphasis.automation.ApplicationInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.StringArray;

public class DriverFunctions {

	private WebDriver driver;
	private Actions builder;
	public static final Logger logger = LoggerFactory
			.getLogger(DriverFunctions.class);

	/**
	 * Constructor sets the local driver object
	 * 
	 * 
	 */
	public DriverFunctions(WebDriver driver) {
		setDriver(driver);
	}

	private WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
		builder = new Actions(driver);
	}

	/**
	 * Navigates to specified URL.
	 * 
	 * @param URL
	 *            : URL To Navigate To
	 * @param objectInPage
	 *            : (Optional) Any visible Object in page after the page is
	 *            loaded.
	 * @param timeOut
	 *            : Max time in seconds to wait for the Object to appear.
	 * @throws TimeoutException
	 *             : Exception thrown when the page is not loaded in given time
	 *             frame
	 * @return
	 */

	public void navigateToUrl(String URL, Object objectInPage, int timeOut)
			throws TimeoutException {
		System.out.println("Open URL: " + URL);
		driver.get(URL);
		logger.info("Open URL: " + URL);
		
		if (objectInPage != null && objectInPage instanceof By) {
			logger.debug("Waiting for Object " + objectInPage.toString()
					+ " for maximum of " + Integer.toString(timeOut)
					+ " seconds.");
			waitforElement(objectInPage, timeOut);
			logger.debug("Object " + objectInPage.toString()
					+ " found. Page Opened successfully.");
		}

	}

	/**
	 * Navigates to specified URL.
	 * 
	 * @param objectToWaitFor
	 *            : Object to wait for loaded.
	 * @param timeOut
	 *            : Max time in seconds to wait for the Object to appear.
	 * @throws TimeoutException
	 *             : Exception thrown when the object is not visible in given
	 *             time frame
	 * @return boolean
	 */

	public boolean waitforElement(Object objectToWaitFor, int timeOut) {
		boolean flag = false;
		try {
			if (objectToWaitFor != null && objectToWaitFor instanceof By) {
				logger.debug("Waiting for Object " + objectToWaitFor.toString()
						+ " for maximum of " + Integer.toString(timeOut)
						+ " seconds.");
				(new WebDriverWait(driver, timeOut)).until(ExpectedConditions
						.presenceOfElementLocated((By) objectToWaitFor));
				logger.debug("Object " + objectToWaitFor.toString() + " found.");
				flag = true;
			}
		} catch (TimeoutException ex) {
			flag = false;
		}
		return flag;
	}

	/**
	 * Clicks a given webelement
	 * 
	 * @param locator
	 *            : Any Locator of the web element to be clicked
	 * 
	 */
	public void click(By locator) {

		WebElement clickableElement = getElement(locator);
		click(clickableElement);

	}

	/**
	 * Overloaded click functions Clicks a given webelement
	 * 
	 * @param clickableElement
	 *            : The web element object to be clicked
	 * 
	 */

	public void click(WebElement clickableElement) {
		try {

			clickableElement.isEnabled();
			clickableElement.click();
			logger.info("Clicked into locator - " + clickableElement.toString());
		} catch (Exception ex) {
			logger.error(
					"Error while trying to click "
							+ clickableElement.toString() + " : {}",
					ex.getMessage());
		}
	}

	/**
	 * Gets text from a given webelement when a locator is passed to it.
	 * 
	 * @param locator
	 *            : Any Locator of the web element, text for which is to be read
	 * 
	 */
	public String getText(By locator) {

		WebElement element = getElement(locator);
		return getText(element);

	}

	/**
	 * Overloaded getText functions Gets text from a given webelement
	 * 
	 * @param element
	 *            : The web element object whose text is to be read
	 * 
	 */

	public String getText(WebElement element) {
		String text;
		try {

			element.isEnabled();
			text = element.getText();
			
			logger.info("Text in element with locator - " + element.toString()
					+ " is: " + text);

		} catch (Exception ex) {
			logger.error("Error while trying to get text from element "
					+ element.toString() + " : {}", ex.getMessage());
			text = null;
		}
		return text;
	}
	
	/**
	 * Get Attribute from a given webelement
	 * 
	 * @param locator
	 *            : Locator for which the attribute value is to be fetched
	 * @param attributeName
	 * 			  : Attribute for which the value is to be fetched
	 */
	public String getAttribute(By locator,String attributeName) {

		WebElement element = getElement(locator);
		return getAttribute(element, attributeName);

	}
	
	
	/**
	 * Get Attribute from a given webelement (Overloaded function)
	 * 
	 * @param element
	 *            : Element for which the attribute value is to be fetched
	 * @param attributeName
	 * 			  : Attribute for which the value is to be fetched
	 */

	public String getAttribute(WebElement element,String attributeName) {
		String text;
		try {

			text = element.getAttribute(attributeName);
			
			logger.info("Value for the attribute "+attributeName+ " is "+ text);
					

		} catch (Exception ex) {
			logger.error("Error while trying to get attribute value from element "
					+ element.toString() + " : {}", ex.getMessage());
			text = null;
		}
		return text;
	}
	
	
	/**
	 * Sends text to a webelement specified by locator parameter
	 * 
	 * @param locator
	 *            : Locator of the web element object to be clicked
	 * @param text
	 *            : Text to be send to given web element
	 */

	public void sendText(By locator, String text) {
		WebElement editableElement = getElement(locator);
		try {
			clear(editableElement);
			editableElement.sendKeys(text);
			logger.info("Send Text " + text + " into locator- "
					+ locator.toString());
		} catch (Exception ex) {
			logger.error(
					"Error while sending text " + text + " to "
							+ locator.toString() + ": {}", ex.getMessage());
		}

	}

	/**
	 * Select item from the list box locator parameter
	 * 
	 * @param locator
	 *            : Locator of the list box object from which data is to be
	 *            selected
	 * @param option
	 *            : Option to be selected from list
	 */

	public void selectFromList(By locator, String option) {
		WebElement dropDownElement = getElement(locator);
		try {
			Select dropdown = new Select(dropDownElement);
			dropdown.selectByVisibleText(option);
			logger.info("Selected  Item " + option + " from locator- "
					+ locator.toString());
		} catch (Exception ex) {
			logger.error("Error while selecting " + option
					+ " from drop down list - " + locator.toString() + ": {}",
					ex.getMessage());
		}

	}

	/**
	 * Select item from the list box locator parameter
	 * 
	 * @param locator
	 *            : Locator of the list box object from which data is to be
	 *            selected
	 * @param option
	 *            : Option to be selected from list
	 */

	public String getSelectedValueFromDropDown(By locator) {
		WebElement dropDownElement = getElement(locator);
		String defaultValue = null;
		try {
			Select dropdown = new Select(dropDownElement);
			defaultValue = dropdown.getFirstSelectedOption().getText();
			logger.info("Current Value is " + defaultValue
					+ " in drop down list - locator- " + locator.toString());

		} catch (Exception ex) {
			logger.error(
					"Error while fetching default value from drop down list - "
							+ locator.toString() + ": {}", ex.getMessage());
		}
		return defaultValue;

	}

	/**
	 * Returns an array of options available in list box
	 * 
	 * @param locator
	 *            : Locator of the list box object
	 * @return {@link StringArray}
	 */

	public ArrayList<String> returnListItems(By locator) {
		WebElement dropDownElement = getElement(locator);
		List<WebElement> options;
		ArrayList<String> optionName = new ArrayList<String>();
		int count;
		try {
			Select dropdown = new Select(dropDownElement);
			options = dropdown.getOptions();
			for (count = 0; count < options.size(); count++) {
				optionName.add(options.get(count).getAttribute("value"));
				logger.info("Option " + count + 1 + " is "
						+ options.get(count).getAttribute("value"));
			}
			return optionName;

		} catch (Exception ex) {
			logger.error("Error while retreiving values from drop down list - "
					+ locator.toString() + ": {}", ex.getMessage());
		}

		return optionName;
	}

	/**
	 * Clears the text in a given webelement
	 * 
	 * @param locator
	 *            : Any Locator of the web element to be clicked
	 * 
	 */
	public void clear(By locator) {

		WebElement clearElement = getElement(locator);
		clear(clearElement);
	}

	/**
	 * Overloaded clear function, clear text in a given webelement
	 * 
	 * @param elementToBeCleared
	 *            : The web element object, text of which is to be cleared
	 * 
	 */

	public void clear(WebElement elementToBeCleared) {
		try {

			elementToBeCleared.isDisplayed();
			elementToBeCleared.clear();
			logger.info("Cleared the text in locator - "
					+ elementToBeCleared.toString());
		} catch (Exception ex) {
			logger.error("Error while trying to clear text in "
					+ elementToBeCleared.toString() + ": {}", ex.getMessage());
		}
	}

	/**
	 * Function to do mouse hover
	 * 
	 * @param moveToElementLocator
	 *            : The web element locator, on which mouse hover is to be
	 *            performed
	 * 
	 */

	public void mouseHover(By moveToElementLocator) {
		WebElement moveToElement = getElement(moveToElementLocator);
		mouseHover(moveToElement);
	}

	/**
	 * Overloaded Function to do mouse hover
	 * 
	 * @param moveToElement
	 *            : The web element object, on which mouse hover is to be
	 *            performed
	 * 
	 */

	public void mouseHover(WebElement moveToElement) {
		try {
			builder.moveToElement(moveToElement).build().perform();
			logger.info("Mouse hover on element" + moveToElement.toString());
		} catch (Exception ex) {
			logger.error("Error while trying to move mouse over "
					+ moveToElement.toString() + ": {}", ex.getMessage());
		}
	}

	/**
	 * Returns the webelement object when a lcoator is passed
	 * 
	 * @param clickableElement
	 *            : The web element object to be clicked
	 * 
	 */

	private WebElement getElement(By locator) {
		return driver.findElement(locator);
	}

	public byte[] getscreenShot() throws IOException {

		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		// DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		// Date date = new Date();
		// System.out.println(dateFormat.format(date));
		// File temp = ((TakesScreenshot)
		// driver).getScreenshotAs(OutputType.FILE);
		// FileUtils.copyFile(temp, new File(dateFormat.format(date)));
		// InputStream screenshotStream = null;
		// try {
		// screenshotStream = new FileInputStream(temp);
		//
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// return IOUtils.toByteArray(screenshotStream);

	}
}