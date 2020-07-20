package com.azry.utils.logparser.parsers;

import com.azry.utils.logparser.helpers.EmptyQueue;
import com.azry.utils.logparser.services.FilesDirectoryController;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Queue;
import java.util.function.Function;
import java.util.regex.Pattern;

@Service
public class Parser {

	private static final String LINE_GROUP_MATCH_SEPARATOR = "---------------------------------";

	private static final String LOG_LINE_DATE_PREFIX_REGEX = "\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d,\\d\\d\\d";

	private static final int LOG_LINE_DATE_PREFIX_LENGTH = 24;

	private static final Pattern LOG_LINE_DATE_PREFIX_PATTERN = Pattern.compile(LOG_LINE_DATE_PREFIX_REGEX);

	private FilesDirectoryController directoryController;

	@Autowired
	public Parser(FilesDirectoryController directoryController) {
		this.directoryController = directoryController;
	}

	public void parseLog(String logName, Function<String, Boolean> lineChecker, Integer wrapLinesAroundCnt) throws IOException {
		File outputFile = directoryController.createOutputFile(logName);
		File logFile = directoryController.getLogFile(logName);
		//noinspection ResultOfMethodCallIgnored
		outputFile.createNewFile();

		try (BufferedReader reader = new BufferedReader(new FileReader(logFile));
			 PrintWriter writer = new PrintWriter(outputFile)) {
			String line;
			boolean writeLine, prevWritten = false;
			int writePrevLines = wrapLinesAroundCnt == null ? 0 : wrapLinesAroundCnt;
			Queue<String> prevNonMatchedLines = wrapLinesAroundCnt == null ? new EmptyQueue<>() : new CircularFifoQueue<>(writePrevLines);
			while ((line = reader.readLine()) != null) {
				if (line.length() > LOG_LINE_DATE_PREFIX_LENGTH &&
						LOG_LINE_DATE_PREFIX_PATTERN.matcher(line.substring(0, LOG_LINE_DATE_PREFIX_LENGTH)).find()) {
					if (!(writeLine = lineChecker.apply(line))) {
						prevNonMatchedLines.offer(line);
					} else {
						if (!prevNonMatchedLines.isEmpty()) {
							writer.println(LINE_GROUP_MATCH_SEPARATOR);
							prevNonMatchedLines.forEach(writer::println);
						}
					}
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
