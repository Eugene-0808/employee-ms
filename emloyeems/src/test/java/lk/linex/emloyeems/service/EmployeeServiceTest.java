package lk.linex.emloyeems.service;

import lk.linex.emloyeems.dto.EmployeeDTO;
import lk.linex.emloyeems.entity.Employee;
import lk.linex.emloyeems.repo.EmployeeRepo;
import lk.linex.emloyeems.util.VarList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepo employeeRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeDTO employeeDTO;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employeeDTO = new EmployeeDTO(1, "John Doe", "123 Main St", "1234567890", "IT", "john.doe@example.com", 30, 50000.0);
        employee = new Employee(1, "John Doe", "123 Main St", "1234567890", "IT", "john.doe@example.com", 30, 50000.0);
    }

    @Test
    void testSaveEmployee_Success() {
        // Arrange
        when(employeeRepo.existsById(anyInt())).thenReturn(false);
        when(modelMapper.map(employeeDTO, Employee.class)).thenReturn(employee);

        // Act
        String result = employeeService.saveEmployee(employeeDTO);

        // Assert
        assertEquals(VarList.RSP_SUCCESS, result);
        verify(employeeRepo).save(employee);
    }

    @Test
    void testSaveEmployee_Duplicate() {
        // Arrange
        when(employeeRepo.existsById(anyInt())).thenReturn(true);

        // Act
        String result = employeeService.saveEmployee(employeeDTO);

        // Assert
        assertEquals(VarList.RSP_DUPLICATED, result);
        verify(employeeRepo, never()).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee_Success() {
        // Arrange
        when(employeeRepo.existsById(anyInt())).thenReturn(true);
        when(modelMapper.map(employeeDTO, Employee.class)).thenReturn(employee);

        // Act
        String result = employeeService.updateEmployee(employeeDTO);

        // Assert
        assertEquals(VarList.RSP_SUCCESS, result);
        verify(employeeRepo).save(employee);
    }

    @Test
    void testUpdateEmployee_NoDataFound() {
        // Arrange
        when(employeeRepo.existsById(anyInt())).thenReturn(false);

        // Act
        String result = employeeService.updateEmployee(employeeDTO);

        // Assert
        assertEquals(VarList.RSP_NO_DATA_FOUND, result);
        verify(employeeRepo, never()).save(any(Employee.class));
    }

    @Test
    void testGetAllEmployee() {
        // Arrange
        List<Employee> employees = List.of(employee);
        List<EmployeeDTO> employeeDTOs = List.of(employeeDTO);
        when(employeeRepo.findAll()).thenReturn(employees);
        when(modelMapper.map(employees, new TypeToken<ArrayList<EmployeeDTO>>(){}.getType())).thenReturn(employeeDTOs);

        // Act
        List<EmployeeDTO> result = employeeService.getAllEmployee();

        // Assert
        assertEquals(employeeDTOs, result);
    }

    @Test
    void testSearchEmployee_Found() {
        // Arrange
        when(employeeRepo.existsById(1)).thenReturn(true);
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(modelMapper.map(employee, EmployeeDTO.class)).thenReturn(employeeDTO);

        // Act
        EmployeeDTO result = employeeService.searchEmployee(1);

        // Assert
        assertEquals(employeeDTO, result);
    }

    @Test
    void testSearchEmployee_NotFound() {
        // Arrange
        when(employeeRepo.existsById(1)).thenReturn(false);

        // Act
        EmployeeDTO result = employeeService.searchEmployee(1);

        // Assert
        assertNull(result);
    }

    @Test
    void testDeleteEmployee_Success() {
        // Arrange
        when(employeeRepo.existsById(1)).thenReturn(true);

        // Act
        String result = employeeService.deleteEmployee(1);

        // Assert
        assertEquals(VarList.RSP_SUCCESS, result);
        verify(employeeRepo).deleteById(1);
    }

    @Test
    void testDeleteEmployee_NoDataFound() {
        // Arrange
        when(employeeRepo.existsById(1)).thenReturn(false);

        // Act
        String result = employeeService.deleteEmployee(1);

        // Assert
        assertEquals(VarList.RSP_NO_DATA_FOUND, result);
        verify(employeeRepo, never()).deleteById(anyInt());
    }
}