import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Text {
    public static String text() {
        String paragraph = "earlier the engineering field only consisted of core branches that specialized in" +
                " individual departments of work and the divisions were mechanical electrical and civil but eventually" +
                " with much more advanced and discoveries in the field of technology and a combination of engineering";

        // Shuffle the words in the paragraph and return
        return shuffleWordsInParagraph(paragraph);
    }

    public static String shuffleWordsInParagraph(String paragraph) {
        // Split the paragraph into words
        List<String> words = Arrays.asList(paragraph.split("\\s+"));

        // Shuffle the words
        Collections.shuffle(words);

        // Join the words back into a paragraph
        return words.stream().collect(Collectors.joining(" "));
    }
}

