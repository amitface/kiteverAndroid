package sms19.listview.adapter;

import java.util.ArrayList;
import java.util.List;

import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.ContactAdd;
import sms19.listview.newproject.NewContact;
import sms19.listview.newproject.model.FetchAllContact.ContactDetails;
import sms19.listview.newproject.model.UpdateContactModel;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;

public class ContactAdaptor extends ArrayAdapter<ContactDetails> implements
		Filterable {
	static CheckBox cbx;
	private List<ContactDetails> planetList;
	private Context context;
	private Filter planetFilter;
	private List<ContactDetails> origPlanetList;

	String Mobile = "";
	private String UserId = "", ResMObile = "", Number_Group = "";
	String Password = "";

	String number, Namec;

	DataBaseDetails dbObject;

	static List<ContactDetails> gCheckBoxDetails = new ArrayList<ContactDetails>();

	public static boolean addTogroupButtonsatus = false;

	public static String userName = "";

	public static boolean statusCheckAllSer = false;

	public ContactAdaptor(List<ContactDetails> planetList, Context ctx) {

		super(ctx, R.layout.customlistforcontacts, planetList);

		this.context = ctx;
		this.planetList = planetList;
		this.origPlanetList = planetList;

		try {
			addTogroupButtonsatus = !planetList.isEmpty();
		} catch (Exception e) {

		}

	}

	public int getCount() {
		try {
			return planetList.size();
		} catch (Exception e) {
			return 0;
		}
	}

	public ContactDetails getItem(int position) {
		return planetList.get(position);
	}

	public long getItemId(int position) {
		return planetList.get(position).hashCode();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// View v = convertView;

		PlanetHolder holder = new PlanetHolder();

		// First let's verify the convertView is not null
		if (convertView == null) {

			// This a new view we inflate the new layout
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater
					.inflate(R.layout.customlistforcontacts, null);

			// Now we can fill the layout with the right values
			TextView tv = (TextView) convertView
					.findViewById(R.id.textViewName);
			TextView tvMobile = (TextView) convertView
					.findViewById(R.id.textViewMObile);

			cbx = (CheckBox) convertView.findViewById(R.id.checkBoxContact);
			ImageView add = (ImageView) convertView
					.findViewById(R.id.editContactImage);
			ImageView delete = (ImageView) convertView
					.findViewById(R.id.deleteContactImage);

			number = planetList.get(position).getContact_mobile();

			Namec = planetList.get(position).getContact_name();

			dbObject = new DataBaseDetails(context);

			add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						String cname = planetList.get(position)
								.getContact_name();
						String cnumber = planetList.get(position)
								.getContact_mobile();
						String cid = planetList.get(position).getContact_id();
						String emailId = planetList.get(position).getEmail_id();
						Intent i = new Intent(context, ContactAdd.class);

						i.putExtra("editname", cname);
						i.putExtra("CID", cid);
						i.putExtra("editNumber", cnumber);
						i.putExtra("updateService", "updateService");
						i.putExtra("emailId", emailId);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i);
					} catch (Exception e) {
					}

				}
			});

			delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					new AlertDialog.Builder(context)
							.setCancelable(false)
							.setMessage("Are you sure to Delete Contact?")
							.setPositiveButton("YES",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

											fetchMobileandUserId();

											String cnumber = planetList.get(
													position)
													.getContact_mobile();

											planetList.remove(position);
											notifyDataSetChanged();
											try {
												dbObject.Open();
												dbObject.DeleteIndividuleContact(number);
												dbObject.close();

											} catch (Exception fd) {
											}
											new webservice(
													null,
													webservice.DeleteIndividualContact
															.geturl(UserId,
																	cnumber),
													webservice.TYPE_GET,
													webservice.TYPE_DELETE_SINGLE_CONTACT,
													new ServiceHitListener() {

														@Override
														public void onSuccess(
																Object Result,
																int id) {
															try {
																dbObject.Open();
																dbObject.DeleteIndividuleContact(number);
																dbObject.close();

															} catch (Exception fd) {
															}

															UpdateContactModel mod = (UpdateContactModel) Result;

															try {
																if (mod.getDeleteContactReview()
																		.get(0)
																		.getMsg()
																		.length() > 0) {
																	Toast.makeText(
																			context,
																			""
																					+ mod.getDeleteContactReview()
																							.get(0)
																							.getMsg(),
																			Toast.LENGTH_SHORT)
																			.show();
																} else {
																	Toast.makeText(
																			context,
																			""
																					+ mod.getDeleteContactReview()
																							.get(0)
																							.getError(),
																			Toast.LENGTH_SHORT)
																			.show();

																}
															} catch (Exception e) {
																// e.printStackTrace();
																// Toast.makeText(context,
																// ""+mod.getDeleteContactReview().get(0).getError(),
																// Toast.LENGTH_SHORT).show();

															}

														}

														@Override
														public void onError(
																String Error,
																int id) {

														}
													});

										}
									})
							.setNegativeButton("NO",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.cancel();
										}
									}).show();

				}
			});

			holder.planetNameView = tv;
			holder.planetContactView = tvMobile;

			convertView.setTag(holder);

			if (NewContact.checkAll) {
				cbx.setChecked(true);

				statusCheckAllSer = true;

			}

			else {
				cbx.setChecked(false);
				statusCheckAllSer = false;
			}

			fetchMobileandUserId();

			cbx.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {

						try {
							number = planetList.get(position)
									.getContact_mobile();
							Namec = planetList.get(position).getContact_name();
						} catch (Exception e) {
							e.printStackTrace();
						}

						if (checkResnumber(number)) {

							/*
							 * try { NewContact.cbxselect.setChecked(false); }
							 * catch (Exception e) { }
							 */

							dbObject.Open();
							dbObject.deleteNumberFromRecipient(number);
							dbObject.close();
						} else {
							dbObject.Open();
							dbObject.addReceipent(UserId, number, Namec);
							dbObject.close();
						}

						if (!checkAllContact(number)) {
							dbObject.Open();
							dbObject.addSelectedContactsFromGroup(Namec,
									number, UserId);
							dbObject.close();
						} else {
							dbObject.Open();
							dbObject.deleteNumberFromSelectedContacts(number);
							dbObject.close();
						}

					} catch (Exception e) {

					}

				}
			});

		} else
			holder = (PlanetHolder) convertView.getTag();
		ContactDetails pp = planetList.get(position);
		holder.planetNameView.setText(pp.getContact_name());
		holder.planetContactView.setText(pp.getContact_mobile());

		return convertView;
	}

	public void resetData() {
		planetList = origPlanetList;
	}

	public static class PlanetHolder {
		public TextView planetNameView;
		public TextView planetContactView;
	}

	@Override
	public Filter getFilter() {
		if (planetFilter == null)
			planetFilter = new PlanetFilter();

		return planetFilter;
	}

	private class PlanetFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			FilterResults results = new FilterResults();

			// We implement here the filter logic
			if (constraint == null || constraint.length() == 0) {
				// No filter implemented we return all the list
				results.values = origPlanetList;
				results.count = origPlanetList.size();
			}

			else {
				// We perform filtering operation
				List<ContactDetails> nPlanetList = new ArrayList<ContactDetails>();

				for (ContactDetails pp : planetList) {
					if (pp.getContact_name().toUpperCase()
							.startsWith(constraint.toString().toUpperCase()))
						nPlanetList.add(pp);
				}

				results.values = nPlanetList;
				results.count = nPlanetList.size();

			}

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {

			// Now we have to inform the adapter about the new list filtered
			if (results.count == 0)
				notifyDataSetInvalidated();

			else {
				planetList = (List<ContactDetails>) results.values;
				notifyDataSetChanged();
			}

		}

	}

	public void fetchMobileandUserId() {

		dbObject = new DataBaseDetails(context);

		dbObject.Open();

		Cursor c;

		c = dbObject.getLoginDetails();

		while (c.moveToNext()) {
			Mobile = c.getString(1);
			UserId = c.getString(3);
			Password = c.getString(5);

		}

		dbObject.close();
	}

	public boolean checkResnumber(String number) {
		dbObject.Open();

		Cursor c;

		c = dbObject.getReceipent();

		while (c.moveToNext()) {

			ResMObile = c.getString(1).trim();

			if (number.trim().equals(ResMObile)) {
				dbObject.close();

				return true;
			}

		}

		dbObject.close();

		return false;
	}

	public boolean IsResContactExist() {

		dbObject.Open();

		Cursor c;

		c = dbObject.getReceipent();

		while (c.moveToNext()) {

			return true;
		}

		dbObject.close();
		return false;
	}

	public boolean checkAllContact(String number) {
		dbObject.Open();

		Cursor c;

		c = dbObject.getSelectedGroupContacts();

		if (c.getCount() >= 0) {
			while (c.moveToNext()) {

				Number_Group = c.getString(1);

				if (number.equals(Number_Group)) {
					dbObject.close();

					return true;
				}
			}
		}

		dbObject.close();

		return false;
	}

}
