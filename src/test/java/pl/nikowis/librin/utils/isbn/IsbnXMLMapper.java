package pl.nikowis.librin.utils.isbn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IsbnXMLMapper {

    public static final List<String> FIND_AND_PRINT_FULL_TITLES = Arrays.asList();
    public String entryFilePath;
    private List<BookDTO> books;
    private List<ISBNModel.Product> products;


    public IsbnXMLMapper(String entryFilePath) {
        this.entryFilePath = entryFilePath;
    }

    public List<BookDTO> map() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ISBNModel.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ISBNModel root = (ISBNModel) jaxbUnmarshaller.unmarshal(new File(entryFilePath));
        System.out.println("Records in ISBN db " + root.products.size());
        books = root.products.stream().filter(this::isDataCompleteAndValid).map(BookDTO::new).collect(Collectors.toList());
        return books;
    }

    public void save(String savePath) throws IOException {
        LibraryDTO library = new LibraryDTO(books);
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        writer.writeValue(new File(savePath), library);
    }

    private boolean isDataCompleteAndValid(ISBNModel.Product product) {
        ISBNModel.DescriptiveDetail descriptiveDet = product.DescriptiveDetailObject;
        if (descriptiveDet == null) return false;
        List<ISBNModel.Contributor> contributors = descriptiveDet.ContributorObject;
        if (contributors == null || contributors.size() < 1) return false;

        for (ISBNModel.Contributor c : contributors) {
            if (c.PersonNameInverted == null || c.ContributorRole == null) return false;
        }

        ISBNModel.TitleDetail titleDetail = descriptiveDet.TitleDetailObject;
        if (titleDetail == null) return false;
        ISBNModel.TitleElement titleElement = titleDetail.TitleElementObject;
        if (titleElement == null) return false;
        return titleElement.TitleText != null;
//        if(FIND_AND_PRINT_FULL_TITLES.size() > 0 && FIND_AND_PRINT_FULL_TITLES.contains(titleElement.TitleText.toLowerCase())) {
//            try {
//                System.out.println( new ObjectMapper().writeValueAsString(product));
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
