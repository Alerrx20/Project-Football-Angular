package ale.atos.apiFootball.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ale.atos.apiFootball.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByEmail(String email);
	
}
