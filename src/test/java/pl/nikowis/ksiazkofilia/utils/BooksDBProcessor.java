package pl.nikowis.ksiazkofilia.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BooksDBProcessor {

    public static final List<String> INCORRECT_AUTHORS = Arrays.asList("Autor anonimowy", "Publikacja zbiorowa", "Autor anonimowy,", "Publikacja zbiorowa,");
    public static int errCounter = 0;

    public static void main(String[] args) throws IOException, JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(RootISBN.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        RootISBN root = (RootISBN) jaxbUnmarshaller.unmarshal(new File("C:\\Users\\nikow\\Desktop\\ksiazkofilia-api\\src\\test\\java\\pl\\nikowis\\ksiazkofilia\\utils\\bazaISBN.xml"));
        System.out.println("Records in db " + root.products.size());

        List<RootISBN.Product> products = root.products;
        List<Book> books = products.stream().filter(BooksDBProcessor::isDataComplete).map(Book::new).collect(Collectors.toList());
        System.out.println("Books with title and author " + books.size());
        Library library = new Library(books);
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        writer.writeValue(new File("C:\\Users\\nikow\\Desktop\\ksiazkofilia-api\\src\\test\\java\\pl\\nikowis\\ksiazkofilia\\utils\\library.json"), library);

        List<String> distinctTitles = books.stream().map(book -> book.title).distinct().collect(Collectors.toList());
        List<String> distinctAuthors = books.stream().map(book -> book.author).distinct().collect(Collectors.toList());
        System.out.println("Distinct authors: " + distinctAuthors.size());
        System.out.println("Distinct titles: " + distinctTitles.size());
        System.out.println("Error counter: " + errCounter);
    }

    private static boolean isDataComplete(RootISBN.Product product) {
        RootISBN.DescriptiveDetail descriptiveDet = product.DescriptiveDetailObject;
        if (descriptiveDet == null) return false;
        List<RootISBN.Contributor> contributor = descriptiveDet.ContributorObject;
        if (contributor == null || contributor.size() < 1) return false;
        RootISBN.TitleDetail titleDetail = descriptiveDet.TitleDetailObject;
        if (titleDetail == null) return false;
        RootISBN.TitleElement titleElement = titleDetail.TitleElementObject;
        if (titleElement == null) return false;
        if (titleElement.TitleText == null) return false;
        if (contributor.get(0).PersonNameInverted == null) return false;
        if (INCORRECT_AUTHORS.contains(contributor.get(0).PersonNameInverted)) return false;
        return true;
    }

    @Data
    public static class Library {
        private List<Book> books;

        public Library() {
        }

        public Library(List<Book> books) {
            this.books = books;
        }

    }

    @Data
    @NoArgsConstructor
    public static class Book {
        private String id;
        private String author;
        private String title;
        private String subtitle;
        private String language;
        private String datestamp;

        public Book(RootISBN.Product product) {
            RootISBN.DescriptiveDetail descriptiveDet = product.DescriptiveDetailObject;
            List<RootISBN.Contributor> contributors = descriptiveDet.ContributorObject;
            this.author = processAuthorName(contributors.get(0).PersonNameInverted);
            this.title = descriptiveDet.TitleDetailObject.TitleElementObject.TitleText;
            this.subtitle = descriptiveDet.TitleDetailObject.TitleElementObject.Subtitle;
            this.language = descriptiveDet.LanguageObject != null ? descriptiveDet.LanguageObject.LanguageCode : null;
            this.datestamp = product.datestamp;

            List<RootISBN.ProductIdentifier> productIds = product.ProductIdentifierObject;
            if (productIds != null && productIds.size() > 0) {
                RootISBN.ProductIdentifier pid = productIds.get(0);
                id = pid.IDValue;
            }
        }

        private String processAuthorName(String personNameInverted) {
            String result = personNameInverted;
            if (result != null && result.contains(",")) {
                String[] split = result.split(",");
                if (split.length == 1) {
                    result = split[0].trim();
                } else if (split.length == 2) {
                    result = split[1].trim() + " " + split[0];
                } else {
//                    System.out.println(result);
                    errCounter++;
                }
            }
            return result;
        }
    }

}
