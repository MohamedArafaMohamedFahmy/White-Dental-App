package com.arafa.mohamed.whitedental.Model;

public class DoctorData {

    private String doctorName,idDoctor;

    public DoctorData(){

    }
    public DoctorData(String doctorName, String idDoctor){
        this.doctorName=doctorName;
        this.idDoctor=idDoctor;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(String idDoctor) {
        this.idDoctor = idDoctor;
    }
}
