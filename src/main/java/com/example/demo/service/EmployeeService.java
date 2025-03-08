package com.example.demo.service;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {

    final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @Autowired
    private EmployeeRepository employeeRepository;

    //Getting data for listing in table
    public Page<Employee> getData(int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findAll(pageable);
    }

//Edit section code
public String storeFile(MultipartFile file) throws IOException {
    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    Path path = Paths.get(UPLOAD_DIR + fileName);
    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    return "/uploads/" + fileName;  // Return relative path for storing in DB
}

    //Uploadin image file code
    public String uploadImage(MultipartFile imagefile) throws IOException {
        if (imagefile != null && !imagefile.isEmpty()) {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // Create directory if it doesn't exist
            }

            String fileName = System.currentTimeMillis() + "_" + imagefile.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, imagefile.getBytes());
            System.out.println(fileName);
            return fileName; // Return file name to store in DB
        }
        return null; // Return null if no file uploaded
    }

    //Entering user detail new one in Database
    public void addEmployee(Employee employee, String imagePath) {
        if (imagePath != null) {
            employee.setImage("/uploads/" + imagePath);
        }
        // Save to database
        employeeRepository.save(employee);
    }

    //Deleteing the emlployees
    public void delete(Long id)
    {
        employeeRepository.deleteById(id);
    }


}
