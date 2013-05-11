package uw.cse.dineon.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import uw.cse.dineon.library.util.ParseUtil;
import android.annotation.SuppressLint;

import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * DiningSession object that represents a the particular segment of time
 * where the initial user checks in to the restaurant and begins 
 * a dining session.
 * 
 * NOTE: In the future where users order food to go or pick up
 * A Dining session can also represent these Dining Sessions as well
 * 
 * Mikes: Suggestion.
 * Have the table IDs be coded where certain values represent not physically
 * checked in Dining Sessions.
 * 
 * Table Num of -1 = Delivery
 * Table Num of -2 = Pick up
 * etc...
 * 
 * @author zachr81, mhotan
 */
public class DiningSession extends TimeableStorable {

	// ID's used for easier parsing
	public static final String USERS = "users";
	public static final String PENDING_ORDERS = "pendingOrders";
	public static final String COMPLETED_ORDERS = "completedOrders";
	public static final String TABLE_ID = "tableId";
	public static final String REQUESTS = "requests";

	// list of users involved in this session
	private final List<UserInfo> mUsers;	
	// list of orders made but not completed 
	private final List<Order> mPendingOrders;
	// List of completed orders
	private final List<Order> mCompletedOrders;
	// list of unresolved pending request
	private final List<CustomerRequest> mPendingRequests;

	// ID of table the dining is taking place at
	private int mTableID;	

	/**
	 * Creates a dining session instance that is associated to a particular table.
	 * Takes current time as 
	 * @param tableID the id of the table
	 */
	public DiningSession(int tableID) {
		this(tableID, null);
	}

	/**
	 * Creates a dining session that is associated with a particular
	 * start date.
	 * @param tableId Table ID to associate to
	 * @param startDate of session
	 */
	public DiningSession(int tableId, Date startDate) {
		super(DiningSession.class, startDate);
		resetTableID(tableId);
		mUsers = new ArrayList<UserInfo>();
		mPendingOrders = new ArrayList<Order>();
		mCompletedOrders = new ArrayList<Order>();
		mPendingRequests = new ArrayList<CustomerRequest>();
	}

	/**
	 * Creates a new DiningSession object with a particular table
	 * and ID (sessToken).
	 * 
	 * @param po ParseObject to 
	 * @throws ParseException 
	 */
	public DiningSession(ParseObject po) throws ParseException {
		super(po);
		mTableID = po.getInt(TABLE_ID);
		mUsers = ParseUtil.toListOfStorables(UserInfo.class, po.getList(USERS));
		mPendingOrders = ParseUtil.toListOfStorables(Order.class, po.getList(PENDING_ORDERS));
		mCompletedOrders = ParseUtil.toListOfStorables(Order.class, po.getList(COMPLETED_ORDERS));
		mPendingRequests = ParseUtil.toListOfStorables(CustomerRequest.class, po.getList(REQUESTS));
	}
	
	/**
	 * Packs this DiningSession into a ParseObject to be stored.
	 * 
	 * @return ParseObject containing saved/packed data
	 */
	@Override
	public ParseObject packObject() {
		ParseObject po = super.packObject();
		po.put(USERS, ParseUtil.toListOfParseObjects(mUsers));
		po.put(PENDING_ORDERS, ParseUtil.toListOfParseObjects(mPendingOrders));
		po.put(COMPLETED_ORDERS, ParseUtil.toListOfParseObjects(mCompletedOrders));
		po.put(REQUESTS, ParseUtil.toListOfParseObjects(mPendingRequests));
		return po;
	}

	/**
	 * Pending orders are defined as orders that have been placed
	 * but not received by the customer.
	 * @return A list of current pending orders
	 */
	public List<Order> getPendingOrders() {
		List<Order> copy = new ArrayList<Order>(mPendingOrders.size());
		Collections.copy(copy, mPendingOrders);
		return copy;
	}

	/**
	 * In order to keep track of what orders have been completed.
	 * @return A list of currently completed orders for this dining session
	 */
	public List<Order> getCompletedOrders() {
		List<Order> copy = new ArrayList<Order>(mCompletedOrders.size());
		Collections.copy(copy, mCompletedOrders);
		return copy;
	}
	
	/**
	 * @return the users of the dining session.
	 */
	public List<UserInfo> getUsers() {
		return new ArrayList<UserInfo>(mUsers);
	}
	
	/**
	 * Returns the start time of this dining session.
	 * @return the startTime
	 */
	public Date getStartTime() {
		return getOriginatingTime();
	}
	
	/**
	 * @return the tableID
	 */
	public int getTableID() {
		return mTableID;
	}

	/**
	 * Adds a order to be pending for this session.
	 * @param order to add.
	 */
	public void addPendingOrder(Order order) {
		mPendingOrders.add(order);
	}

	/**
	 * Order has completed preparation and is served to the customer.
	 * @param order that is served
	 */
	public void orderServed(Order order) {
		// Order has left the kitchen and served to the table
		// TODO Essentially moving from one list to the other
		if (mPendingOrders.remove(order)) {
			mCompletedOrders.add(order);
		}
	}

	/**
	 * Resets the current table ID to the inputed one.
	 * @param newId to set to 
	 */
	public void resetTableID(int newId) {
		//TODO validate table number
		// Throw illegal argument exception if needed
		mTableID = newId;
	}

	/**
	 * @param userInfo to add to the session
	 */
	public void addUser(UserInfo userInfo) {
		this.mUsers.add(userInfo);
	}
	
	/**
	 * Removes this user from the current dining session if 
	 * he or she exists.
	 * @param userInfo Information of User to remove
	 */
	public void removeUser(UserInfo userInfo) {
		this.mUsers.remove(userInfo);
	}

	/**
	 * Adds the Customer Request to the this dining session.
	 * 
	 * @param request request to be added
	 */
	public void addRequest(CustomerRequest request) {
		
	}
	
	@Override
	public void deleteFromCloud() {
		
		// Delete all the request at this time
		for (CustomerRequest r: mPendingRequests) {
			r.deleteFromCloud();
		}
		
		// Delete all the pending orders
		for (Order r: mPendingOrders) {
			r.deleteFromCloud();
		}
		
		// We keep the orders in the cloud for later analytics
		// We dont
		
		super.deleteFromCloud();
	}
}
	//	/**
	//	 * Create a new DiningSession from a given Parcel.
	//	 * 
	//	 * @param source Parcel containing information in the following form:
	//	 * 		List<UserInfo>, long, long, list<Order>, int, int.
	//	 */
	//	public DiningSession(Parcel source) {
	//		super();
	//		readFromParcel(source);
	//	}

	//	/**
	//	 * Unpacks the given ParseObject into this DiningSession setting
	//	 * field values to the given data.
	//	 * 
	//	 * @param pobj ParseObject to be unpacked into a DiningSession
	//	 */
	//	@SuppressWarnings({ "unchecked", "static-access" })
	//	@Override
	//	public void unpackObject(ParseObject pobj) {
	//		this.setObjId(pobj.getObjectId());
	//		this.mUsers.addAll((List<UserInfo>) pobj.get(this.USERS));
	//		this.setStartTime(pobj.getLong(START_TIME));
	//		this.setEndTime(pobj.getLong(this.END_TIME));
	//		this.setOrders((List<Order>) pobj.get(this.ORDERS));
	//		this.setSessToken(pobj.getInt(this.SESS_TOKEN));
	//		this.setTableID(pobj.getInt(this.TABLE_ID));
	//		//this.setWaiterRequest((RequestType) pobj.get(this.WAITER_REQUEST));
	//	}

	//	/**
	//	 * @param wreq RequestType from customer to waiter
	//	 */
	//	public void setWaiterRequest(RequestType wreq) {
	//		this.waiterRequest = wreq;
	//	}

	//	@Override
	//	public int describeContents() {
	//		return 0;
	//	}
	//	
	//	/**
	//	 * Writes this DiningSession to Parcel dest in the order:
	//	 * List<User>, long, long, (boolean stored as an) int, List<Order>, int, int
	//	 * to be retrieved at a later time.
	//	 * 
	//	 * @param dest Parcel to write DiningSession data to.
	//	 * @param flags int
	//	 */
	//	// NOTE: if you change the write order you must change the read order
	//	// below.
	//	@Override
	//	public void writeToParcel(Parcel dest, int flags) {
	//		dest.writeTypedList(users);
	//		dest.writeLong(startTime);
	//		dest.writeLong(endTime);
	//		dest.writeTypedList(orders);
	//		dest.writeInt(sessToken);
	//		dest.writeInt(tableID);
	//	}
	//	
	//	/**
	//	 * Helper method for updating DiningSession with the data from a Parcel.
	//	 * @param source Parcel containing data in the order:
	//	 * 		List<User>, long, long, (boolean stored as an) int, List<Order>, int, int
	//	 */
	//	private void readFromParcel(Parcel source) {
	//		source.readTypedList(users, UserInfo.CREATOR); // default class load used
	//		startTime = source.readLong();
	//		endTime = source.readLong();
	//		source.readTypedList(orders, Order.CREATOR);
	//		sessToken = source.readInt();
	//		tableID = source.readInt();
	//	}
	//	
	//	/**
	//	 * Parcelable creator object of a DiningSession.
	//	 * Can create a MenuItem from a Parcel.
	//	 */
	//	public static final Parcelable.Creator<DiningSession> CREATOR = 
	//			new Parcelable.Creator<DiningSession>() {
	//
	//				@Override
	//				public DiningSession createFromParcel(Parcel source) {
	//					return new DiningSession(source);
	//				}
	//
	//				@Override
	//				public DiningSession[] newArray(int size) {
	//					return new DiningSession[size];
	//				}
	//			};


