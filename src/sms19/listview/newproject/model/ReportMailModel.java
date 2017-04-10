package sms19.listview.newproject.model;

import java.util.ArrayList;
import java.util.List;

public class ReportMailModel {
	private List<DeliveryReport> DeliveryReport;

	public ArrayList<DeliveryReport> getDeliveryReport() {
		return (ArrayList<sms19.listview.newproject.model.DeliveryReport>) DeliveryReport;
	}

	public void setDeliveryReport(List<DeliveryReport> DeliveryReport) {
		this.DeliveryReport = DeliveryReport;
	}

	@Override
	public String toString() {
		return "ClassPojo [DeliveryReport = " + DeliveryReport + "]";
	}
}
