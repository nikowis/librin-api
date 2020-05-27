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
import java.util.List;
import java.util.stream.Collectors;

public class BooksDBProcessor {

    public static void main(String[] args) throws IOException, JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(RootISBN.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        RootISBN root = (RootISBN) jaxbUnmarshaller.unmarshal(new File("C:\\Users\\nikow\\Desktop\\ksiazkofilia-api\\src\\test\\java\\pl\\nikowis\\ksiazkofilia\\utils\\bazaISBN.xml"));
        System.out.println(root.products.size());

        List<RootISBN.Product> products = root.products;
        List<Book> books = products.stream().filter(BooksDBProcessor::isDataComplete).map(Book::new).collect(Collectors.toList());
        System.out.println("Books " + books.size());
        Library library = new Library(books);

        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        writer.writeValue(new File("C:\\Users\\nikow\\Desktop\\ksiazkofilia-api\\src\\test\\java\\pl\\nikowis\\ksiazkofilia\\utils\\library.json"), library);
    }

    private static boolean isDataComplete(RootISBN.Product product) {
        RootISBN.DescriptiveDetail descriptiveDet = product.DescriptiveDetailObject;
        if(descriptiveDet == null) return false;
        RootISBN.Contributor contributor = descriptiveDet.ContributorObject;
        if(contributor == null) return false;
        RootISBN.TitleDetail titleDetail = descriptiveDet.TitleDetailObject;
        if(titleDetail == null) return false;
        RootISBN.TitleElement titleElement = titleDetail.TitleElementObject;
        if(titleElement == null) return false;
        if(titleElement.TitleText == null)return false;
        if(contributor.PersonNameInverted == null)return false;
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
        private String author;
        private String title;

        public Book(RootISBN.Product product) {
            RootISBN.DescriptiveDetail descriptiveDet = product.DescriptiveDetailObject;
            this.author = descriptiveDet.ContributorObject.PersonNameInverted;
            this.title = descriptiveDet.TitleDetailObject.TitleElementObject.TitleText;
        }
    }

}
