package noteService.utils;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import noteService.entity.Note;

public class NoteValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Note.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Note toDo = (Note) obj;

        String priority = toDo.getPriority();

        if(!"high".equals(priority) && !"low".equals(priority)){
            errors.rejectValue("priority", "Priority must be 'high' or 'low'!");
        }
	}
}
