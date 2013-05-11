package uw.cse.dineon.library;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract.CommonDataKinds.Email;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * @author Espeo196, Michael Hotan
 */
public class UserInfo extends Storable {
	public static final String PARSEUSER = "parseUser";
	public static final String IMAGE_ID = "imageId";
	public static final String PROFILE_DESCRIPTION = "profileDescription";
	public static final String PHONE = "userPhone";
	
	private static final String UNDETERMINED = "Undetermined";
	
	private final ParseUser mUser;
	private String mPhone;
	private String mImageID;
	private String mProfileDescription;

	/**
	 * Default constructor.
	 * @param user ParseUser
	 */
	public UserInfo(ParseUser user) {
		super(UserInfo.class);
		mUser = user;
		mImageID = UNDETERMINED;
		mProfileDescription = UNDETERMINED;
		mPhone = UNDETERMINED;
	}
	
	/**
	 * Creates a UserInfo instance from this parse object.
	 * @param po PArseObject 
	 * @throws ParseException 
	 */
	public UserInfo(ParseObject po) throws ParseException {
		super(po);
		mUser = po.getParseUser(PARSEUSER).fetchIfNeeded();
		mImageID = po.getString(IMAGE_ID);
		mProfileDescription = po.getString(PROFILE_DESCRIPTION);
		mPhone = po.getString(PHONE);
	}
	
	@Override
	public ParseObject packObject() {
		ParseObject po = super.packObject();
		po.put(PARSEUSER, (ParseUser)mUser);
		po.put(IMAGE_ID, (String)mImageID);
		po.put(PROFILE_DESCRIPTION, (String)mProfileDescription);
		po.put(PHONE, (String)mPhone);
		return po;
	}
	
	/**
	 * @return String user name
	 */
	public String getName() {
		return mUser.getUsername();
	}

	/**
	 * @return int user phone number
	 */
	public String getPhone() {
		return mPhone;
	}

	/**
	 * @param number int Phone number
	 */
	public void setPhone(String number) {
		this.mPhone = number;
	}

	/**
	 * @return String user email
	 */
	public String getEmail() {
		return mUser.getEmail();
	}

	/**
	 * @param email String
	 */
	public void setEmail(String email) {
		mUser.setEmail(email);
	}




//	@Override
//	public void unpackObject(ParseObject pobj) {
//		this.setObjId(pobj.getObjectId());
//		this.setName(pobj.getString(UserInfo.PARSEUSER));
//		this.setPhone(pobj.getString(UserInfo.IMAGE_ID));
//		this.setEmail(pobj.getString(UserInfo.PROFILE_DESCRIPTION));
//	}
//	
//	@Override
//	public int describeContents() {
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeString(mName);
//		dest.writeString(phone);
//		dest.writeString(email);
//		dest.writeString(this.getObjId());
//	}
//	
//	/**
//	 * Parcelable creator object of a UserInfo.
//	 * Can create a UserInfo from a Parcel.
//	 */
//	public static final Parcelable.Creator<UserInfo> CREATOR = 
//			new Parcelable.Creator<UserInfo>() {
//
//				@Override
//				public UserInfo createFromParcel(Parcel source) {
//					return new UserInfo(source);
//				}
//
//				@Override
//				public UserInfo[] newArray(int size) {
//					return new UserInfo[size];
//				}
//	};
//			
//
//	/**
//	 * Read an object back out of parcel.
//	 * @param source parcel to read from.
//	 */
//	private void readFromParcel(Parcel source) {
//		this.setName(source.readString());
//		this.setPhone(source.readString());
//		this.setEmail(source.readString());
//		this.setObjId(source.readString());
//	}
}
