package sms19.listview.adapter;

public class NotificationModel 
{
	private String title;
	private String message;
	private String LandingPage;
	public NotificationModel(String title, String message, String LandingPage)
	{
		this.title=title;
		this.message=message;
		this.LandingPage=LandingPage;
	}
	public String getTitle() {
		return title;
	}
	/*public void setTitle(String title) {
		this.title = title;
	}*/
	public String getMessage() {
		return message;
	}
	/*public void setMessage(String message) {
		this.message = message;
	}*/
	public String getLandingPage() {
		return LandingPage;
	}
	/*public void setLandingPage(String landingPage) {
		LandingPage = landingPage;
	}*/
	{
		
	}
}
