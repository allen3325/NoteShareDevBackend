package ntou.notesharedevbackend.noteModule;

import ntou.notesharedevbackend.noteModule.entity.Article;

import java.util.ArrayList;
import java.util.LinkedList;

public class PlagiarismChecker {

    public ArrayList<String> getLlsArray(String comparison, String self) {
        Article article1 = new Article(comparison);
        Article article2 = new Article(self);
        final String containsEngRegex = " *[a-zA-Z]+ *";
        int equalTextLengthParam;
        ArrayList<String> similarParagraph = new ArrayList<String>();
        // 1. get articles
        String text1 = article1.getArticle();
        String text2 = article2.getArticle();
        // 2. diff_match_patch
        diff_match_patch dmp = new diff_match_patch();
        LinkedList<diff_match_patch.Diff> diff2 = dmp.diff_main(text1, text2);
        // 3. deal with diff_match_patch's result
        int longestSimilarParagraph = 0;
        for (diff_match_patch.Diff di : diff2) {
            // 3-1. get EQUAL result and get lls
            if (di.operation.toString().equals("EQUAL")) {
                String tmp = di.text.replaceAll(System.lineSeparator(),"");
                if(tmp.length()>6){
                    similarParagraph.add(tmp);
                }
                if (di.text.length() > longestSimilarParagraph && di.text.length() > 1) {
                    longestSimilarParagraph = di.text.length();
                }
            }
        }
        // 4. calculate result
        // 5. print result
        return similarParagraph;
    }

    public int getArticleLengthDivide(String comparison,String lls){
        Article article1 = new Article(comparison);
        Article article2 = new Article(lls);
        // clean articles
        String text1 = article1.getArticle();
        String text2 = article2.getArticle();
        return text2.length();
    }

    public float getTotalTextEqual(String comparison,String lls){
        Article article1 = new Article(comparison);
        Article article2 = new Article(lls);
        final String containsEngRegex = " *[a-zA-Z]+ *";
        int equalTextLengthParam;
        float totalTextEqual = 0F;
        // 1. clean articles
        String text1 = article1.getArticle();
        String text2 = article2.getArticle();
        // 2. diff_match_patch
        diff_match_patch dmp = new diff_match_patch();
        LinkedList<diff_match_patch.Diff> diff2 = dmp.diff_main(text1, text2);
        // 3. deal with diff_match_patch's result
        for (diff_match_patch.Diff di : diff2) {
            // 3-1. if is not English(purpose that is Chinese), count it if its length longer than 6
            if (di.text.matches(containsEngRegex)) {
                equalTextLengthParam = 2;
            } else {
                equalTextLengthParam = 6;
            }
            // 3-2. get totalTextEqual
            if (di.operation.toString().equals("EQUAL")) {
                if (di.text.length() >= equalTextLengthParam) {
                    totalTextEqual += di.text.length();
                }
            }
        }
        return totalTextEqual;
    }

    public float getPlagiarism(float totalTextEqual,int length){
        return (float) totalTextEqual/length;
    }


}
