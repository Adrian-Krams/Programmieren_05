package highlighting.presets;

import highlighting.regex.Token;
import java.util.List;
import java.util.regex.Pattern;

public final class MiniJavaTokens {

    private static final Pattern JAVADOC_COMMENT = Pattern.compile("/\\*\\*[\\s\\S]*?\\*/");
    private static final Pattern BLOCK_COMMENT = Pattern.compile("/\\*(?!\\*)[\\s\\S]*?\\*/");
    private static final Pattern LINE_COMMENT = Pattern.compile("//[^\\r\\n]*");

    private static final Pattern STRING_LITERAL = Pattern.compile("\"([^\"\\\\]|\\\\.)*\"");
    private static final Pattern CHAR_LITERAL = Pattern.compile("'([^'\\\\]|\\\\.)'");

    private static final Pattern ANNOTATION = Pattern.compile("@[A-Za-z][A-Za-z-]*");
    private static final Pattern KEYWORD =
        Pattern.compile("\\b(package|import|class|public|private|final|return|null|new)\\b");

    // TODO (Phase I+II: RegexHighlighter/ScanningHighlighter)
    // TODO: Define the MiniJava tokens used by the highlighters. Each token is a mapping from a
    // regular expression to a colour (and, if applicable, a specific matching group). The order of
    // tokens in this list determines their relative priority during highlighting. One example token
    // definition is provided below; define the remaining tokens in an analogous way.

    // Basic token set for MiniJava. Extend this list with further tokens as needed (e.g. identifiers,
    // numeric literals, operators, brackets, whitespace), following the same pattern. Each token is
    // defined by a regular expression and a colour. Optionally, a specific capturing group within the
    // pattern can be selected as the "highlighted" region.
    public static List<Token> defaultTokens() {
        return List.of(
            Token.of(JAVADOC_COMMENT, MiniJavaColours.JAVADOC_COMMENT_COLOUR),
            Token.of(BLOCK_COMMENT, MiniJavaColours.BLOCK_COMMENT_COLOUR),
            Token.of(LINE_COMMENT, MiniJavaColours.LINE_COMMENT_COLOUR),
            Token.of(STRING_LITERAL, MiniJavaColours.STRING_LITERAL_COLOUR),
            Token.of(CHAR_LITERAL, MiniJavaColours.CHAR_LITERAL_COLOUR),
            Token.of(ANNOTATION, MiniJavaColours.ANNOTATION_COLOUR),
            Token.of(KEYWORD, MiniJavaColours.KEYWORD_COLOUR));
    }
}
