package uw.cse.dineon.restaurant.active;

import uw.cse.dineon.library.CustomerRequest;
import uw.cse.dineon.library.UserInfo;
import uw.cse.dineon.restaurant.DineOnRestaurantActivity;
import uw.cse.dineon.restaurant.R;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * This activity is used in portrait mode. 
 * @author mhotan
 */
public class RequestDetailActivity extends DineOnRestaurantActivity implements
RequestDetailFragment.RequestDetailListener {

	public static final String EXTRA_REQUEST = "request";

	private CustomerRequest mRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_details);
		
		// Assume that another activity (RestaurantMainActivity) has already set the value
		mRequest = mRestaurant.getTempCustomerRequest();

		if (mRequest == null) { 
			Log.e(TAG, "Null Customer request found");
			return;
		}

		RequestDetailFragment frag = (RequestDetailFragment) getSupportFragmentManager().
				findFragmentById(R.id.fragment1);
		if (frag != null && frag.isInLayout()) {
			frag.setRequest(mRequest);
		}
	}

	@Override
	public void onSendTaskToStaff(CustomerRequest request, String staff, String urgency) {
		// TODO Auto-generated method stub
		request.setWaiter(staff);
	}

	@Override
	public void sendShoutOut(UserInfo user, String message) {
		String log = getString(R.string.restaurant_part1) 
				+ message  + getString(R.string.restaurant_part2) + user.getName();
		Log.d(TAG, log);
		Toast.makeText(this, log, Toast.LENGTH_SHORT).show();
	}

	@Override
	public CustomerRequest getRequest() {
		return mRequest;
	}

}
