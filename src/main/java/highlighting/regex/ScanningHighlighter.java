package highlighting.regex;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaTokens;
import java.util.ArrayList;
import java.util.List;

// TODO: Implement a scanning-based highlighting strategy that reads the input from left to right.
// At each position, select the longest token that matches at this position. If there is a tie, the
// token that appears earlier in the token list should be preferred.

// TODO: Make this class inherit from {@code SyntaxHighlighter} and implement the abstract method
// {@code collectMatches}. The scanning algorithm should ensure that the resulting list of regions
// is already sorted, non-overlapping and contains only valid regions, so that no additional
// normalisation or conflict resolution is required. Therefore, {@code resolveConflicts} can be left
// as is, and {@code normalize} should be overridden as the identity function.
public class ScanningHighlighter extends SyntaxHighlighter {

    // TODO: Implement the scanning-based matching strategy here. Iterate from left to right over the
    // input, determine the best matching token at each position, and collect all resulting highlight
    // regions in order.
    @Override
    public List<HighlightRegion> collectMatches(String text) {
        List<HighlightRegion> regions = new ArrayList<>();
        int index = 0;

        while (index < text.length()) {
            HighlightRegion bestMatch = findLongestMatchAt(text, index);

            if (bestMatch == null) {
                index++;
            } else {
                regions.add(bestMatch);
                index = bestMatch.end();
            }
        }

        return regions;
    }

    private HighlightRegion findLongestMatchAt(String text, int index) {
        HighlightRegion bestMatch = null;

        for (Token token : MiniJavaTokens.defaultTokens()) {
            List<HighlightRegion> matches = token.test(text);

            for (HighlightRegion match : matches) {
                if (match.start() != index) {
                    continue;
                }

                if (bestMatch == null || length(match) > length(bestMatch)) {
                    bestMatch = match;
                }
            }
        }

        return bestMatch;
    }

    private int length(HighlightRegion region) {
        return region.end() - region.start();
    }

    // TODO: Implement the identity function here.
    @Override
    public List<HighlightRegion> normalize(List<HighlightRegion> candidates) {
        return candidates;
    }
}
