package lk.linex.emloyeems.repo;

import lk.linex.emloyeems.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
    List<Employee> findByNameContaining(String name);
    List<Employee> findByDepartmentContaining(String department);
    List<Employee> findByNameContainingOrDepartmentContaining(String name, String department);
}
