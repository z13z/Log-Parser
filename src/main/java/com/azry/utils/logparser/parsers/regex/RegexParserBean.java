package com.azry.utils.logparser.parsers.regex;

import com.azry.utils.logparser.dto.request.RegexParseRequest;
import com.azry.utils.logparser.parsers.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RegexParserBean implements RegexParser {

	private Parser parser;

	@Autowired
	public RegexParserBean(Parser parser) {
		this.parser = parser;
	}

	@Override
	public void parse(RegexParseRequest request) {
		try {
			Pattern pattern = Pattern.compile(request.getRegex());
			List<Pattern> excludePatterns = request.getExcludeRegexList().stream().filter(s -> !StringUtils.isEmpty(s)).map(Pattern::compile).collect(Collectors.toList());
			parser.parseLog(request.getFileName(), (line) -> lineContainsStringMatchingRegex(line, pattern, excludePatterns));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean lineContainsStringMatchingRegex(String line, Pattern pattern, List<Pattern> excludePatterns) {
		return excludePatterns.stream().noneMatch((pat) -> pat.matcher(line).find()) && pattern.matcher(line).find();
	}
}
