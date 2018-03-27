package noteService.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import noteService.repository.NoteRepository;
import noteService.entity.Note;

@Service
public class NoteService {

	@Autowired
	NoteRepository noteRepository;
	
	public List<Note> getNotes(String email) {
		return noteRepository.findByFkClient(email);
	}

	public Note addNote(Note note) {
		return noteRepository.save(note);
	}

	public void deleteNote(Integer id) {
		noteRepository.delete(id);
	}

}
