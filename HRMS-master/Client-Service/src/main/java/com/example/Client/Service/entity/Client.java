package com.example.Client.Service.entity;


import jakarta.persistence.*;

@jakarta.persistence.Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String Salutation;
    private String ClientName;
    private String email;
    private String Country;
    private String mobileNo;
    private String profilePicture;
    private String createdAt;
    private String updatedAt;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;





    public Client(){

    }


    public Client(Long id, String salutation, String clientName, String email, String country, String mobileNo, String profilePicture, String createdAt, String updatedAt) {
        Id = id;
        Salutation = salutation;
        ClientName = clientName;
        this.email = email;
        Country = country;
        mobileNo = mobileNo;
        this.profilePicture = profilePicture;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getSalutation() {
        return Salutation;
    }

    public void setSalutation(String salutation) {
        Salutation = salutation;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        mobileNo = mobileNo;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }




}

