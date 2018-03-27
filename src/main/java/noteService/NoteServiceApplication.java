package noteService;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import noteService.repository.NoteRepository;
import noteService.repository.ClientRepository;
import noteService.entity.Note;
import noteService.entity.Client;
import noteService.utils.EncryptionUtils;


@SpringBootApplication
public class NoteServiceApplication implements CommandLineRunner{

	@Autowired ClientRepository clientRepository; 
	@Autowired NoteRepository noteRepository;
	@Autowired EncryptionUtils utils;
	
	public static void main(String[] args) {
		SpringApplication.run(NoteServiceApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		
		String encryptedPwd;
		
		encryptedPwd = utils.encrypt("Pgs");
		clientRepository.save(new Client("paul@gmail.com", "Me", encryptedPwd));
		
		encryptedPwd = utils.encrypt("Password");
		clientRepository.save(new Client("user@gmail.com", "Franz", encryptedPwd));
		
		
		noteRepository.save(new Note(null, "Learn Microservices", new Date(), "high", "paul@gmail.com"));
		noteRepository.save(new Note(null, "Buy new car", new Date(), "low", "paul@gmail.com"));
		noteRepository.save(new Note(null, "Order pizza", new Date(), "low", "paul@gmail.com"));
		
		noteRepository.save(new Note(null, "Feed animals", new Date(), "high", "user@gmail.com"));
		noteRepository.save(new Note(null, "Go to the gym", null, "low", "user@gmail.com"));

		
	}
}
