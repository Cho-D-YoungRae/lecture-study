package study.mybatisstudy;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeMapper employeeMapper;

    @PostMapping
    public Employee post(@RequestBody Employee employee) {
        employeeMapper.insert(employee);
        return employee;
    }

    @GetMapping
    public List<Employee> getAll() {
        return employeeMapper.getAll();
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable("id") Long id) {
        return employeeMapper.getById(id);
    }
}
