package pl.karollisiewicz.cinema.domain.movie.review;

import android.support.annotation.NonNull;

import java.io.Serializable;

public final class Review implements Serializable {
    private static final long serialVersionUID = 2210182568996836073L;
    private final String author;
    private final String content;
    private final String url;

    private Review(@NonNull final Builder builder) {
        author = builder.author;
        content = builder.content;
        url = builder.url;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public static final class Builder {
        private String author;
        private String content;
        private String url;

        public Builder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Review build() {
            return new Review(this);
        }
    }
}
