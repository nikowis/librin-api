package pl.nikowis.librin.utils.isbn;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

import static pl.nikowis.librin.utils.ISBNMain.CSV_SEPARATOR;

@Data
@NoArgsConstructor
public class BookDTO {


    public static final String STR_SGN = "\"";
    public static final String NULL = "null";
    private String id;
    private String author;
    private String title;
    private String subtitle;
    private String language;
    private String datestamp;
    private String form; //http://www.onix-codelists.io/codelist/150
    public List<Contributor> contributors = new LinkedList<>();

    public BookDTO(ISBNModel.Product product) {
        ISBNModel.DescriptiveDetail descriptiveDet = product.DescriptiveDetailObject;
        List<ISBNModel.Contributor> isbnContribs = descriptiveDet.ContributorObject;
        this.author = null;
        this.title = descriptiveDet.TitleDetailObject.TitleElementObject.TitleText;
        this.subtitle = descriptiveDet.TitleDetailObject.TitleElementObject.Subtitle;
        this.language = descriptiveDet.LanguageObject != null ? descriptiveDet.LanguageObject.LanguageCode : null;
        this.datestamp = product.datestamp;
        this.form = descriptiveDet.getProductForm();

        for(ISBNModel.Contributor c: isbnContribs) {
            contributors.add(new Contributor(c));
        }

        List<ISBNModel.ProductIdentifier> productIds = product.ProductIdentifierObject;
        if (productIds != null && productIds.size() > 0) {
            ISBNModel.ProductIdentifier pid = productIds.get(0);
            id = pid.IDValue;
        }
    }

    public String toCsvRecord() {
        return STR_SGN + id +  STR_SGN + CSV_SEPARATOR
                + (author != null ? STR_SGN  + author.replace("'", "''").replace("\"", "'\"") + STR_SGN : NULL) + CSV_SEPARATOR
                + (title != null ? STR_SGN  + title.replace("'", "''").replace("\"", "'\"") + STR_SGN : NULL) + CSV_SEPARATOR
                + (subtitle != null ? STR_SGN  + subtitle.replace("'", "''").replace("\"", "'\"") + STR_SGN : NULL) + CSV_SEPARATOR
                + (datestamp != null ? STR_SGN  + datestamp + STR_SGN : NULL);
    }

    @Override
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", language='" + language + '\'' +
                ", datestamp='" + datestamp + '\'' +
                ", form='" + form + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        BookDTO book = (BookDTO) o;

        if (!title.toLowerCase().equals(book.title.toLowerCase())) return false;
        if (!author.toLowerCase().equals(book.author.toLowerCase())) return false;
        return subtitle != null ? subtitle.toLowerCase().equals(book.subtitle != null ? book.subtitle.toLowerCase() : null) : book.subtitle == null;
    }

    @Override
    public int hashCode() {
        int result = author.toLowerCase().hashCode();
        result = 31 * result + title.toLowerCase().hashCode();
        return result;
    }

    @Data
    @NoArgsConstructor
    public static class Contributor {
        private String name;
        public String role;

        public Contributor(ISBNModel.Contributor c) {
            name = c.getPersonNameInverted();
            role = c.getContributorRole();
        }
    }
}
