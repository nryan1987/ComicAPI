package com.api.Comics.constants;

public final class ApplicationConstants {
    public static final String findAllWithNotes = "SELECT *," +
            " (SELECT GROUP_CONCAT(Notes SEPARATOR ', ') FROM Notes WHERE Notes.ComicID=Comics.ComicID ORDER BY Notes.Notes) AS Notes"
            + " FROM `Comics`"
            + " ORDER BY Title, Volume, Issue, Notes";

    public static final String searchQuery = "SELECT *," +
            " (SELECT GROUP_CONCAT(Notes SEPARATOR ', ') FROM Notes WHERE Notes.ComicID=Comics.ComicID ORDER BY Notes.Notes) AS Notes"
            + " FROM `Comics`"
            + " HAVING ComicID LIKE CONCAT('%', :searchTerm, '%')"
            + " OR title LIKE CONCAT('%', :searchTerm, '%')"
            + " OR issue LIKE CONCAT('%', :searchTerm, '%')"
            + " OR publisher LIKE CONCAT('%', :searchTerm, '%')"
            + " OR Notes LIKE CONCAT('%', :searchTerm, '%')"
            + " ORDER BY Title, Volume, Issue, Notes";
}
