package lk.linex.emloyeems.controller;

import lk.linex.emloyeems.dto.EmployeeDTO;
import lk.linex.emloyeems.dto.ResponseDTO;
import lk.linex.emloyeems.service.EmployeeService;
import lk.linex.emloyeems.util.VarList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        employeeDTO = new EmployeeDTO(1, "John Doe", "123 Main St", "1234567890", "IT", "john.doe@example.com", 30, 50000.0);
    }

    // ===================== saveEmployee =====================

    @Test
    void testSaveEmployee_Success() {
        when(employeeService.saveEmployee(any(EmployeeDTO.class))).thenReturn(VarList.RSP_SUCCESS); // "00"

        ResponseEntity<ResponseDTO> response = employeeController.saveEmployee(employeeDTO);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(VarList.RSP_SUCCESS, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(employeeDTO, response.getBody().getContent());
    }

    @Test
    void testSaveEmployee_Duplicate() {
        when(employeeService.saveEmployee(any(EmployeeDTO.class))).thenReturn(VarList.RSP_DUPLICATED); // "06"

        ResponseEntity<ResponseDTO> response = employeeController.saveEmployee(employeeDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(VarList.RSP_DUPLICATED, response.getBody().getCode());
        assertEquals("Employee Registered", response.getBody().getMessage());
        assertEquals(employeeDTO, response.getBody().getContent());
    }

    @Test
    void testSaveEmployee_Fail() {
        when(employeeService.saveEmployee(any(EmployeeDTO.class))).thenReturn(VarList.RSP_FAIL); // else branch

        ResponseEntity<ResponseDTO> response = employeeController.saveEmployee(employeeDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(VarList.RSP_FAIL, response.getBody().getCode());
        assertEquals("Error", response.getBody().getMessage());
        assertNull(response.getBody().getContent());
    }

    @Test
    void testSaveEmployee_Exception() {
        when(employeeService.saveEmployee(any(EmployeeDTO.class)))
                .thenThrow(new RuntimeException("DB connection failed"));

        ResponseEntity<ResponseDTO> response = employeeController.saveEmployee(employeeDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(VarList.RSP_ERROR, response.getBody().getCode());
        assertEquals("DB connection failed", response.getBody().getMessage());
        assertNull(response.getBody().getContent());
    }

    // ===================== updateEmployee =====================

    @Test
    void testUpdateEmployee_Success() {
        when(employeeService.updateEmployee(any(EmployeeDTO.class))).thenReturn(VarList.RSP_SUCCESS); // "00"

        ResponseEntity<ResponseDTO> response = employeeController.updateEmployee(employeeDTO);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(VarList.RSP_SUCCESS, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(employeeDTO, response.getBody().getContent());
    }

    @Test
    void testUpdateEmployee_NotFound() {
        // Controller checks res.equals("01") → RSP_NO_DATA_FOUND → sets RSP_DUPLICATED (existing controller behavior)
        when(employeeService.updateEmployee(any(EmployeeDTO.class))).thenReturn(VarList.RSP_NO_DATA_FOUND); // "01"

        ResponseEntity<ResponseDTO> response = employeeController.updateEmployee(employeeDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(VarList.RSP_DUPLICATED, response.getBody().getCode()); // matches actual controller code
        assertEquals("NOT A Registered Employee", response.getBody().getMessage());
    }

    @Test
    void testUpdateEmployee_Fail() {
        when(employeeService.updateEmployee(any(EmployeeDTO.class))).thenReturn(VarList.RSP_FAIL); // else branch

        ResponseEntity<ResponseDTO> response = employeeController.updateEmployee(employeeDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(VarList.RSP_FAIL, response.getBody().getCode());
        assertEquals("Error", response.getBody().getMessage());
        assertNull(response.getBody().getContent());
    }

    @Test
    void testUpdateEmployee_Exception() {
        when(employeeService.updateEmployee(any(EmployeeDTO.class)))
                .thenThrow(new RuntimeException("Update failed"));

        ResponseEntity<ResponseDTO> response = employeeController.updateEmployee(employeeDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(VarList.RSP_ERROR, response.getBody().getCode());
        assertEquals("Update failed", response.getBody().getMessage());
        assertNull(response.getBody().getContent());
    }

    // ===================== getAllEmployees =====================

    @Test
    void testGetAllEmployees_Success() {
        List<EmployeeDTO> employeeList = List.of(employeeDTO);
        when(employeeService.getAllEmployee()).thenReturn(employeeList);

        ResponseEntity<ResponseDTO> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(VarList.RSP_SUCCESS, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(employeeList, response.getBody().getContent());
    }

    @Test
    void testGetAllEmployees_Exception() {
        when(employeeService.getAllEmployee())
                .thenThrow(new RuntimeException("Fetch failed"));

        ResponseEntity<ResponseDTO> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(VarList.RSP_ERROR, response.getBody().getCode());
        assertEquals("Fetch failed", response.getBody().getMessage());
        assertNull(response.getBody().getContent());
    }

    // ===================== searchEmployee =====================

    @Test
    void testSearchEmployee_Found() {
        when(employeeService.searchEmployee(1)).thenReturn(employeeDTO);

        ResponseEntity<ResponseDTO> response = employeeController.searchEmployee(1);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(VarList.RSP_SUCCESS, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(employeeDTO, response.getBody().getContent());
    }

    @Test
    void testSearchEmployee_NotFound() {
        when(employeeService.searchEmployee(99)).thenReturn(null);

        ResponseEntity<ResponseDTO> response = employeeController.searchEmployee(99);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(VarList.RSP_NO_DATA_FOUND, response.getBody().getCode());
        assertEquals("No Employee Available For this empId", response.getBody().getMessage());
        assertNull(response.getBody().getContent());
    }

    @Test
    void testSearchEmployee_Exception() {
        when(employeeService.searchEmployee(1))
                .thenThrow(new RuntimeException("Search failed"));

        ResponseEntity<ResponseDTO> response = employeeController.searchEmployee(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(VarList.RSP_ERROR, response.getBody().getCode());
        assertEquals("Search failed", response.getBody().getMessage());
        assertNull(response.getBody().getContent());
    }

    // ===================== deleteEmployee =====================

    @Test
    void testDeleteEmployee_Success() {
        when(employeeService.deleteEmployee(1)).thenReturn(VarList.RSP_SUCCESS); // "00"

        ResponseEntity<ResponseDTO> response = employeeController.deleteEmployee(1);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(VarList.RSP_SUCCESS, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertNull(response.getBody().getContent());
    }

    @Test
    void testDeleteEmployee_NotFound() {
        when(employeeService.deleteEmployee(99)).thenReturn(VarList.RSP_NO_DATA_FOUND); // else branch

        ResponseEntity<ResponseDTO> response = employeeController.deleteEmployee(99);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(VarList.RSP_NO_DATA_FOUND, response.getBody().getCode());
        assertEquals("No Employee Available For this empId", response.getBody().getMessage());
        assertNull(response.getBody().getContent());
    }

    @Test
    void testDeleteEmployee_Exception() {
        when(employeeService.deleteEmployee(1))
                .thenThrow(new RuntimeException("Delete failed"));

        ResponseEntity<ResponseDTO> response = employeeController.deleteEmployee(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(VarList.RSP_ERROR, response.getBody().getCode());
        assertEquals("Delete failed", response.getBody().getMessage());
        assertNull(response.getBody().getContent());
    }
}