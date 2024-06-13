package com.example.ASM.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "staffs")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer staffID;

    @NotEmpty(message = "Fullname is not valid")
    private String fullName;

    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password is not valid")
    private String password;

    @NotNull(message = "Gender is not valid")
    private String gender;

    private String imageName;

    @NotEmpty(message = "Phone is not valid")
    @Size(min = 0, max = 13, message = "Phone is not valid")
    private String phone;

    private boolean enabled;

    @NotEmpty(message = "Address is not valid")
    private String address;

    private Date createDate;

    public Staff(String fullName, String email, String password, String gender, String imageName, String phone, boolean enabled, String address, Date createDate) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.imageName = imageName;
        this.phone = phone;
        this.enabled = enabled;
        this.address = address;
        this.createDate = createDate;
    }

    @OneToMany(mappedBy = "staff")
    private Set<Product> products;

    @Transient
    public String getPhotosImagePath() {
        if (staffID == null || imageName == null) return "/images/default-user.png";
        return "/staff-photos/" + this.staffID + "/" + this.imageName;
    }
}
