package pl.karollisiewicz.cinema.app.data.source.web.review;

import java.io.Serializable;

public class Review implements Serializable {
    private static final long serialVersionUID = 1811043115020551645L;
    private String id;
    private String author;
    private String contetn;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContetn() {
        return contetn;
    }

    public void setContetn(String contetn) {
        this.contetn = contetn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
