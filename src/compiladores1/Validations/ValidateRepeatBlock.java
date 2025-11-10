package compiladores1.Validations;

import java.util.ArrayList;
import java.util.List;

import compiladores1.Models.ErrorDictionary;
import compiladores1.Models.Token;
import compiladores1.Models.TokenType;

public class ValidateRepeatBlock {
	private final List<Token> tokens;
	private int position = 0;
	private final List<String> errors = new ArrayList<>();
	private final ErrorDictionary errorDict = new ErrorDictionary();

	public ValidateRepeatBlock(List<Token> tokens) {
		this.tokens = tokens != null ? tokens : new ArrayList<>();
	}

	public void parseRepeatBlocks() {
		while (position < tokens.size()) {
			Token t = peek();
			if (t == null) { advance(); continue; }

			if (t.getType() == TokenType.REPEAT) {
				validateRepeatAt(position);
			} else {
				advance();
			}
		}
	}

	private void validateRepeatAt(int repeatIdx) {
		Token repeatTok = tokens.get(repeatIdx);
		int repeatLine = repeatTok.getLine();

		int tokensOnRepeatLine = 0;
		for (Token tk : tokens) if (tk.getLine() == repeatLine) tokensOnRepeatLine++;
		if (tokensOnRepeatLine > 1) {
			addError(1100, repeatLine, null);
		}

		int nextIdx = repeatIdx + 1;
		if (nextIdx >= tokens.size()) {
			addError(1101, repeatLine, null);
			position = nextIdx;
			return;
		}
		Token next = tokens.get(nextIdx);
		if (next.getType() != TokenType.BEGIN || next.getLine() != repeatLine + 1) {
			addError(1101, next.getLine(), null);
			position = nextIdx;
			return;
		}

		int beginLine = next.getLine();
		int tokensOnBeginLine = 0;
		for (Token tk : tokens) if (tk.getLine() == beginLine) tokensOnBeginLine++;
		if (tokensOnBeginLine > 1) {
			addError(1101, beginLine, "La palabra 'begin' debe estar sola en su línea");
		}

		int scan = nextIdx + 1;
		int depth = 1;
		int matchingEndIdx = -1;
		while (scan < tokens.size()) {
			Token tk = tokens.get(scan);
			if (tk.getType() == TokenType.BEGIN) depth++;
			else if (tk.getType() == TokenType.END) {
				depth--;
				if (depth == 0) { matchingEndIdx = scan; break; }
			}
			scan++;
		}

		if (matchingEndIdx == -1) {
			addError(1103, beginLine, "Falta 'end;' que cierre el repeat");
			position = scan;
			return;
		}

		if (nextIdx + 1 >= matchingEndIdx) {
			addError(1102, beginLine + 1, "Falta sentencia dentro del repeat");
			position = matchingEndIdx + 1;
			return;
		}

		Token endTok = tokens.get(matchingEndIdx);
		int semIdx = -1;
		for (int k = matchingEndIdx + 1; k < tokens.size() && k <= matchingEndIdx + 3; k++) {
			Token tk = tokens.get(k);
			if (tk.getType() == TokenType.SYMBOL && ";".equals(tk.getLexeme())) { semIdx = k; break; }
		}
		if (semIdx == -1) {
			addError(1103, endTok.getLine(), "'end' debe finalizar con ';'");
			position = matchingEndIdx + 1;
			return;
		}

			int untilIdx = -1;
			for (int k = semIdx + 1; k < tokens.size() && k <= semIdx + 200; k++) {
				if (tokens.get(k).getType() == TokenType.UNTIL) { untilIdx = k; break; }
			}
		if (untilIdx == -1) {
			addError(1104, endTok.getLine(), "Falta 'until' con condición y ';'");
			position = semIdx + 1;
			return;
		}

		Token untilTok = tokens.get(untilIdx);
		int j = untilIdx + 1;
		boolean foundSemi = false;
		boolean hasCondition = false;
		while (j < tokens.size()) {
			Token tt = tokens.get(j);
			if (tt.getLine() != untilTok.getLine()) break; // condition must be on same line
			if (tt.getType() == TokenType.SYMBOL && ";".equals(tt.getLexeme())) { foundSemi = true; break; }
			hasCondition = true;
			j++;
		}
		if (!hasCondition) {
			addError(1104, untilTok.getLine(), "Falta condición después de 'until'");
		}
		if (!foundSemi) {
			addError(1104, untilTok.getLine(), "'until' debe terminar con ';'");
		}

		position = foundSemi ? j + 1 : j;
	}

	private Token peek() { return position < tokens.size() ? tokens.get(position) : null; }
	private Token advance() { return position < tokens.size() ? tokens.get(position++) : null; }

	private void addError(int code, int lineNumber, String extraInfo) {
		String message = errorDict.getError(code);
		if (extraInfo != null && !extraInfo.isEmpty()) message += ": " + extraInfo;
		errors.add(String.format("Error %d en línea %04d: %s", code, lineNumber, message));
	}

	public List<String> getErrors() { return errors; }
}
