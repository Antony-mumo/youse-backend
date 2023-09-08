package com.company.youse.repositrories;

import com.company.youse.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    /**
     * method finds a document by its name
     * @param documentName
     * @return
     */
    Document findTopByDocumentName(String documentName) ;
}
