package noteService.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import noteService.repository.ClientRepository;
import noteService.entity.Client;
import noteService.utils.EncryptionUtils;
import noteService.utils.JsonWebTokenUtils;
import noteService.utils.UserNotInDatabaseException;
import noteService.utils.UserNotLoggedException;

@Component
public class LoginService {

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private EncryptionUtils encUtils;
	
	@Autowired
	JsonWebTokenUtils jwtUtils;
	
	public Optional<Client> getUserFromDb(String email, String password) throws UserNotInDatabaseException {
		Optional<Client> userr = clientRepository.findClientByEmail(email);
		if(userr.isPresent()) {
			Client user = userr.get();
			if(! encUtils.decrypt(user.getPassword()).equals(password)) {
				throw new UserNotInDatabaseException("Wrong email or password");
			}
		}
		return userr;
	}

	public String createJwt(String email, String name, Date date) throws UnsupportedEncodingException {
		date.setTime(date.getTime() + (300*1000));
		return jwtUtils.generateJwt(email, name, date);
	}

    public Map<String, Object> verifyJwtAndGetData(HttpServletRequest request) throws UserNotLoggedException{
        String jwt = jwtUtils.getJwtFromHttpRequest(request);
        if(jwt == null){
            throw new UserNotLoggedException("User not logged! Login first.");
        }
        try {
			return jwtUtils.jwt2Map(jwt);
		} catch (ExpiredJwtException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }

}