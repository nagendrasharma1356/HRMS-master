package com.example.Client.Service.dto;

import com.example.Client.Service.entity.Client;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {

    private Long id;
    private String Company;
    private String OfficialWebsite;
    private String TaxName;
    private String GST;
    private String PhoneNumber;
    private String City;
    private String State;
    private String Postal;
    private String AddedBy;
    private String CompanyAddress;
    private String ShippingAddress;
    private List<Long> clientIds;

    private List<ClientDto> clients;



    public void setClients(List<ClientDto> clients) {
        this.clients = clients;
    }

    public List<Long> getClientIds() {
        return clientIds;
    }

    public void setClientIds(List<Long> clientIds) {
        this.clientIds = clientIds;
    }
    public Long getId() {
        return id;

    }


    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", Company='" + Company + '\'' +
                ", OfficialWebsite='" + OfficialWebsite + '\'' +
                ", TaxName='" + TaxName + '\'' +
                ", GST='" + GST + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", City='" + City + '\'' +
                ", State='" + State + '\'' +
                ", Postal='" + Postal + '\'' +
                ", AddedBy='" + AddedBy + '\'' +
                ", CompanyAddress='" + CompanyAddress + '\'' +
                ", ShippingAddress='" + ShippingAddress + '\'' +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return Company;
    }



    public void setCompany(String company) {
        Company = company;
    }

    public String getOfficialWebsite() {
        return OfficialWebsite;
    }

    public void setOfficialWebsite(String officialWebsite) {
        OfficialWebsite = officialWebsite;
    }

    public String getTaxName() {
        return TaxName;
    }

    public void setTaxName(String taxName) {
        TaxName = taxName;
    }

    public String getGST() {
        return GST;
    }

    public void setGST(String GST) {
        this.GST = GST;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPostal() {
        return Postal;
    }

    public void setPostal(String postal) {
        Postal = postal;
    }

    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    public String getCompanyAddress() {
        return CompanyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        CompanyAddress = companyAddress;
    }

    public String getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        ShippingAddress = shippingAddress;
    }


}
