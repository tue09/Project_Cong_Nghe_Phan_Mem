package com.example.project1.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int noRoom;
    
    private String idOwner;
    private String nameOwner;
    private String numberPhoneOwner;
    @Column(name = "`key`")
    private String key;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    // MapopedBy trỏ tới tên biến Address ở trong Person.
    private List<Resident> residents;

    public Room() {
        this.residents = new ArrayList<Resident>();
    }

    // Constructor với tham số
    public Room(int noRoom, String idOwner, String nameOwner, String numberPhoneOwner) {
        this.noRoom = noRoom;
        this.idOwner = idOwner;
        this.nameOwner = nameOwner;
        this.numberPhoneOwner = numberPhoneOwner;
        this.residents = new ArrayList<Resident>();
    }

    public void addResident(Resident resident) {
        residents.add(resident);
    }

    public void eraseResident(int index) {
        residents.remove(index);
    }

    public void generateKey() {
        String suffix = idOwner.substring(idOwner.length() - 4);
        this.key = suffix + String.valueOf(id);
    }

    public int getId() {
        return id;
    }

    public int getNoRoom() {
        return noRoom;
    }

    public void setNoRoom(int noRoom) {
        this.noRoom = noRoom;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public String getNameOwner() {
        return nameOwner;
    }

    public void setNameOwner(String nameOwner) {
        this.nameOwner = nameOwner;
    }

    public String getNumberPhoneOwner() {
        return numberPhoneOwner;
    }

    public void setNumberPhoneOwner(String numberPhoneOwner) {
        this.numberPhoneOwner = numberPhoneOwner;
    }


    public String getKey() {
        return key;
    }


}