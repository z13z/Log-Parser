package com.azry.utils.logparser.parsers.regex;

import com.azry.utils.logparser.dto.request.RegexParseRequest;

public interface RegexParser {

	void parse(RegexParseRequest regexRequest);
}
