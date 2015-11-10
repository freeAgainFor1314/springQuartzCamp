package pl.java.scalatech.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.java.scalatech.domain.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

}
