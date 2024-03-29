package com.example.project1.controllers.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.project1.Repository.AddResidentRequestRepository;
import com.example.project1.Repository.RequestRepository;
import com.example.project1.Repository.ResidentRepository;
import com.example.project1.Repository.RoomRepository;
import com.example.project1.Repository.TypeDonationRepository;
import com.example.project1.entity.AddResidentRequest;
import com.example.project1.entity.Request;
import com.example.project1.entity.Resident;
import com.example.project1.entity.Room;
import com.example.project1.entity.TypeDonation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserRequestController {
    @Autowired
    RequestRepository RequestRepo;

    @Autowired
    ResidentRepository ResidentRepo;

    @Autowired
    RoomRepository RoomRepo;

    @Autowired
    TypeDonationRepository TypeDonationRepo;

    @Autowired
    AddResidentRequestRepository AddResidentRequestRepo;

    @GetMapping("/user/request/{key}/add_resident")
    public String requestAddResident(@PathVariable String key ,Model model) {
        Room aa = RoomRepo.findByKey(key).get(0);
        AddResidentRequest resident = new AddResidentRequest();
        resident.setRoom(aa.getNoRoom());
        model.addAttribute("key", key);
        model.addAttribute("resident", resident);
        return "user/Request/add_resident";
    }

    @PostMapping("/user/request/{key}/save_resident")
    public String requestSaveResident(@PathVariable String key, AddResidentRequest request) {
        Room aa = RoomRepo.findByKey(key).get(0);
        request.setRoom(aa.getNoRoom());
        AddResidentRequestRepo.save(request);
        return "redirect:/user/index/{key}";
    }

    @GetMapping("/user/request/{key}/room_info")
    public String changeRoomInfo(@PathVariable String key, Model model) {
        Room aa = RoomRepo.findByKey(key).get(0);
        Request request = new Request();
        request.setNoRoom(aa.getNoRoom());
        model.addAttribute("request", request);
        return "user/Request/room_info";
    }

    @PostMapping("/user/request/{key}/room_info/save")
    public String saveRoomInfo(@PathVariable String key, Request request) {
        Room a = RoomRepo.findByKey(key).get(0);
        request.setNoRoom(a.getNoRoom());
        RequestRepo.save(request);
        return "redirect:/user/index/{key}";
    }

    @GetMapping("/user/request/{key}/resident_info")
    public String changeResidentInfo(@PathVariable String key, Model model) {
        Request request = new Request();
        Room a = RoomRepo.findByKey(key).get(0);
        List<Resident> residents = a.getResidents();
        model.addAttribute("residents", residents);
        model.addAttribute("request", request);
        return "user/Request/resident_info";
    }

    @PostMapping("/user/request/{key}/resident_info/save")
    public String saveResidentInfo(@PathVariable String key, Request request) {
        Room a = RoomRepo.findByKey(key).get(0);
        request.setNoRoom(a.getNoRoom());
        request.setObjectName(ResidentRepo.findByIdResident(request.getObjectId()).get(0).getName());
        RequestRepo.save(request);
        return "redirect:/user/index/{key}";
    }

    @GetMapping("/user/request/{key}/update_fee")
    public String updateFee(@PathVariable String key, Model model) {
        List<TypeDonation> list = TypeDonationRepo.findAll();
        model.addAttribute("list", list);
        Request request = new Request();
        model.addAttribute("request", request);
        return "user/Request/update_fee";
    }

    @PostMapping("/user/request/{key}/update_fee/save")
    public String saveUpdateFee(@PathVariable String key, Request request) {
        Room a = RoomRepo.findByKey(key).get(0);
        request.setNoRoom(a.getNoRoom());
        if (request.getIdRequest() > 18) {
            List<TypeDonation> types = TypeDonationRepo.findByNo(request.getIdRequest() - 20);
            if (!types.isEmpty()) {
                request.setDonationName(types.get(0).getType());
            }
        }
        RequestRepo.save(request);
        return "redirect:/user/index/{key}";
    }    
    
}
