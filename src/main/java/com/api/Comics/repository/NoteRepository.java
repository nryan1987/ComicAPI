package com.api.Comics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.api.Comics.entities.NoteEntity;

public interface NoteRepository extends CrudRepository<NoteEntity, Integer> {
	@Query(value = "SELECT * FROM Notes n WHERE ComicID=?1", nativeQuery = true)
	List<NoteEntity> findNotesByComicID(Integer comicID);
}
