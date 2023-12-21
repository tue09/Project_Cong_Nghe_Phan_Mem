package com.example.project1.controllers.Manager;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.project1.Repository.ResidentRepository;
import com.example.project1.Repository.RoomRepository;
import com.example.project1.entity.Resident;

@Controller
public class ManagerResidentController {
    @Autowired
    private ResidentRepository residentRepo;

    @Autowired
    private RoomRepository roomRepository;
    public int roomNumber = 0;

    @GetMapping("/manager/room/{noRoom}")
    public String viewRoomDetails(@PathVariable String noRoom, Model model) {
        roomNumber = Integer.parseInt(noRoom);
        java.util.List<Resident> listResidentRoom = residentRepo.findByRoom(roomNumber);
        model.addAttribute("noRoom", noRoom);
        model.addAttribute("listResidentRoom", listResidentRoom);
        return "manager/room/residentRoom";
    }

    @GetMapping("/manager/room/{roomNumber}/addresident")
    public String viewRoomDetails(@PathVariable int roomNumber, Model model) {
        model.addAttribute("roomNumber", roomNumber);
        model.addAttribute("Resident", new Resident());
        return "manager/resident/create";
    }

    @PostMapping("/manager/room/{roomNumber}/addresident/save")
    public String save(@PathVariable int roomNumber, Resident resident) {
        Resident save = residentRepo.save(resident);
        save.setNoRoom(roomNumber);
        residentRepo.save(save);
        return "redirect:/manager/room/{roomNumber}";
    }
}