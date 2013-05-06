package uw.cse.dineon.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uw.cse.dineon.library.util.ParseUtil;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;

/**
 * DiningSession object that represents a period of time and the orders
 * made while dining for a particular session.
 * 
 * @author zachr81
 *
 */
public class DiningSession extends Storable implements Parcelable {
	
	// ID's used for easier parsing
	public static final String USERS = "users";
	public static final String START_TIME = "startTime";
	public static final String END_TIME = "endTime";
	public static final String ORDERS = "orders";
	public static final String SESS_TOKEN = "sessToken";
	public static final String TABLE_ID = "tableID";
	public static final String WAITER_REQUEST = "waiterRequest";
	
	private List<UserInfo> users;	// list of users in this session
	private long startTime;			// start of this dining session
	private long endTime;			// end of this dining session
	private List<Order> orders;		// list of orders made during this session
	private int sessToken;			// token used to ID a particular sesson
	private int tableID;			// ID of table the dining is taking place at
	private RequestType waiterRequest;	// type of request being made (if any)
	
	/**
	 * Default constructor.
	 */
	public DiningSession() {
		super();
		this.users = new ArrayList<UserInfo>();
		this.orders = new ArrayList<Order>();
	}
	
	/**
	 * Creates a new DiningSession object with a particular table
	 * and ID (sessToken).
	 * 
	 * @param orders before instantiating this session
	 * @param sessToken int ID of a particular dining session
	 * @param tableID int ID of the table for this session
	 */
	public DiningSession(List<Order> orders, int sessToken, int tableID) {
		super();
		this.users = new ArrayList<UserInfo>();
		this.sessToken = sessToken;
		this.startTime = System.currentTimeMillis();
		this.orders = orders;
		this.tableID = tableID;
		this.waiterRequest = RequestType.NONE;
	}
	
	/**
	 * Create a new DiningSession from a given Parcel.
	 * 
	 * @param source Parcel containing information in the following form:
	 * 		List<UserInfo>, long, long, list<Order>, int, int.
	 */
	public DiningSession(Parcel source) {
		super();
		readFromParcel(source);
	}

	/**
	 * @return the orders
	 */
	public List<Order> getOrders() {
		List<Order> copy = new ArrayList<Order>(orders.size());
		Collections.copy(copy, orders);
		return copy;
	}
	
	/**
	 * @param orders the orders to set
	 */
	public void setOrders(List<Order> orders) {
		List<Order> lst = new ArrayList<Order>();
		lst.addAll(orders);
		this.orders = lst;
	}
	
	/**
	 * @return the sessToken
	 */
	public int getSessToken() {
		return sessToken;
	}
	
	/**
	 * @param sessToken the sessToken to set
	 */
	public void setSessToken(int sessToken) {
		this.sessToken = sessToken;
	}
	
	/**
	 * @return the tableID
	 */
	public int getTableID() {
		return tableID;
	}
	
	/**
	 * @param tableID the tableID to set
	 */
	public void setTableID(int tableID) {
		this.tableID = tableID;
	}
	
	/**
	 * @param order to add
	 */
	public void addOrder(Order order) {
		orders.add(order);
	}
	
	/**
	 * @param order to remove
	 */
	public void removeOrder(Order order) {
		orders.remove(order);
	}

	/**
	 * Packs this DiningSession into a ParseObject to be stored.
	 * 
	 * @return ParseObject containing saved/packed data
	 */
	@SuppressLint("UseValueOf")
	@Override
	public ParseObject packObject() {
		ParseObject pobj = new ParseObject(this.getClass().getSimpleName());
		pobj.add(DiningSession.USERS, ParseUtil.packListOfStorables(this.users));
		pobj.add(DiningSession.START_TIME, new Long(this.startTime));
		pobj.add(DiningSession.END_TIME, new Long(this.endTime));
		pobj.add(DiningSession.ORDERS, ParseUtil.packListOfStorables(this.orders));
		pobj.add(DiningSession.SESS_TOKEN, new Integer(this.sessToken));
		pobj.add(DiningSession.TABLE_ID, new Integer(this.tableID));
		pobj.add(DiningSession.WAITER_REQUEST, this.waiterRequest.ordinal());
		//in case this storable is going to be used after the pack.
		this.setObjId(pobj.getObjectId());
		
		return pobj;
	}

	/**
	 * Unpacks the given ParseObject into this DiningSession setting
	 * field values to the given data.
	 * 
	 * @param pobj ParseObject to be unpacked into a DiningSession
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void unpackObject(ParseObject pobj) {
		this.setObjId(pobj.getObjectId());
		//this.users.addAll((List<UserInfo>) pobj.get(this.USERS));
		this.users.addAll((List) ParseUtil.unpackListOfStorables((ParseObject) pobj.get(this.USERS)));
		this.setStartTime(pobj.getLong(START_TIME));
		this.setEndTime(pobj.getLong(this.END_TIME));
		this.setOrders((List<Order>) pobj.get(this.ORDERS));
		this.setSessToken(pobj.getInt(this.SESS_TOKEN));
		this.setTableID(pobj.getInt(this.TABLE_ID));
		//this.setWaiterRequest((RequestType) pobj.get(this.WAITER_REQUEST));
	}
	
	/**
	 * @param wreq RequestType from customer to waiter
	 */
	public void setWaiterRequest(RequestType wreq) {
		this.waiterRequest = wreq;
	}
	
	/**
	 * @return the users
	 */
	public List<UserInfo> getUsers() {
		List<UserInfo> copy = new ArrayList<UserInfo>();
		for (int i = 0; i < this.users.size(); i++) {
			copy.add(this.users.get(i));
		}
		return copy;
	}

	/**
	 * @param userInfo to add to the session
	 */
	public void addUser(UserInfo userInfo) {
		this.users.add(userInfo);
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	/**
	 * Writes this DiningSession to Parcel dest in the order:
	 * List<User>, long, long, (boolean stored as an) int, List<Order>, int, int
	 * to be retrieved at a later time.
	 * 
	 * @param dest Parcel to write DiningSession data to.
	 * @param flags int
	 */
	// NOTE: if you change the write order you must change the read order
	// below.
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(users);
		dest.writeLong(startTime);
		dest.writeLong(endTime);
		dest.writeTypedList(orders);
		dest.writeInt(sessToken);
		dest.writeInt(tableID);
	}
	
	/**
	 * Helper method for updating DiningSession with the data from a Parcel.
	 * @param source Parcel containing data in the order:
	 * 		List<User>, long, long, (boolean stored as an) int, List<Order>, int, int
	 */
	private void readFromParcel(Parcel source) {
		source.readTypedList(users, UserInfo.CREATOR); // default class load used
		startTime = source.readLong();
		endTime = source.readLong();
		source.readTypedList(orders, Order.CREATOR);
		sessToken = source.readInt();
		tableID = source.readInt();
	}
	
	/**
	 * Parcelable creator object of a DiningSession.
	 * Can create a MenuItem from a Parcel.
	 */
	public static final Parcelable.Creator<DiningSession> CREATOR = 
			new Parcelable.Creator<DiningSession>() {

				@Override
				public DiningSession createFromParcel(Parcel source) {
					return new DiningSession(source);
				}

				@Override
				public DiningSession[] newArray(int size) {
					return new DiningSession[size];
				}
			};

}
