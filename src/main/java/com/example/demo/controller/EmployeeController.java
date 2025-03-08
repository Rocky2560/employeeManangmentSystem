package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping("/home")
    public String index(Model model, @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "5") int size)
    {
        Page<Employee> listEmployee = employeeService.getData(page, size);
        model.addAttribute("listEmployee", listEmployee);
        model.addAttribute("posts", listEmployee.getContent());  // Posts for current page
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", listEmployee.getTotalPages());
        System.out.println(listEmployee.getSize());
//        model.addAttribute("employee", new Employee());
        return "index";
    }

    @PostMapping("/add")
    public String addEmployee( @RequestParam("name") String name,
                              @RequestParam("image") MultipartFile image1,
                              RedirectAttributes model) {
        try {
            // Upload Image and get file path
            String imagePath = employeeService.uploadImage(image1);
            System.out.println(imagePath);

            // Save Employee with Image Path
            employeeService.addEmployee(new Employee(name), imagePath);

            model.addFlashAttribute("message", "Employee added successfully!");
        } catch (IOException e) {
            model.addFlashAttribute("error", "Failed to upload image!");
            e.printStackTrace();
        }
//        model.addAttribute("listEmployee", employeeService.getData());
        return "redirect:/home"; // Redirect to index page
    }

    @PostMapping("/edit")
    public String editEmployee(@RequestParam("id") Long id, RedirectAttributes model,
                               @RequestParam("name") String name,
                               @RequestParam(value = "image", required = false) MultipartFile imageFile,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "5") int size) throws IOException {

        // Fetch existing employee from database
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Update name
        employee.setName(name);

        // Check if user uploaded a new image
        if (imageFile != null && !imageFile.isEmpty()) {
            // Save new image (assuming you have a service to handle file storage)
            String filePath = employeeService.storeFile(imageFile);
            employee.setImage(filePath);  // Update image in database
        }

        // Save updated employee
        employeeRepository.save(employee);
        model.addFlashAttribute("message", "Employee Updated successfully!");
        return "redirect:/home";  // Redirect to employee list page
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id, RedirectAttributes model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size)
    {
        employeeService.delete(id);
        model.addFlashAttribute("message", "Delete was successful");
//        model.addAttribute("listEmployee", employeeService.getData());
        return "redirect:/home";
    }


}
