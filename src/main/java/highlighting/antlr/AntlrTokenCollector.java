package highlighting.antlr;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaColours;
import java.awt.*;
import java.util.List;
import org.antlr.v4.runtime.*;

// TODO Phase III — AntlrTokenCollector (token-based syntax highlighting).
public class AntlrTokenCollector extends SyntaxHighlighter {

    @Override
    public List<HighlightRegion> collectMatches(String text) {

        List<HighlightRegion> regions = new java.util.ArrayList<>();

        MiniJavaLexer lexer = new MiniJavaLexer(CharStreams.fromString(text));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        tokenStream.fill();

        List<Token> tokens = tokenStream.getTokens();

        for (int i = 0; i < tokens.size(); i++) {

            Token token = tokens.get(i);

            if (token.getType() == Token.EOF) {
                continue;
            }

            Color colour = null;

            switch (token.getType()) {

                case MiniJavaLexer.STRING_LITERAL ->
                    colour = MiniJavaColours.STRING_LITERAL_COLOUR;

                case MiniJavaLexer.CHAR_LITERAL ->
                    colour = MiniJavaColours.CHAR_LITERAL_COLOUR;

                case MiniJavaLexer.LINE_COMMENT ->
                    colour = MiniJavaColours.LINE_COMMENT_COLOUR;

                case MiniJavaLexer.BLOCK_COMMENT ->
                    colour = MiniJavaColours.BLOCK_COMMENT_COLOUR;

                case MiniJavaLexer.JAVADOC_COMMENT ->
                    colour = MiniJavaColours.JAVADOC_COMMENT_COLOUR;

                case MiniJavaLexer.PACKAGE,
                     MiniJavaLexer.IMPORT,
                     MiniJavaLexer.CLASS,
                     MiniJavaLexer.PUBLIC,
                     MiniJavaLexer.PRIVATE,
                     MiniJavaLexer.FINAL,
                     MiniJavaLexer.RETURN,
                     MiniJavaLexer.NULL,
                     MiniJavaLexer.NEW,
                     MiniJavaLexer.IF,
                     MiniJavaLexer.ELSE,
                     MiniJavaLexer.WHILE,
                     MiniJavaLexer.EXTENDS,
                     MiniJavaLexer.IMPLEMENTS ->
                    colour = MiniJavaColours.KEYWORD_COLOUR;

                case MiniJavaLexer.AT -> {
                    colour = MiniJavaColours.ANNOTATION_COLOUR;

                    if (i + 1 < tokens.size()
                        && tokens.get(i + 1).getType() == MiniJavaLexer.IDENTIFIER) {

                        Token nextToken = tokens.get(i + 1);

                        regions.add(new HighlightRegion(
                            nextToken.getStartIndex(),
                            nextToken.getStopIndex() + 1,
                            MiniJavaColours.ANNOTATION_COLOUR
                        ));
                    }
                }
            }

            if (colour != null) {
                regions.add(new HighlightRegion(
                    token.getStartIndex(),
                    token.getStopIndex() + 1,
                    colour
                ));
            }
        }

        return regions;
    }
}
