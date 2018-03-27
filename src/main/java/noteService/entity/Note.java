package noteService.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="notes")
public class Note {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name="description")
	@NotNull @NotBlank @NotEmpty
	private String description;
	
	@Column(name="date")
	private Date date;

	@Column(name="priority")
	@NotNull @NotBlank @NotEmpty
	private String priority;
	
	@Column(name="fk_Client")
	@NotNull @NotBlank @NotEmpty
	private String fkClient;
	
	@PrePersist
	void getTimeOperation() {
		this.date = new Date();
	}

	public Note(Integer id, String description, Date date, String priority, String fkClient) {
		super();
		this.id = id;
		this.description = description;
		this.date = date;
		this.priority = priority;
		this.fkClient = fkClient;
	}
	
	public Note() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getFkClient() {
		return fkClient;
	}

	public void setFkUser(String fkClient) {
		this.fkClient = fkClient;
	}
	
}