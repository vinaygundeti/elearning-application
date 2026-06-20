package com.vinay.elearning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(long id) {
        return studentRepository.findById(id).orElseThrow(() ->
                new StudentNotFoundException("Student not found with id : " + id));
    }

    public Student updateStudent(long id, Student student) {

        Student existingStudent =
                studentRepository.findById(id).orElse(null);

        if (existingStudent != null) {
            existingStudent.setName(student.getName());
            existingStudent.setDepartment(student.getDepartment());

            return studentRepository.save(existingStudent);
        }

        return null;
    }

    public String deleteStudent(long id) {
        studentRepository.deleteById(id);
        return "Student Deleted Successfully";
    }
}