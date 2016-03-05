import java.io.File;
import java.io.IOException;
import java.nio.file.spi.FileTypeDetector;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.compress.compressors.FileNameUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.filetypedetector.FileType;


public class extractPdf {

	private final int toleranceLvl = 10;

	private PDDocument pdf;

	public extractPdf(String aFilePath) {

		File pdfFile = new File(aFilePath);
		try {
			if (!pdfFile.exists()) {
				throw new IOException("file " + pdfFile.getName() + " does not"
						+ " exist at path: " + pdfFile.getPath());
			}


			if (!FilenameUtils.getExtension(pdfFile.getName()).equalsIgnoreCase("pdf")) {
				throw new IOException("file " + pdfFile.getName() + " is not a pdf file");
			}

			pdf = PDDocument.load(pdfFile);			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getPdfText() {
		String text = "";
		PDFTextStripper stripper;
		try {
			stripper = new PDFTextStripper();
			stripper.setStartPage(0);
			stripper.setEndPage(pdf.getNumberOfPages());
			text = stripper.getText(pdf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	public ArrayList<String> getStringAround(String[] toSearch) {
		ArrayList<String> searchResult = new ArrayList<String>();
		String text = this.getPdfText();
		String[] allLines = text.split("\n");

		for (String line : allLines) {
			for (String currentSearch: toSearch) {
				if (line.contains(currentSearch)) {

					if (!searchResult.contains(line)) {
						searchResult.add(line);
					}
				}
			}

		}
		return searchResult;
	}

	public ArrayList<String> getStringWithDates(ArrayList<String> toSearch) {
		ArrayList<String> searchResult = new ArrayList<String>();
		Month[] months = Month.values();
		for (String line : toSearch) {

			for (Month month: months) {

				if (line.contains(month.name())) {

					if (!searchResult.contains(line)) {
						searchResult.add(line);
					}
				}
			}

		}
		return searchResult;
	}

	public Date extractDateFromString(String toParse) {
		// toParse already contains some form of date, 
		// as this function should be called after getStringWithDates()

		Month[] months = Month.values();
		String date = "";
		Month month = null;
		// split line into words
		String[] words = toParse.split("\\s+");

		int indexOfMonth = 0;
		for (int wordIdx = 0; wordIdx < words.length; wordIdx++) {
			for (Month monthVal : months) {	

				// word clean-up
				words[wordIdx] = removeUselessCharFromString(words[wordIdx]);
				if (words[wordIdx].equalsIgnoreCase(monthVal.name())) {
					month = monthVal;
					indexOfMonth = wordIdx;
					break;
				}

			}
		}

		// look for date
		int searchIdx = toleranceLvl;
		while (searchIdx > 0) {
			if ((indexOfMonth - searchIdx) >= 0 
					&& (indexOfMonth + searchIdx) <= words.length) {
				if (isDate(words[indexOfMonth - searchIdx]) != null) {
					date = isDate(words[indexOfMonth - searchIdx]);
				} 

				else if (isDate(words[indexOfMonth + searchIdx]) != null) {
					date = isDate(words[indexOfMonth + searchIdx]);
				} 
			}

			searchIdx--;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");

		Date finalDate = null;
		try {
			if (date != "" && month != null) {
				finalDate = sdf.parse(date + "/" + month.getValue());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return finalDate;
	}

	private String isDate(String toCheck) {

		toCheck = removeUselessCharFromString(toCheck);

		if (NumberUtils.isNumber(toCheck)) {
			return toCheck;
		}

		return null;

	}

	private String removeUselessCharFromString(String toClean) {
		String [] dateSuffixesAndUselessChars = {"st", "nd", "rd", "th", ".", ")", "(", "[", "]", "{", "}" , ","};

		for (String suffix: dateSuffixesAndUselessChars) {
			if (toClean.contains(suffix)) {
				toClean = toClean.replace(suffix, "");
			}
		}

		return toClean;

	}


}
