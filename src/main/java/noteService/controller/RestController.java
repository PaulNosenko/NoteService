package noteService.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.jsonwebtoken.ExpiredJwtException;
import noteService.repository.NoteRepository;
import noteService.repository.ClientRepository;
import noteService.entity.Note;
import noteService.entity.Client;
import noteService.service.LoginService;
import noteService.service.NoteService;
import noteService.utils.EncryptionUtils;
import noteService.utils.JsonResponseBody;
import noteService.utils.NoteValidator;
import noteService.utils.UserNotInDatabaseException;
import noteService.utils.UserNotLoggedException;

@org.springframework.web.bind.annotation.RestController
public class RestController {

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	NoteRepository noteDao;

	@Autowired
	EncryptionUtils utils;

	@Autowired
	NoteService noteService;
	
	@Autowired
	LoginService loginService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<JsonResponseBody> login(@RequestParam(value = "email") String email, @RequestParam(value = "password") String pwd) {

		try {
			Optional<Client> userr = loginService.getUserFromDb(email, pwd);
			if (userr.isPresent()) {
				Client user = userr.get();
				String jwt = loginService.createJwt(email, user.getName(), new Date());
				return ResponseEntity.status(HttpStatus.OK).header("jwt", jwt)
						.body(new JsonResponseBody(HttpStatus.OK.value(), "Success! You Logged In"));
			}
		} catch (UserNotInDatabaseException e1) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "No corrispondence inf db for users"));
		} catch (UnsupportedEncodingException e2) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new JsonResponseBody(HttpStatus.BAD_REQUEST.value(), "Forbidden " + e2.toString()));
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Problems during Log in"));
	}

	@RequestMapping("/showNotes")
	public ResponseEntity<JsonResponseBody> showNotes(HttpServletRequest request) {
		
		try {
			Map<String, Object> userData = loginService.verifyJwtAndGetData(request);
			return ResponseEntity.status(HttpStatus.OK).body( new JsonResponseBody(HttpStatus.OK.value(), noteService.getNotes((String) userData.get("email"))));
		} catch (UserNotLoggedException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Forbidden " + e.toString()));
		} catch(ExpiredJwtException e3){
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new JsonResponseBody(HttpStatus.GATEWAY_TIMEOUT.value(), "Session Expired!: " + e3.toString()));
        } 
	}
	
	@RequestMapping(value="/newNote", method = RequestMethod.POST)
	public ResponseEntity<JsonResponseBody> newNote(HttpServletRequest request, @Valid Note toDo, BindingResult result){
		
		NoteValidator validator = new NoteValidator();
		validator.validate(toDo, result);
		if(result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new JsonResponseBody(HttpStatus.BAD_GATEWAY.value(), "Errors " + result.toString()));
		}
		
		try {
			loginService.verifyJwtAndGetData(request);			
			return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseBody(HttpStatus.OK.value(), noteService.addNote(toDo)));
		}catch(UserNotLoggedException e1){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "User not logged! Login first : " + e1.toString()));
        }catch(ExpiredJwtException e3){
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new JsonResponseBody(HttpStatus.GATEWAY_TIMEOUT.value(), "Session Expired!: " + e3.toString()));
        }
	}
	
	@RequestMapping(value="/deleteNote/{id}")
	public ResponseEntity<JsonResponseBody> deleteNote(HttpServletRequest request, @PathVariable(value="id") Integer id){
		try {
			loginService.verifyJwtAndGetData(request);
			noteService.deleteNote(id);
			return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseBody(HttpStatus.OK.value(), "ToDo correctly deleted"));
		} catch (UserNotLoggedException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Forbidden " + e.toString()));
		}catch(ExpiredJwtException e3){
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new JsonResponseBody(HttpStatus.GATEWAY_TIMEOUT.value(), "Session Expired!: " + e3.toString()));
        }
		
	}

}
