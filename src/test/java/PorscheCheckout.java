import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class PorscheCheckout {

	public static void main(String[] args) throws InterruptedException {
		WebDriverManager.chromedriver().setup();

		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().fullscreen();
		driver.get("https://www.porsche.com/usa/modelstart/");
		driver.findElement(By.partialLinkText("718")).click();
		String price = driver.findElement(By.className("m-14-model-price")).getText();

		// double caymanPrice2 = ConverttoDouble(price);
		// System.out.println(caymanPrice2);
		double caymanPrice = Double.parseDouble(price.replace(",", "").substring(7, 15));
		System.out.println(caymanPrice); // 4

		WebElement button = driver.findElement(By.xpath("//*[@id='m982120']/div[2]/div/a/span")); // 5
		button.click(); // click next page

		Set<String> mm = driver.getWindowHandles();
		Iterator<String> it = mm.iterator();
		String parent = it.next();
		String child = it.next();

		driver.switchTo().window(child);

		String handle = driver.getWindowHandle();

		// price at the second window

		String basePrice2 = driver.findElement(By.xpath("//section[@id='s_price']//div[@class='ccaPrice'][contains(text(),\"$56\")]")).getText();
		System.out.println("Base price at second page: " +basePrice2);
		double basePrice2d = Double.parseDouble(basePrice2.replace(",", "").replace("$", ""));
		System.out.println(basePrice2d);

		// 6
		if (caymanPrice == basePrice2d) {
			System.out.println("Cayman 718 price is verified");
		} else {
			System.out.println("Cayman 718 price is not verified");
		}

		// 7 Verify that Price for Equipment is 0

		String priceforEquipment = driver.findElement(By.xpath("//*[@id='s_price']/div[1]/div[2]/div[2]")).getText();
		double priceforEquipmentd = Double.parseDouble(priceforEquipment.replace("$", ""));

		if (priceforEquipmentd == 0.0) {
			System.out.println("Price for Equipment is " + priceforEquipmentd + " verified");
		} else {
			System.out.println("Price for Equipment is not " + priceforEquipmentd + " verified");
		}

		// 8. Verify that total price is the sum of base price + Delivery, Processing
		// and Handling Fee

		String totalPrice = driver.findElement(By.xpath("//*[@id=\'s_price\']/div[1]/div[4]/div[2]")).getText();
		double totalPricetd = Double.parseDouble(totalPrice.replace("$", "").replace(",", ""));

		String handlingFee = driver.findElement(By.xpath("//*[@id=\'s_price\']/div[1]/div[3]/div[2]")).getText();
		double handlingFeed = Double.parseDouble(handlingFee.replace("$", "").replace(",", ""));

		if (totalPricetd == (priceforEquipmentd + handlingFeed + basePrice2d)) {
			System.out.println("Total Price: " + totalPricetd + " equals to price for Equipment: " + priceforEquipmentd
					+ "+ Handling Fee: " + handlingFeed + " Base Price: " + basePrice2d);
		} else {
			System.out.println("Total Price: " + totalPricetd + " does not equal to price for Equipment: "
					+ priceforEquipmentd + "+ Handling Fee: " + handlingFeed + " Base Price: " + basePrice2d);
		}

		// 9. Select color “Miami Blue”
		
		driver.findElement(By.xpath("//li[@id='s_exterieur_x_FJ5']")).click();
		//driver.findElement(By.id("s_exterieur_x_FJ5")).click();

		// 10.Verify that Price for Equipment is Equal to Miami Blue price
		priceforEquipment = driver.findElement(By.xpath("//*[@id=\'s_price\']/div[1]/div[2]/div[2]")).getText();
		priceforEquipmentd = Double.parseDouble(priceforEquipment.replace("$", "").replace(",", ""));
		System.out.println("Price For The Blue color Equipment " + priceforEquipmentd);

		// 11.Verify that total price is the sum of base price + Price for Equipment +
		// Delivery, Processing and Handling Fee
		totalPricetd = priceforEquipmentd + handlingFeed + basePrice2d;
		System.out.println("Total Price : " + totalPricetd);

		// 12.Select 20" Carrera Sport Wheels

		driver.findElement(By.xpath("//*[@id=\"s_conf_submenu\"]/div/div")).click(); // Overview menu

		driver.findElement(By.xpath("//*[@id=\"submenu_exterieur_x_AA_submenu_x_IRA\"]/a")).click(); // Wheels menu
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"s_exterieur_x_MXRD\"]/span/span")).click(); // click on the wheel

		// 13.Verify that Price for Equipment is the sum of Miami Blue price + 20"
		// Carrera Sport Wheels

		String wheelPrice = driver.findElement(By.xpath("//*[@id=\"s_exterieur_x_IRA\"]/div[2]/div[1]/div/div[2]"))
				.getText();
		double wheelPriced = Double.parseDouble(wheelPrice.replace("$", "").replace(",", ""));

		String pricewithWheels = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText();
		double pricewithWheelsd = Double.parseDouble(pricewithWheels.replace("$", "").replace(",", ""));

		priceforEquipmentd = priceforEquipmentd + wheelPriced;

		if (pricewithWheelsd == priceforEquipmentd) {
			System.out.println("Total Equipment Price with wheels is equal to: " + pricewithWheelsd);
		} else {
			System.out.println("Total Equipment Price with wheels is not equal to: " + priceforEquipmentd);
		}

		// 14.Verify that total price is the sum of base price + Price for Equipment +
		// Delivery,Processing and Handling Fee
		String TotalpricewithWheels = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]"))
				.getText();
		double totalpricewithWheelsd = Double.parseDouble(TotalpricewithWheels.replace("$", "").replace(",", ""));

		if (totalpricewithWheelsd == priceforEquipmentd + handlingFeed + basePrice2d) {
			System.out.println("Total Price with wheels is equal to: " + totalpricewithWheelsd);
		} else {
			System.out.println(
					"Total Price with wheels is not equal to: " + (priceforEquipmentd + handlingFeed + basePrice2d));
		}

		// 15.Select seats ‘Power Sport Seats (14-way) with Memory Package’
		driver.findElement(By.xpath("//*[@id=\"s_conf_submenu\"]/div/div")).click(); // Overview menu

		driver.findElement(By.xpath("//*[@id=\"submenu_interieur_x_AI_submenu_x_submenu_parent\"]")).click(); // interior
																												// menu
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"submenu_interieur_x_AI_submenu_x_submenu_seats\"]/a")).click(); // seats
																												// menu
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"s_interieur_x_PP06\"]")).click(); // click power seats

		// 16.Verify that Price for Equipment is the sum of Miami Blue price + 20"
		// Carrera Sport
		// Wheels + Power Sport Seats (14-way) with Memory Package

		String seatPrice = driver.findElement(By.xpath("//*[@id=\"seats_73\"]/div[2]/div[1]/div[3]/div")).getText();
		double seatPriced = Double.parseDouble(seatPrice.replace("$", "").replace(",", ""));

		priceforEquipmentd += seatPriced;
		System.out.println("Equipment total with power seats " + priceforEquipmentd);

		// 17.Verify that total price is the sum of base price + Price for Equipment +
		// Delivery, Processing and Handling Fee
		totalpricewithWheelsd += seatPriced;
		System.out.println("Total price with power seats " + totalpricewithWheelsd);
		// 18.Click on Interior Carbon Fiber
		driver.findElement(By.xpath("//*[@id=\"s_conf_submenu\"]/div/div")).click();// Overview menu
		driver.findElement(
				By.xpath("//*[@id=\"submenu_individualization_x_individual_submenu_x_submenu_parent\"]/span")).click();// Options
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"submenu_individualization_x_individual_submenu_x_IIC\"]/a")).click(); // Interior
																														// //
																														// Carbon
																														// Fiber
		Thread.sleep(1000);

		// 19.Select Interior Trim in Carbon Fiber i.c.w. Standard Interior
		driver.findElement(By.xpath("//*[@id=\"vs_table_IIC_x_PEKH_x_c01_PEKH\"]")).click();

		// 20.Verify that Price for Equipment is the sum of Miami Blue price + 20"
		// Carrera Sport
		// Wheels + Power Sport Seats (14-way) with Memory Package + Interior Trim in
		// Carbon Fiber i.c.w. Standard Interior

		String carbonFiber = driver.findElement(By.xpath("//*[@id=\"vs_table_IIC_x_PEKH\"]/div[1]/div[2]/div"))
				.getText();
		double carbonFiberd = Double.parseDouble(carbonFiber.replace("$", "").replace(",", ""));
		priceforEquipmentd += carbonFiberd;

		System.out.println(priceforEquipmentd);

		// 21.Verify that total price is the sum of base price + Price for Equipment
		// +Delivery,Processing and Handling Fee
		totalpricewithWheelsd += carbonFiberd;
		System.out.println("Total price with carbon fiber interior " + totalpricewithWheelsd);

		// 22.Click on Performance
		driver.findElement(By.xpath("//*[@id=\"s_conf_submenu\"]/div/div")).click(); // Overview
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"submenu_individualization_x_individual_submenu_x_IMG\"]/a")).click();// performance
		Thread.sleep(1000);
		// 23.Select 7-speed Porsche Doppelkupplung (PDK)
		driver.findElement(By.xpath("//*[@id=\"vs_table_IMG_x_M250_x_c11_M250\"]")).click();

		String doppelkupplung = driver.findElement(By.xpath("//*[@id=\"vs_table_IMG_x_M250\"]/div[1]/div[2]/div"))
				.getText();
		double doppelkupplungd = Double.parseDouble(doppelkupplung.replace("$", "").replace(",", ""));
		priceforEquipmentd += doppelkupplungd;

		// 24.Select Porsche Ceramic Composite Brakes (PCCB)
		driver.findElement(By.xpath("//*[@id=\"search_x_inp\"]")).sendKeys("porsche ceramic"); // how?
		driver.findElement(By.xpath("//*[@id=\"search_x_M450_x_c94_M450_x_shorttext\"]")).click();

		String brakes = driver.findElement(By.xpath("//*[@id=\"vs_table_IMG_x_M450\"]/div[1]/div[2]/div")).getText();
		double brakesd = Double.parseDouble(brakes.replace("$", "").replace(",", ""));
		priceforEquipmentd += brakesd;

		// 25.Verify that Price for Equipment is the sum of Miami Blue price + 20"
		// Carrera Sport
		// Wheels + Power Sport Seats (14-way) with Memory Package + Interior Trim in
		// Carbon Fiber i.c.w. Standard Interior + 7-speed Porsche Doppelkupplung (PDK)
		// +
		// Porsche Ceramic Composite Brakes (PCCB)

		String finalEquipment = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText();
		double finalEquipmentd = Double.parseDouble(finalEquipment.replace("$", "").replace(",", ""));

		if (priceforEquipmentd == finalEquipmentd) {
			System.out.println("Final Equipment Price is equal to the total: " + priceforEquipmentd);
		} else {
			System.out.println("Final Equipment is not equal to: " + finalEquipmentd);
		}
		// 26.Verify that total price is the sum of base price + Price for Equipment +
		// Delivery,
		// Processing and Handling Fee

		totalpricewithWheelsd += (brakesd + doppelkupplungd);
		if (totalpricewithWheelsd == totalpricewithWheelsd) {
			System.out.println("Final Price is equal to the total: " + totalpricewithWheelsd);
		} else {
			System.out.println("Final is not equal to: " + totalpricewithWheelsd);
		}
		driver.close();
	}

	public static double ConverttoDouble(String s) {
		double dbl = Double.parseDouble(s.replace(",", "").replace("$", ""));
		return dbl;
	}
}
