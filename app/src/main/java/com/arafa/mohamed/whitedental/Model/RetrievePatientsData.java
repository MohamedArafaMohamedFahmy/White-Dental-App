package com.arafa.mohamed.whitedental.Model;

import java.io.Serializable;

public class RetrievePatientsData implements Serializable {

    private String idPatient,patientName,age,phoneNumber,chronicDiseases,notes,address,job,
            visitDate,amount,visitNumber,selectNameDoctor,operation;

    public RetrievePatientsData(){

    }

    public RetrievePatientsData(String idPatient,String patientName, String age, String phoneNumber,
                                String address, String job, String selectNameDoctor){
        this.idPatient=idPatient;
        this.patientName=patientName;
        this.age=age;
        this.phoneNumber=phoneNumber;
        this.address=address;
        this.job=job;
        this.selectNameDoctor=selectNameDoctor;

    }

    public RetrievePatientsData(String idPatient,String patientName, String age, String phoneNumber,String chronicDiseases,String notes,
                                String address, String job, String selectNameDoctor){
        this.idPatient=idPatient;
        this.patientName=patientName;
        this.age=age;
        this.phoneNumber=phoneNumber;
        this.chronicDiseases=chronicDiseases;
        this.notes=notes;
        this.address=address;
        this.job=job;
        this.selectNameDoctor=selectNameDoctor;
    }

    public RetrievePatientsData(String visitNumber,String visitDate,String amount, String operation){
        this.visitNumber=visitNumber;
        this.visitDate=visitDate;
        this.amount=amount;
        this.operation=operation;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }


    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getChronicDiseases() {
        return chronicDiseases;
    }

    public void setChronicDiseases(String chronicDiseases) {
        this.chronicDiseases = chronicDiseases;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSelectNameDoctor() {
        return selectNameDoctor;
    }

    public void setSelectNameDoctor(String selectNameDoctor) {
        this.selectNameDoctor = selectNameDoctor;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
