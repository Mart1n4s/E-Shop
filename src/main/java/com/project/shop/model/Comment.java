package com.project.shop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String commentTitle;
    private String commentBody;
    private LocalDate dateCreated;

    @ManyToOne
    private Cart cart;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> replies;
    @ManyToOne
    private Comment parentComment;

    public Comment(String commentTitle, String commentBody) {
        this.commentTitle = commentTitle;
        this.commentBody = commentBody;
        this.dateCreated = LocalDate.now();
    }

    public Comment(String commentTitle, Comment parentComment) {
        this.commentTitle = commentTitle;
        this.parentComment = parentComment;
        this.dateCreated = LocalDate.now();
    }

    public Comment(String commentTitle) {
        this.commentTitle = commentTitle;
        this.dateCreated = LocalDate.now();
    }

    @Override
    public String toString() {
        return commentTitle + " " + dateCreated;
    }
}


