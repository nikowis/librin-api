package pl.nikowis.librin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.model.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    //    https://stackoverflow.com/questions/58621139/spring-jpa-define-sql-function-for-usage-in-custom-query
    @Query(nativeQuery = true
            , value = "SELECT DISTINCT author FROM book WHERE lower(public.f_unaccent(author)) LIKE lower(public.f_unaccent(CONCAT('%', ?1,'%'))) ORDER BY author LIMIT 10")
    List<String> findAuthorsByQuery(@Param("query") String query);

    @Query(nativeQuery = true
            , value = "SELECT * FROM book WHERE lower(public.f_unaccent(title)) LIKE lower(public.f_unaccent(CONCAT('%', ?1,'%'))) ORDER BY datestamp DESC LIMIT 10")
    List<Book> findBookByQuery(@Param("query") String query);

    @Query(nativeQuery = true
            , value = "SELECT * FROM book WHERE lower(public.f_unaccent(author)) LIKE lower(public.f_unaccent(?1)) AND " +
            " lower(public.f_unaccent(title)) LIKE lower(public.f_unaccent(CONCAT('%', ?2,'%'))) " +
            " ORDER BY datestamp DESC LIMIT 10")
    List<Book> findBookByAuthorAndQuery(@Param("author") String author, @Param("query") String query);
}
