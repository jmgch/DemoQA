package com.garciachicojm.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class DemoQA {
	public static final List<String> USERS = Collections
			.unmodifiableList(Arrays.asList("Jan van Dam", "Chack Norris",
					"Klark n Kent", "John Daw", "Bat Man", "Tim Los",
					"Dave o Core", "Pay Pal", "Lazy Cat", "Jack & Johnes"));

	public static final String URL = "http://demoqa.com/registration/";

	public static ArrayList<Integer> NUMBERS = new ArrayList<Integer>() {
		{
			add(0);
			add(1);
			add(2);
			add(3);
			add(4);
			add(5);
			add(6);
			add(7);
			add(8);
			add(9);
		}
	};

	static FirefoxDriver driver = new FirefoxDriver();

	public static ArrayList<String> REGISTERED_USERS = new ArrayList<String>();

	public static Random rand = new Random();

	@Test
	public static void main(String[] args) {

		driver.get(URL);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		checkURL(URL);

		for (int i = 0; i < 5; i++) {
			registerUsersInURL(getUser());
		}

		printPendingUsers();

		driver.quit();

	}

	private static void checkURL(String url) {
		String currentURL = driver.getCurrentUrl();
		Assert.assertEquals("http://demoqa.com/registration/", currentURL);
	}

	private static String getUser() {
		String name = getRandomUser();
		String firstName = "";
		String secondName = "";
		if (name.split(" ").length == 2) {
			firstName = name.split(" ")[0];
			secondName = name.split(" ")[1];
		} else if (name.split(" ").length == 3) {
			firstName = name.split(" ")[0];
			secondName = name.split(" ")[2];
		}
		return firstName + " " + secondName;
	}

	private static String getRandomUser() {
		int number = 0;
		String name = "";

		number = rand.nextInt(NUMBERS.size());
		System.out.println("User: " + USERS.get(NUMBERS.get(number))
				+ " is going to be registered");
		name = USERS.get(NUMBERS.get(number));
		NUMBERS.remove(number);
		REGISTERED_USERS.add(name);
		return name;
	}

	private static void registerUsersInURL(String user) {

		String firstName = user.split(" ")[0];
		String lastName = user.split(" ")[1];

		try {
			// Insert First Name
			WebElement first = driver.findElement(By.name("first_name"));
			first.clear();
			first.sendKeys(firstName);

			// Insert Last Name
			WebElement second = driver.findElement(By.name("last_name"));
			second.clear();
			second.sendKeys(lastName);

			// Insert random Marital Status
			List<WebElement> radioButtons = driver.findElements(By
					.cssSelector("input[class='input_fields  radio_fields']"));
			radioButtons.get(rand.nextInt(radioButtons.size())).click();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

			// Insert random Hobby
			List<WebElement> checkBoxes = driver
					.findElements(By
							.cssSelector("input[class='input_fields  piereg_validate[required] radio_fields']"));
			for (int i = 0; i < checkBoxes.size(); i++) {
				if (checkBoxes.get(i).isSelected())	checkBoxes.get(i).click();
			}
			checkBoxes.get(rand.nextInt(checkBoxes.size())).click();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

			// Insert random Country
			randomDropdownSelection("dropdown_7");

			// Insert random date of birth
			randomDropdownSelection("mm_date_8");
			randomDropdownSelection("dd_date_8");
			randomDropdownSelection("yy_date_8");

			// Insert Phone Number
			WebElement number = driver.findElement(By.name("phone_9"));
			number.clear();
			number.sendKeys(randomPhoneNumber());

			// Insert User Name
			WebElement userElement = driver.findElement(By.name("username"));
			userElement.clear();
			userElement.sendKeys(firstName + lastName);

			// Insert email
			WebElement email = driver.findElement(By.name("e_mail"));
			email.clear();
			email.sendKeys(firstName + lastName + "@" + "gmail.com");

			// Insert password
			WebElement pass = driver.findElement(By.id("password_2"));
			pass.clear();
			pass.sendKeys("pass" + firstName + lastName);

			// Confirm password
			WebElement confPass = driver.findElement(By
					.id("confirm_password_password_2"));
			confPass.clear();
			confPass.sendKeys("pass" + firstName + lastName);

			// Submit
			WebElement submit = driver.findElement(By.name("pie_submit"));
			submit.click();

			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} catch (Exception e) {
			Assert.fail();
			System.out.println("Error while registering a user: " + e);
		}
	}

	private static void randomDropdownSelection(String id) {
		Select dropdown = new Select(driver.findElement(By.id(id)));
		List<WebElement> countries = dropdown.getOptions();
		ArrayList<String> list = new ArrayList<String>();
		for (WebElement element : countries) {
			list.add(element.getText());
		}
		dropdown.selectByVisibleText(list.get(rand.nextInt(list.size())));
	}

	private static String randomPhoneNumber() {
		return String.valueOf(rand.nextInt(10)) + String.valueOf(6)
				+ String.valueOf(rand.nextInt(10))
				+ String.valueOf(rand.nextInt(10))
				+ String.valueOf(rand.nextInt(10))
				+ String.valueOf(rand.nextInt(10))
				+ String.valueOf(rand.nextInt(10))
				+ String.valueOf(rand.nextInt(10))
				+ String.valueOf(rand.nextInt(10))
				+ String.valueOf(rand.nextInt(10));
	}

	private static void printPendingUsers() {
		for (String user : USERS) {
			if (!REGISTERED_USERS.contains(user)) {
				System.out.println("Not registered user: " + user);
			}
		}
	}
}
