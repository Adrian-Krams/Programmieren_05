package highlighting.regex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import highlighting.core.HighlightRegion;
import java.util.List;
import org.junit.jupiter.api.Test;

class ScanningHighlighterTest {

    @Test
    void leererTextHatKeineTreffer() {
        ScanningHighlighter highlighter = new ScanningHighlighter();

        List<HighlightRegion> regions = highlighter.computeRegions("");

        assertTrue(regions.isEmpty());
    }

    @Test
    void textOhneTokenHatKeineTreffer() {
        ScanningHighlighter highlighter = new ScanningHighlighter();

        List<HighlightRegion> regions = highlighter.computeRegions("abc def ghi");

        assertTrue(regions.isEmpty());
    }

    @Test
    void erkenntKeywordImText() {
        ScanningHighlighter highlighter = new ScanningHighlighter();

        List<HighlightRegion> regions = highlighter.computeRegions("abc public xyz");

        assertEquals(1, regions.size());
        assertEquals(4, regions.get(0).start());
        assertEquals(10, regions.get(0).end());
    }

    @Test
    void kommentarWirdVorKeywordsBevorzugt() {
        ScanningHighlighter highlighter = new ScanningHighlighter();

        String text = "// public class return";
        List<HighlightRegion> regions = highlighter.computeRegions(text);

        assertEquals(1, regions.size());
        assertEquals(0, regions.get(0).start());
        assertEquals(text.length(), regions.get(0).end());
    }

    @Test
    void laengstesMatchGewinntBeiJavadoc() {
        ScanningHighlighter highlighter = new ScanningHighlighter();

        String text = "/** public class */";
        List<HighlightRegion> regions = highlighter.computeRegions(text);

        assertEquals(1, regions.size());
        assertEquals(0, regions.get(0).start());
        assertEquals(text.length(), regions.get(0).end());
    }

    @Test
    void benachbarteTrefferWerdenBeideErkannt() {
        ScanningHighlighter highlighter = new ScanningHighlighter();

        List<HighlightRegion> regions = highlighter.computeRegions("\"a\"'b'");

        assertEquals(2, regions.size());
        assertEquals(0, regions.get(0).start());
        assertEquals(3, regions.get(0).end());
        assertEquals(3, regions.get(1).start());
        assertEquals(6, regions.get(1).end());
    }

    @Test
    void normalizeGibtEingabeUnveraendertZurueck() {
        ScanningHighlighter highlighter = new ScanningHighlighter();
        List<HighlightRegion> input = highlighter.collectMatches("public");

        List<HighlightRegion> result = highlighter.normalize(input);

        assertTrue(input == result);
    }
}
