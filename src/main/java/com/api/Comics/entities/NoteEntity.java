package com.api.Comics.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "Notes")
public class NoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NoteID", insertable = false, updatable = false)
    private int NoteID;

    @Column(name = "ComicID")
    private int ComicID;

    @Column(name = "Notes")
    private String Notes;

    //The values for RecordCreationDate and LastUpdated are handled on the DB side.
    @Column(name = "RecordCreationDate", insertable = false, updatable = false)
    private Timestamp recordCreationDate;

    @Column(name = "LastUpdated", insertable = false, updatable = false)
    private Timestamp lastUpdated;
}
