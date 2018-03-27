package noteService.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import noteService.entity.Note;

public interface NoteRepository  extends JpaRepository<Note, Integer>{
	
	List<Note> findByFkClient(String email);


}