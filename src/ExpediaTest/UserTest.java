package ExpediaTest;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import Expedia.Booking;
import Expedia.Car;
import Expedia.Discount;
import Expedia.Flight;
import Expedia.Hotel;
import Expedia.ServiceLocator;
import Expedia.User;
public class UserTest
{	
	private User target;
	private final Date StartDate = new Date(2009, 11, 1);
	private final Date EndDate = new Date(2009, 11, 30);
	

	@Before
	public void TestInitialize()
	{
		target = new User("Bob Dole");
	}
	
	@Test
	public void TestThatUserInitializes()
	{
		Assert.assertEquals("Bob Dole", target.Name);
	}
	
	@Test
	public void TestThatUserHasZeroFrequentFlierMilesOnInit()
	{
		Assert.assertEquals(0, target.FrequentFlierMiles());
	}
	
	@Test
	public void TestThatUserCanBookEverything()
	{
		target.book(new Booking[]{new Flight(StartDate, EndDate, 0), new Hotel(5), new Car(3)});
		Assert.assertEquals(3, target.Bookings.size());
	}
	
	@Test
	public void TestThatUserHasFrequentFlierMilesAfterBooking()
	{
		target.book(new Booking[]{new Flight(StartDate, EndDate, 1), new Hotel(5), new Car(3)});
		Assert.assertTrue(0 < target.FrequentFlierMiles());
		Assert.assertEquals(3, target.Bookings.size());
	}
	
	@Test
	public void TestThatUserCanBookAOnlyFlight()
	{
		target.book(new Booking[]{new Flight(StartDate, EndDate, 0)});
		Assert.assertEquals(1, target.Bookings.size());
	}
	
	@Test
	public void TestThatUserCanBookAHotalAndACar()
	{
		target.book(new Booking[]{new Car(5), new Hotel(5)});
		Assert.assertEquals(2, target.Bookings.size());
	}
	
	@Test
	public void TestThatUserHasCorrectNumberOfFrequentFlyerMilesAfterOneFlight()
	{
		target.book(new Booking[]{new Flight(StartDate, EndDate, 500)});
		Assert.assertEquals(500, target.FrequentFlierMiles());
	}
	
	@Test
	public void TestThatUserTotalCostIsCorrect()
	{
		Flight flight = new Flight(StartDate, EndDate, 500);
		target.book(new Booking[]{flight});	
		Assert.assertEquals(flight.getBasePrice(), target.Price(), 0.01);
	}
	
	@Test
	public void TestThatUserTotalCostIsCorrectWhenMoreThanFlights()
	{
		Car car = new Car(5);
		Flight flight = new Flight(StartDate, EndDate, 500);
		target.book(new Booking[]{flight});	
		target.book(new Booking[]{car});
		Assert.assertEquals(flight.getBasePrice() + car.getBasePrice(), target.Price(), 0.01);
	}
	
	@Test
	public void TestDoubleMiles()
	{
		Car car = new Car(5);
		Flight flight = new Flight(StartDate, EndDate, 10000);
		target.bookWithDoubleMiles(new Booking[]{flight});
		Assert.assertEquals(5000, target.bonusFrequentFlierMiles, 0.01);
	}
	@Test
	public void TestDoubleMilesLessThan5000()
	{
		Car car = new Car(5);
		Flight flight = new Flight(StartDate, EndDate, 700);
		target.bookWithDoubleMiles(new Booking[]{flight});
		Assert.assertEquals(700, target.bonusFrequentFlierMiles, 0.01);
	}
	@Test
	public void TestThatDiscountInitializes()
	{
		Discount target = new Discount(0.01, 1);
		ServiceLocator.Instance().AddDiscount(target);
		this.target.book(new Booking[]{new Flight(StartDate, EndDate, 100), new Hotel(5), new Car(3)});
		assertEquals(1024.65,this.target.Price(), 0.01);
	}
	@Test
	public void TestThatDiscountInitializesTermCoverage()
	{
//		if(1.0-discount.ReductionPercent < result && discount.FrequentFlyerMilesCost <= FrequentFlierMiles())
//		{
//			result = 1.0 - discount.ReductionPercent;
//		}
		Discount target = new Discount(0, 1);
		ServiceLocator.Instance().AddDiscount(target);
		this.target.book(new Booking[]{new Flight(StartDate, EndDate, 100), new Hotel(5), new Car(3)});
		assertEquals(1035,this.target.Price(), 0.01);
	}
	
	@After
	public void TearDown()
	{
		target = null; // this is entirely unnecessary.. but I'm just showing a usage of the TearDown method here
	}
}
