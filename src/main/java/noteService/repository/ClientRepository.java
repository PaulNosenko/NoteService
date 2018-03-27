package noteService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import noteService.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, String>{

	Optional<Client> findClientByEmail(String email);

	Optional<Client> findClientByName(String name);
}
