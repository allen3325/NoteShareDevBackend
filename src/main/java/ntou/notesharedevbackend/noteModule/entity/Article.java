package ntou.notesharedevbackend.noteModule.entity;

public class Article {
    private String article;
    private Double plagiarismPercent;
    private Double quotePercent;

    public Article() {
    }
    public Article(String content) {
        this.article = cleanArticle(content);
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public Double getPlagiarismPercent() {
        return plagiarismPercent;
    }

    public void setPlagiarismPercent(Double plagiarismPercent) {
        this.plagiarismPercent = plagiarismPercent;
    }

    public Double getQuotePercent() {
        return quotePercent;
    }

    public void setQuotePercent(Double quotePercent) {
        this.quotePercent = quotePercent;
    }

    public String cleanArticle(String article) {
        // remove HTML tag -> replace useless space into one space -> all English into UpperCase
        String containHtmlTagRegex = "<.*?>";
        String containUselessSpaceRegex = "\\s{2,}";
        article = article.replaceAll(containHtmlTagRegex, "     ");
        article = article.replaceAll(containUselessSpaceRegex, " ");
        article = article.replaceAll(System.lineSeparator(),"");
        article = article.toUpperCase();
        return article;
    }

}
