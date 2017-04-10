package sms19.listview.newproject.model;

import java.util.ArrayList;
import java.util.List;

public class ScheduledMailModel {

	private List<SheduledMailList> SheduledMailList;

	public ArrayList<SheduledMailList> getSheduledMailList() {
		return (ArrayList<sms19.listview.newproject.model.SheduledMailList>) SheduledMailList;
	}

	public void setSheduledMailList(List<SheduledMailList> SheduledMailList) {
		this.SheduledMailList = SheduledMailList;
	}

	@Override
	public String toString() {
		return "ClassPojo [SheduledMailList = " + SheduledMailList + "]";
	}
}
