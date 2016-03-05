import java.util.ArrayList;
import java.util.Date;

public class testPDF {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//extractPdf pdf = new extractPdf("E:/mcgill/6th semester/Fall-2015-ECSE-304-001-Signals-and-Systems-2-1-15-2016-6-07-PM/Syllabus W2016.pdf");
		extractPdf pdf = new extractPdf("E:/ECSE334_Course_Outline.pdf");
		// System.out.println(pdf.getPdfText());
		String[] keywords = {"midterm", "Midterm", "MIDTERM"};
		//String[] keywords = {"quiz", "Quiz", "QUIZ"};
		ArrayList<String> linesForMid = pdf.getStringAround(keywords);
		
		//System.out.println(linesForMid);
		ArrayList<String> revInfo = pdf.getStringWithDates(linesForMid);
		ArrayList<Date> taskDates = new ArrayList<Date>();
		
		//System.out.println(revInfo);
		
		for (String dInfo: revInfo) {
			
			Date taskDate = pdf.extractDateFromString(dInfo);

			if (!taskDates.contains(taskDate) && taskDate != null) {
				taskDates.add(taskDate);
			}
			
		}
		
		CalenderEvent[] midEvents = new CalenderEvent[taskDates.size()];
		
		for (int i = 0; i < midEvents.length; i++) {
			midEvents[i] = new CalenderEvent(keywords[0] + " " + (i + 1), taskDates.get(i));
		}
		
		for (CalenderEvent mids : midEvents) {
			System.out.println(mids.getTask() + " at " + mids.getDate() + " " + mids.getMonth());
		}
		
	}

}
