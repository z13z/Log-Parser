package com.azry.utils.logparser.parsers;

import com.azry.utils.logparser.services.FilesDirectoryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;
import java.util.regex.Pattern;

@Service
public class Parser {

	private static final String LOG_LINE_DATE_PREFIX_REGEX = "\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d,\\d\\d\\d";

	private static final int LOG_LINE_DATE_PREFIX_LENGTH = 24;

	private static final Pattern LOG_LINE_DATE_PREFIX_PATTERN = Pattern.compile(LOG_LINE_DATE_PREFIX_REGEX);

	private FilesDirectoryController directoryController;

	@Autowired
	public Parser(FilesDirectoryController directoryController) {
		this.directoryController = directoryController;
	}

	public void parseLog(String logName, Function<String, Boolean> lineChecker) throws IOException {
		File outputFile = directoryController.createOutputFile(logName);
		//noinspection ResultOfMethodCallIgnored
		outputFile.createNewFile();

		try (BufferedReader reader = new BufferedReader(new FileReader(logName));
			 PrintWriter writer = new PrintWriter(outputFile)) {
			String line;
			boolean writeLine, prevWritten = false;
			while ((line = reader.readLine()) != null) {
				if (line.length() > LOG_LINE_DATE_PREFIX_LENGTH &&
						LOG_LINE_DATE_PREFIX_PATTERN.matcher(line.substring(0, LOG_LINE_DATE_PREFIX_LENGTH)).find()) {
					writeLine = lineChecker.apply(line);
				} else {
					writeLine = prevWritten;
				}
				if (prevWritten = writeLine) {
					writer.println(line);
					writer.flush();
				}
			}
		}
	}
}
