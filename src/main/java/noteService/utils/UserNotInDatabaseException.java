package noteService.utils;

public class UserNotInDatabaseException extends Exception{

	public UserNotInDatabaseException(String message) {
		super(message);
	}
	
}