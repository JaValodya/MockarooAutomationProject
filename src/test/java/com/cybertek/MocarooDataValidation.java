package com.cybertek;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MocarooDataValidation {

	static WebDriver driver;
	String expectedTitle1 = "mockaroo";
	String expectedTitle2 = "realistic data generator";
	String expectedLabel1 = "Field Name";
	String expectedLabel2 = "Type";
	String expectedLabel3 = "Options";
	String defaultNumberOfRows = "1000";
	String defaultSelectionFormat = "CSV";
	String defaultLineEnding = "Unix (LF)";

	String FieldName1 = "City";
	String FieldName2 = "Country";

	String expextedChooseATypeTitle = "Choose a Type";
	String expectedRecordsCount = "1000";

	static int count = 7;

	Set<String> uniqueCitiesListExpected = new HashSet<>();
	Set<String> uniqueCountriesListExpected = new HashSet<>();

	int uniqueCitiesCount = 0;
	int uniqueCountriesCount = 0;

	public static void removeAllFields() {

		List<WebElement> fieldList = driver
				.findElements(By.xpath("//a[@class = 'close remove-field remove_nested_fields']"));
		for (WebElement temp : fieldList) {
			temp.click();
		}
	}

	// this method will add one field
	public static void addField() {

		driver.findElement(By.xpath("//a[@class = 'btn btn-default add-column-btn add_nested_fields']")).click();

	}

	public static void addField(String FieldName) throws InterruptedException {
		addField();
		driver.findElement(By.xpath("//div[@id='fields']/div[" + count + "]/div[2]/input")).sendKeys(FieldName);// ????????!!!!!!!!
		driver.findElement(By.xpath("//div[@id='fields']/div[" + count + "]/div[3]/input[3]")).click();
		Assert.assertTrue(driver.findElement(By.xpath("//div[@id = 'type_dialog_wrap']")).isDisplayed());// what o do in
		Thread.sleep(1000);// what other methods of sleep can be used?
		driver.findElement(By.id("type_search_field")).sendKeys(FieldName);
		driver.findElement(By.xpath("//div[.= '" + FieldName + "' ]")).click();
		count++;// this will change the xpath to the new added field
	}

	public static void downloadData() {
		driver.findElement(By.xpath("//button[@id = 'download']")).click();
		// FileReader fr = new FileReader("MOCK_DATA(2).csv");//how to update all the
		// time
		// BufferedReader br = new BufferedReader(fr);
	}

	public static BufferedReader readData() {

		FileReader fr = null;
		try {
			fr = new FileReader("MOCK_DATA (2).csv");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // how to update all the time
		BufferedReader br = new BufferedReader(fr);

		return br;

	}

	@BeforeClass // runs once for all tests
	public void setUp() {
		System.out.println("Setting up WebDriver in BeforeClass...");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get("https://mockaroo.com/");
		removeAllFields();
	}

	// @BeforeMethod // runs before each @Test
	// public void navigateToHomePage() {
	// // System.out.println("Navigating to homepage in @BeforeMethod....");
	//
	//
	//
	// }

	// 4. Assert Mockaroo and realistic data generator are displayed use Xpath
	@Test
	public void titleCheck() {
		String actualTitle1 = driver.findElement(By.xpath("//div[@class ='brand']")).getText();
		String actualTitle2 = driver.findElement(By.xpath("//div[@class ='tagline']")).getText();
		Assert.assertTrue(actualTitle1.equals(expectedTitle1));
		Assert.assertTrue(actualTitle2.equals(expectedTitle2));
	}

	// 6. Assert that ‘Field Name’ , ‘Type’, ‘Options’ labels are displayed
	@Test
	public void labelsCheck() {

		Assert.assertTrue((driver.findElement(By.xpath("//div[@class ='column column-header column-name']")).getText())
				.equals(expectedLabel1));
		Assert.assertTrue((driver.findElement(By.xpath("//div[@class ='column column-header column-type']")).getText())
				.equals(expectedLabel2));
		Assert.assertTrue(
				(driver.findElement(By.xpath("//div[@class ='column column-header column-options']")).getText())
						.equals(expectedLabel3));
	}

	// 7. Assert that ‘Add another field’ button is enabled.
	@Test
	public void addAnotherFieldCheck() {

		Assert.assertTrue(driver
				.findElement(By.xpath("//a[@class = 'btn btn-default add-column-btn add_nested_fields']")).isEnabled());

	}

	// 8. Assert that default number of rows is 1000.
	@Test
	public void defaultNumberOfRowsCheck() {

		Assert.assertTrue(
				(driver.findElement(By.xpath("//input[@class = 'medium-number form-control']")).getAttribute("value"))
						.equals(defaultNumberOfRows));

	}

	// 9. Assert that default format selection is CSV
	@Test
	public void defaultSelectionFormatCheck() {

		Assert.assertTrue(
				(driver.findElement(By.xpath("//option[@value = 'csv']")).getText()).equals(defaultSelectionFormat));

	}

	// 10. Assert that Line Ending is Unix(LF)
	@Test
	public void defaultLineEndingCheck() {

		Assert.assertTrue(
				(driver.findElement(By.xpath("//option[@value = 'unix']")).getText()).equals(defaultLineEnding));

	}

	// 11. Assert that header checkbox is checked and BOM is unchecked
	@Test
	public void defaultHeaderBOMCheck() {

		Assert.assertTrue((driver.findElement(By.id("schema_include_header")).isSelected()));
		System.out.println(driver.findElement(By.id("schema_include_header")).isSelected());
		Assert.assertFalse((driver.findElement(By.id("schema_bom")).isSelected()));
		System.out.println(driver.findElement(By.id("schema_bom")).isSelected());
	}

	// 12. Click on ‘Add another field’ and enter name “City”
	// 13. Click on Choose type and assert that Choose a Type dialog box is
	// displayed.
	// 14. Search for “city” and click on City on search results.
	// 15. Repeat steps 12-14 with field name and type “Country”
	// 16. Click on Download Data.
	// 17. Open the downloaded file using BufferedReader.
	// 18. Assert that first row is matching with Field names that we selected.
	@Test
	public void firstRowMatchChek() throws InterruptedException {
		// addField();
		addField(FieldName1);
		Thread.sleep(1000);// ????????????????
		addField(FieldName2);
		Thread.sleep(1000);
		// driver.findElement(By.xpath("//button[@id = 'download']")).click();
		downloadData();
		BufferedReader br = readData();
		// FileReader fr = null;
		// try {
		// fr = new FileReader("MOCK_DATA (2).csv");
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } // how to update all the time
		// BufferedReader br = new BufferedReader(fr);
		String firstline = null;
		try {
			firstline = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(firstline);

		Assert.assertTrue((firstline.replace(",", "")).equalsIgnoreCase(FieldName1 + FieldName2));
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 19. Assert that there are 1000 records

	@Test
	public void recordsCountCheck() {
		BufferedReader br = readData();
		String line;
		int actualRecordsCount = -1; // -1 - because we don't need first line to be count / ??????
		try {
			while ((line = br.readLine()) != null) {
				actualRecordsCount++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(actualRecordsCount);
		Assert.assertTrue(String.valueOf(actualRecordsCount++).equals(expectedRecordsCount));
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 20. From file add all Cities to Cities ArrayList
	// 21. Add all countries to Countries ArrayList
	// 22. Sort all cities and find the city with the longest name and shortest name
	/*
	 * 23. In Countries ArrayList, find how many times each Country is mentioned.
	 * and print out ex: Indonesia-10 Russia-7 etc
	 */

	@Test // what to do with this code
	public void closeUp() {
		BufferedReader br = readData();
		List<String> citiesList = new ArrayList<>();
		List<String> countriesList = new ArrayList<>();
		String line = "";
		try {
			while ((line = br.readLine()) != null) {
				citiesList.add(line.substring(0, line.indexOf(",")));
				countriesList.add(line.substring(line.indexOf(",") + 1, line.length()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// removes the first row
		citiesList.remove(0);
		countriesList.remove(0);

		// sort the list
		Collections.sort(citiesList);
		Collections.sort(countriesList);

		String shortestNameCity = citiesList.get(0);
		String longestNameCity = citiesList.get(0);
		for (int i = 0; i < citiesList.size(); i++) {
			if (shortestNameCity.length() > citiesList.get(i).length()) {
				shortestNameCity = citiesList.get(i);
			} else if (longestNameCity.length() < citiesList.get(i).length()) {
				longestNameCity = citiesList.get(i);
			}

		}
		System.out.println("Shortest city name: " + shortestNameCity);
		System.out.println("Longest city name: " + longestNameCity);

		// count Unique Countries names
		List<String> uniqueCountriesList = new ArrayList<>();
		// int count1 = 0;
		for (int i = 0; i < countriesList.size();) {
			String country = countriesList.get(i);
			int count = 0;
			while (country.equals(countriesList.get(i))) {
				count++;
				// count1++;
				i++;
				if (i == countriesList.size()) {
					break;
				}
			}
			uniqueCountriesList.add(country + "-" + count);

		}
		// System.out.println(count1);
		// for (String temp : uniqueCountriesList) {
		// System.out.println(temp);
		// }

		// count Unique Cities names
		List<String> uniqueCitiesList = new ArrayList<>();
		// int count1 = 0;
		for (int i = 0; i < citiesList.size();) {
			String city = citiesList.get(i);
			int count = 0;
			while (city.equals(citiesList.get(i))) {
				count++;
				// count1++;
				i++;
				if (i == citiesList.size()) {
					break;
				}
			}
			uniqueCitiesList.add(city + "-" + count);

		}

		// for (String temp : uniqueCitiesList) {
		// System.out.println(temp);
		// }

		// 24. From file add all Cities to citiesSet HashSet
		for (String temp : citiesList) {
			uniqueCitiesListExpected.add(temp);
		}
		uniqueCitiesListExpected.size();
		Assert.assertTrue(uniqueCitiesList.size() == uniqueCitiesListExpected.size());

		for (String temp : countriesList) {
			uniqueCountriesListExpected.add(temp);
		}
		uniqueCountriesListExpected.size();
		Assert.assertTrue(uniqueCountriesList.size() == uniqueCountriesListExpected.size());
	}

	// 24. From file add all Cities to citiesSet HashSet

	// 25. Count how many unique cities are in Cities ArrayList and assert that it
	// is matching with the count of citiesSet HashSet.
	// 26. Add all Countries to countrySet HashSet
	/*
	 * 27. Count how many unique cities are in Countries ArrayList and assert that
	 * it is matching with the count of countrySet HashSet.
	 */

	// 28. Push the code to any GitHub repo that you have and submit the url
}
