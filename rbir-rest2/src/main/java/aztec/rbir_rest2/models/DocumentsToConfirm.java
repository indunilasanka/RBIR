package aztec.rbir_rest2.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by asankai on 15/11/2017.
 */
@JsonSerialize
public class DocumentsToConfirm {
    private long id;
    private DocumentModel document;

    public DocumentsToConfirm(long id, DocumentModel document){
        this.id = id;
        this.document = document;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DocumentModel getDocument() {
        return document;
    }

    public void setDocument(DocumentModel document) {
        this.document = document;
    }
}
