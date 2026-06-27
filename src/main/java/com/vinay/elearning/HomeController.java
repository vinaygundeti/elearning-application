package com.vinay.elearning;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public HomeController(StudentRepository studentRepository,
                          CourseRepository courseRepository,
                          EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    // Home Page
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // Student Login Page
    @GetMapping("/student-login")
    public String studentLogin() {
        return "student-login";
    }

    // Student Registration Page
    @GetMapping("/student-register")
    public String studentRegister() {
        return "student-registration";
    }

    // Student Register
    @PostMapping("/register")
    public String registerStudent(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String mobile,
            @RequestParam String password,
            @RequestParam String department) {

        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setMobile(mobile);
        student.setPassword(password);
        student.setDepartment(department);

        studentRepository.save(student);

        return "index";
    }

    // Student Login
    @PostMapping("/student-login")
    public String loginStudent(@RequestParam String email,
                               @RequestParam String password) {

        Student student = studentRepository.findByEmailAndPassword(email, password);

        if (student != null) {
            return "dashboard";
        }

        return "student-login";
    }

    // Students List
    @GetMapping("/students")
    public String viewStudents(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "students";
    }

    // Delete Student
    @GetMapping("/delete-student")
    public String deleteStudent(@RequestParam Long id) {
        studentRepository.deleteById(id);
        return "redirect:/students";
    }

    // Edit Student
    @GetMapping("/edit-student")
    public String editStudent(@RequestParam Long id, Model model) {
        Student student = studentRepository.findById(id).orElse(null);
        model.addAttribute("student", student);
        return "edit-student";
    }

    // Update Student
    @PostMapping("/update-student")
    public String updateStudent(@ModelAttribute Student student) {
        studentRepository.save(student);
        return "redirect:/students";
    }

    // Admin Login Page
    @GetMapping("/admin-login")
    public String adminLoginPage() {
        return "admin-login";
    }

    // Admin Login
    @PostMapping("/admin-login")
    public String adminLogin(@RequestParam String username,
                             @RequestParam String password,
                             Model model) {

        if (username.equals("admin") && password.equals("admin123")) {

            model.addAttribute("totalStudents", studentRepository.count());
            model.addAttribute("totalCourses", courseRepository.count());
            model.addAttribute("totalEnrollments", enrollmentRepository.count());

            return "admin-dashboard";
        }

        return "admin-login";
    }

    // Add Course Page
    @GetMapping("/add-course")
    public String addCoursePage() {
        return "add-course";
    }

    // Save Course
    @PostMapping("/save-course")
    public String saveCourse(@RequestParam String courseName,
                             @RequestParam String trainerName,
                             @RequestParam String duration,
                             @RequestParam String videoUrl) {

        Course course = new Course();
        course.setCourseName(courseName);
        course.setTrainerName(trainerName);
        course.setDuration(duration);
        course.setVideoUrl(videoUrl);

        courseRepository.save(course);

        return "redirect:/courses";
    }

    // View Courses
    @GetMapping("/courses")
    public String viewCourses(Model model) {

        List<Course> courses = courseRepository.findAll();

        model.addAttribute("courses", courses);

        return "courses";
    }

    // Delete Course
    @GetMapping("/delete-course")
    public String deleteCourse(@RequestParam Long id) {

        courseRepository.deleteById(id);

        return "redirect:/courses";
    }

    // Edit Course
    @GetMapping("/edit-course")
    public String editCourse(@RequestParam Long id, Model model) {

        Course course = courseRepository.findById(id).orElse(null);

        model.addAttribute("course", course);

        return "edit-courses";
    }

    // Update Course
    @PostMapping("/update-course")
    public String updateCourse(@ModelAttribute Course course) {

        courseRepository.save(course);

        return "redirect:/courses";
    }

    // Enroll Page
    @GetMapping("/enroll")
    public String enrollPage(Model model) {

        model.addAttribute("courses", courseRepository.findAll());

        return "enroll-course";
    }

    // Save Enrollment
    @PostMapping("/save-enrollment")
    public String saveEnrollment(@RequestParam String studentEmail,
                                 @RequestParam String courseName) {

        Student student = studentRepository.findByEmail(studentEmail);

        Enrollment enrollment = new Enrollment();

        enrollment.setStudentEmail(studentEmail);
        enrollment.setStudentName(student.getName());
        enrollment.setCourseName(courseName);

        enrollment.setStatus("In Progress");
        enrollment.setCertificateGenerated(false);

        enrollmentRepository.save(enrollment);

        return "dashboard";
    }

    // View Enrollments
    @GetMapping("/enrollments")
    public String viewEnrollments(Model model) {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        model.addAttribute("enrollments", enrollmentRepository.findAll());

        return "enrollments";
    }
    @GetMapping("/my-courses")
    public String myCourses(Model model) {

        List<Enrollment> enrollments = enrollmentRepository.findAll();

        model.addAttribute("enrollments", enrollments);

        return "my-courses";
    }

    // Delete Enrollment
    @GetMapping("/delete-enrollment")
    public String deleteEnrollment(@RequestParam Long id) {

        enrollmentRepository.deleteById(id);

        return "redirect:/enrollments";
    }
    @GetMapping("/complete-course")
    public String completeCourse(@RequestParam Long id) {

        Enrollment enrollment = enrollmentRepository.findById(id).orElse(null);

        if (enrollment != null) {
            enrollment.setStatus("Completed");
            enrollment.setCertificateGenerated(true);
            enrollmentRepository.save(enrollment);
        }

        return "redirect:/my-courses";
    }
    @GetMapping("/certificate")
    public String certificate(
            @RequestParam Long id,
            Model model) {

        Enrollment enrollment = enrollmentRepository.findById(id).orElse(null);

        if (enrollment != null) {
            model.addAttribute("studentName", enrollment.getStudentName());
            model.addAttribute("courseName", enrollment.getCourseName());
        }

        return "certificate";
    }

}