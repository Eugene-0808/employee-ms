package lk.linex.emloyeems.repo;

import lk.linex.emloyeems.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
    // Changed 'Name' to 'EmpName' to match the Entity field 'empName'
    List<Employee> findByEmpNameContaining(String name);

    List<Employee> findByDepartmentContaining(String department);

    // Changed 'Name' to 'EmpName' here as well
    List<Employee> findByEmpNameContainingOrDepartmentContaining(String name, String department);
}
