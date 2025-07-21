package jwt.demo.controller;

import jwt.demo.entity.Student;
import jwt.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/students")
public class StudentController 
{

    @Autowired
    private StudentRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/post")
    public Student createStudent(@RequestBody Student student) {
    	student.setPassword(passwordEncoder.encode(student.getPassword()));
        return repository.save(student);
    }

    @GetMapping("/get")
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    @GetMapping("/get/{id}")
    public Student getStudent(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    
    @PutMapping("/update/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student newStudent) {
        Student student = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "student not in the repo"));
        if(newStudent.getName()!=null) {
        	student.setName(newStudent.getName());
        }
        if(newStudent.getAge()!=null) {
        	student.setAge(newStudent.getAge());
        }
        if(newStudent.getEmail()!=null) {
        	student.setEmail(newStudent.getEmail());
        }

        return repository.save(student); 
    }


    @DeleteMapping("delete/{id}")
    public boolean deleteStudent(@PathVariable Long id) {
    	 Student student=repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"student not in the repo"));
  
    		 repository.deleteById(id);
    		 return true;
    	 }
    	
  }

