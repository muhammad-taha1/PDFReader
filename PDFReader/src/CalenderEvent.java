import java.util.Date;


public class CalenderEvent {
	
	private String task;
	private Date date;
	
	public CalenderEvent(String aTask, Date aDate) {
		task = aTask;
		date = aDate;
	}

	public String getTask() {
		return task;
	}

	public Date getDateObj() {
		return date;
	}
	
	public int getDate() {
		return date.getDate();
	}
	
	public String getMonth() {
		
		for (Month month: Month.values()) {
			if ((date.getMonth() + 1) == month.getValue()) {
				return month.name();
			}
		}
		
		return null;
	}

}
