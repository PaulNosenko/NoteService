package noteService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import noteService.entity.Client;
import noteService.entity.Note;
import noteService.repository.ClientRepository;
import noteService.repository.NoteRepository;
import noteService.utils.EncryptionUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteServiceApplicationTests {

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	NoteRepository noteRepository;
	
	@Autowired
	EncryptionUtils encUtils;
	
	@Test
	public void contextLoads() {
	}

	@Test
	public void testCreate() {
		Client client = new Client("mail@gmail.com", "Name", encUtils.encrypt("Password"));
		Note note = new Note(null, "description", null, "high", "mail@gmail.com");
		clientRepository.save(client);
		noteRepository.save(note);
	}
	@Test
	public void testRead(){
		Note note = noteRepository.findOne(1);
		assertNotNull(note);
		assertEquals("Learn Microservices", note.getDescription());
	}
	@Test
	public void testDelete(){
		noteRepository.delete(1);
	}
	
}
