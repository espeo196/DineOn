package uw.cse.dineon.restaurant.test;

import java.util.ArrayList;
import java.util.List;

import uw.cse.dineon.library.CurrentOrderItem;
import uw.cse.dineon.library.CustomerRequest;
import uw.cse.dineon.library.DineOnUser;
import uw.cse.dineon.library.DiningSession;
import uw.cse.dineon.library.MenuItem;
import uw.cse.dineon.library.Order;
import uw.cse.dineon.library.Restaurant;
import uw.cse.dineon.restaurant.DineOnRestaurantApplication;
import uw.cse.dineon.restaurant.R;
import uw.cse.dineon.restaurant.active.DiningSessionDetailActivity;
import uw.cse.dineon.restaurant.active.OrderDetailActivity;
import uw.cse.dineon.restaurant.active.RequestDetailActivity;
import uw.cse.dineon.restaurant.active.RequestListFragment;
import uw.cse.dineon.restaurant.active.RestauarantMainActivity;
import android.app.Activity;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Test Class for testing Restaurant main activity
 * @author mhotan
 */
public class RestaurantMainActivityTest extends
ActivityInstrumentationTestCase2<RestauarantMainActivity> {

	private RestauarantMainActivity mActivity;
	private ParseUser mUser;

	private DineOnUser mUI;
	private Restaurant mRestaurant;

	private CustomerRequest mRequest;
	private Order mOrder;
	private DiningSession testSession;

	int orderNum = 100;
	private int WAIT_LOGIN_TIME = 10000;

	private static final String fakeUserName = "fakeLoginName";
	private static final String fakePassword = "fakeLoginPassword";

	public RestaurantMainActivityTest() throws ParseException {
		super(RestauarantMainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		Parse.initialize(getInstrumentation().getTargetContext(), 
				"RUWTM02tSuenJPcHGyZ0foyemuL6fjyiIwlMO0Ul", 
				"wvhUoFw5IudTuKIjpfqQoj8dADTT1vJcJHVFKWtK");

		setActivityInitialTouchMode(false);

		mUser = new ParseUser();
		mUser.setUsername(fakeUserName);
		mUser.setPassword(fakePassword);

		mUI = new DineOnUser(mUser);
		
		// MH Add image
		
		mRestaurant = new Restaurant(mUser);
		mRequest = new CustomerRequest("Me Hungy", mUI.getUserInfo());

		List<CurrentOrderItem> items = new ArrayList<CurrentOrderItem>();
		items.add(new CurrentOrderItem(new MenuItem(123, 1.99, "Yum yums", "description")));
		mOrder = new Order(1, mUI.getUserInfo(), items);
		mOrder.setObjId("435");
		testSession = new DiningSession(1, mUI.getUserInfo(), mRestaurant.getInfo());
		mRestaurant.addCustomerRequest(mRequest);
		mRestaurant.addOrder(mOrder);
		mRestaurant.addDiningSession(testSession);

		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				RestauarantMainActivity.class);
		setActivityIntent(intent);
		
		DineOnRestaurantApplication.logIn(mRestaurant);

		setActivityInitialTouchMode(false);

		mActivity = getActivity();
	}

	@Override
	protected void tearDown() throws Exception {
		mRequest.deleteFromCloud();
		mOrder.deleteFromCloud();
		testSession.deleteFromCloud();
		mUI.deleteFromCloud();
		mRestaurant.deleteFromCloud();
		mActivity.finish();
		super.tearDown();
	}

	/**
	 * Tests for requests page
	 * Whitebox testing
	 */
	public void testOrdersPage() { 
		final android.support.v4.view.ViewPager pager = (android.support.v4.view.ViewPager) 
				mActivity.findViewById(uw.cse.dineon.restaurant.R.id.pager_restaurant_main);
		PagerAdapter adapter = pager.getAdapter();
		assertNotNull(adapter);

		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				pager.setCurrentItem(0); // requests page
			}
			
		});
		
		
		
		getInstrumentation().waitForIdleSync();
		
		assertEquals(0, pager.getCurrentItem());
		
		final View vwTop = mActivity.findViewById(R.id.listitem_order_top);
		
		
		assertNotNull(vwTop);

		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				vwTop.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		
		final View proButton = pager.findFocus().findViewById(uw.cse.dineon.restaurant.R.id.button_proceed);
		assertNotNull(proButton);
		
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				proButton.performClick();
			}
		});
		
		ActivityMonitor monitor = getInstrumentation().addMonitor(
				OrderDetailActivity.class.getName(), null, false);
		
		OrderDetailActivity startedActivity = (OrderDetailActivity) monitor
		        .waitForActivityWithTimeout(WAIT_LOGIN_TIME);
		assertNotNull(startedActivity);
		
		if (startedActivity != null) {
			startedActivity.finish();
		}
		
		
	}

	/**
	 * Tests for requests page
	 * Whitebox testing
	 */
	public void testRequestsPage() {
		final android.support.v4.view.ViewPager pager = (android.support.v4.view.ViewPager) 
				mActivity.findViewById(uw.cse.dineon.restaurant.R.id.pager_restaurant_main);
		PagerAdapter adapter = pager.getAdapter();
		assertNotNull(adapter);

		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				pager.setCurrentItem(1); // requests page
			}
			
		});
		
		getInstrumentation().waitForIdleSync();
		
		final View button = mActivity.findViewById(uw.cse.dineon.restaurant.R.id.listitem_request_top);
		assertNotNull(button);

		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				button.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		
		final View newActButton = pager.findFocus().findViewById(R.id.button_proceed);
		assertNotNull(newActButton);
		
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				newActButton.performClick();
			}
		});
		
		ActivityMonitor monitor = getInstrumentation().addMonitor(
				RequestDetailActivity.class.getName(), null, false);
		
		RequestDetailActivity startedActivity = (RequestDetailActivity) monitor
		        .waitForActivityWithTimeout(WAIT_LOGIN_TIME);
		assertNotNull(startedActivity);
		
		if (startedActivity != null) {
			startedActivity.finish();
		}
		
	}
	
	/**
	 * Tests for dining session detail page
	 * Whitebox testing
	 */
	public void testDiningSessionDetailPage() {
		final android.support.v4.view.ViewPager pager = (android.support.v4.view.ViewPager) 
				mActivity.findViewById(uw.cse.dineon.restaurant.R.id.pager_restaurant_main);
		PagerAdapter adapter = pager.getAdapter();
		assertNotNull(adapter);

		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				pager.setCurrentItem(2); // requests page
			}
			
		});
		
		
		
		getInstrumentation().waitForIdleSync();
		assertEquals(2, pager.getCurrentItem());
		
		
		//Staging
		final View vwTop = mActivity.findViewById(R.id.listitem_user_top);
		
		
		assertNotNull(vwTop);

		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				vwTop.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		
		final View proButton = pager.findFocus().findViewById(uw.cse.dineon.restaurant.R.id.button_proceed);
		assertNotNull(proButton);
		
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				proButton.performClick();
			}
		});
		
		ActivityMonitor monitor = getInstrumentation().addMonitor(
				DiningSessionDetailActivity.class.getName(), null, false);
		
		DiningSessionDetailActivity startedActivity = (DiningSessionDetailActivity) monitor
		        .waitForActivityWithTimeout(WAIT_LOGIN_TIME );
		
		assertNotNull(startedActivity);
		
		if (startedActivity != null) {
			startedActivity.finish();
		}
	}


	/**
	 * Tests for requests page components
	 * Whitebox testing
	 */
	public void testRequestLayoutItemsPopulate() {
		TextView requestTitle = (TextView) mActivity.findViewById(uw.cse.dineon.restaurant.R.id.label_request_title);
		TextView requestTime = (TextView) mActivity.findViewById(uw.cse.dineon.restaurant.R.id.label_request_time);
		ImageView arrowButton = (ImageView) mActivity.findViewById(uw.cse.dineon.restaurant.R.id.button_expand_request);
		assertNotNull(requestTitle);
		assertNotNull(requestTime);
		assertNotNull(arrowButton);
	}
	
	/**
	 * Asserts that the customer request does not store nulls
	 */
	public void testOnRequestSelectedNull() {
		mActivity.onRequestSelected(mRequest);
		ActivityMonitor testMon = getInstrumentation().addMonitor(
				RequestDetailActivity.class.getName(), null, false);
		Activity testA = getInstrumentation().waitForMonitorWithTimeout(testMon, 4000);
		testA.finish();
		mActivity.onRequestSelected(null);
		assertNotNull(mActivity.getRequest());
	}
	
	/**
	 * Asserts that orders are correctly updated with null and nonnull values
	 */
	public void testOnOrderSelected() {
		mActivity.onOrderSelected(mOrder);
		ActivityMonitor testMon = getInstrumentation().addMonitor(
				OrderDetailActivity.class.getName(), null, false);
		Activity testA = getInstrumentation().waitForMonitorWithTimeout(testMon, 4000);
		testA.finish();
		assertEquals(mOrder, mActivity.getOrder());
		
		mActivity.onOrderSelected(null);
		assertNotNull(mActivity.getOrder());
	}
	
	/**
	 * Asserts that null dining sessions are not stored
	 */
	public void testOnDiningSessionSelectedNull() {
		mActivity.onDiningSessionSelected(testSession);
		
		ActivityMonitor monRia = getInstrumentation().addMonitor(
				DiningSessionDetailActivity.class.getName(), null, false);
		Activity dsda = getInstrumentation().waitForMonitorWithTimeout(monRia, 4000);
		if(dsda != null) dsda.finish();
		mActivity.onDiningSessionSelected(null);
		assertNotNull(mActivity.getDiningSession());
		
		
	}
	
	/**
	 * Tests that progress changed correctly displays
	 */
	public void testOnProgressChanged() {
		mActivity.onProgressChanged(mOrder, 100);
	}
	
	/**
	 * Tests that shout outs are sent correctly
	 */
	public void testSendShoutOut() {
		mActivity.sendShoutOut(mUI.getUserInfo(), "Testing");
	}
	
	/**
	 * Toast that tasks are sent to staff
	 */
	public void testOnSendTaskToStaff() {
		mActivity.onSendTaskToStaff(mRequest, "Bert", "Low");
	}
	

	
	
}
