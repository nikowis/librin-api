package pl.nikowis.ksiazkofilia.utils;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ONIXMessage")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class RootISBN {

    @XmlElement(name = "Product")
    List<Product> products = new ArrayList<>();

    @XmlElement(name = "Header")
    Header header;

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class Header {
        @XmlElement(name = "Sender")
        Sender Sender;
        String MessageNumber;
        String SentDateTime;
        String MessageNote;
        String DefaultLanguageOfText;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class Sender {
        String SenderName;
        String ContactName;
        String EmailAddress;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class Product {
        String RecordReference;
        String NotificationType;
        String RecordSourceType;
        String RecordSourceName;
        @XmlElement(name = "ProductIdentifier")
        ProductIdentifier ProductIdentifierObject;
        @XmlElement(name = "DescriptiveDetail")
        DescriptiveDetail DescriptiveDetailObject;
        @XmlElement(name = "CollateralDetail")
        CollateralDetail CollateralDetailObject;
        @XmlElement(name = "PublishingDetail")
        PublishingDetail PublishingDetailObject;
        @XmlElement(name = "ProductSupply")
        ProductSupply ProductSupplyObject;
        String _datestamp;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ProductSupply {
        @XmlElement(name = "SupplyDetail")
        SupplyDetail SupplyDetailObject;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SupplyDetail {
        @XmlElement(name = "Supplier")
        Supplier SupplierObject;
        String ProductAvailability;
        String UnpricedItemType;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Supplier {
        String SupplierRole;
        String SupplierName;
        String TelephoneNumber;
        String EmailAddress;
        @XmlElement(name = "Website")
        Website WebsiteObject;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Website {
        String WebsiteLink;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PublishingDetail {
        @XmlElement(name = "Imprint")
        Imprint ImprintObject;
        @XmlElement(name = "Publisher")
        Publisher PublisherObject;
        String CityOfPublication;
        String CountryOfPublication;
        @XmlElement(name = "PublishingDate")
        ArrayList<PublishingDate> PublishingDate = new ArrayList<>();
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PublishingDate {
        String PublishingDateRole;
        String Date;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Publisher {
        String PublishingRole;
        String PublisherName;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Imprint {
        String ImprintName;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CollateralDetail {
        @XmlElement(name = "TextContent")
        ArrayList<TextContent> TextContent = new ArrayList<>();
        @XmlElement(name = "SupportingResource")
        SupportingResource SupportingResourceObject;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TextContent {
        String ContentAudience;
        String Text;
        String TextType;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SupportingResource {
        String ResourceContentType;
        String ContentAudience;
        String ResourceMode;
        @XmlElement(name = "ResourceVersion")
        ResourceVersion ResourceVersionObject;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ResourceVersion {
        String ResourceForm;
        String ResourceLink;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DescriptiveDetail {
        String ProductComposition;
        String ProductForm;
        @XmlElement(name = "ProductFormFeature")
        ArrayList<ProductFormFeature> ProductFormFeature = new ArrayList<>();
        String PrimaryContentType;
        String ProductContentType;
        @XmlElement(name = "TitleDetail")
        TitleDetail TitleDetailObject;
        @XmlElement(name = "Contributor")
        Contributor ContributorObject;
        String EditionType;
        String EditionNumber;
        @XmlElement(name = "Language")
        Language LanguageObject;
        @XmlElement(name = "Extent")
        Extent ExtentObject;
        @XmlElement(name = "Subject")
        Subject SubjectObject;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ProductFormFeature {
        String ProductFormFeatureType;
        String ProductFormFeatureValue;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Subject {
        String SubjectSchemeIdentifier;
        String SubjectCode;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Extent {
        String ExtentType;
        String ExtentValue;
        String ExtentUnit;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Language {
        String LanguageRole;
        String LanguageCode;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Contributor {
        String SequenceNumber;
        String ContributorRole;
        String PersonNameInverted;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TitleDetail {
        String TitleType;
        @XmlElement(name = "TitleElement")
        TitleElement TitleElementObject;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TitleElement {
        String TitleElementLevel;
        String YearOfAnnual;
        String TitleText;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ProductIdentifier {
        String ProductIDType;
        String IDValue;
    }
}
