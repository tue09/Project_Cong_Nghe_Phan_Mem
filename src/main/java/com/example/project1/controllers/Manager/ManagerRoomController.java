package com.example.project1.controllers.Manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.project1.Repository.AddResidentRequestRepository;
import com.example.project1.Repository.DonationFeeRepository;
import com.example.project1.Repository.MandatoryFeeHistoryRepository;
import com.example.project1.Repository.MandatoryFeeRepository;
import com.example.project1.Repository.RequestRepository;
import com.example.project1.Repository.ResidentRepository;
import com.example.project1.Repository.RoomHistoryRepository;
import com.example.project1.Repository.RoomRepository;
import com.example.project1.entity.AddResidentRequest;
import com.example.project1.entity.Request;
import com.example.project1.entity.DonationFee;
import com.example.project1.entity.DonationFeeHistory;
import com.example.project1.entity.MandatoryFee;
import com.example.project1.entity.MandatoryFeeHistory;
import com.example.project1.entity.Resident;
import com.example.project1.entity.ResidentHistory;
import com.example.project1.entity.Room;
import com.example.project1.entity.RoomHistory;
import com.example.project1.entity.ChartData;
import com.example.project1.service.serviceHistoryRoom;
import com.example.project1.service.serviceRoom;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;



@Controller
public class ManagerRoomController {
    @Autowired
    private RoomRepository RoomRepo;
    @Autowired
    private RoomHistoryRepository RoomHistoryRepo;

    @Autowired
    private ResidentRepository ResidentRepo;

    @Autowired
    private RequestRepository RequestRepo;

    @Autowired
    private MandatoryFeeRepository MandatoryFeeRepo;

    @Autowired
    private MandatoryFeeHistoryRepository MandatoryFeeHistoryRepo;

    @Autowired
    private DonationFeeRepository DonationFeeRepo;

    @Autowired
    private AddResidentRequestRepository AddResidentRequestRepo;

    @Autowired
    private serviceRoom service;
    @Autowired
	private serviceHistoryRoom serviceHR;

    @Autowired
    private ManagerFeeController feeController;
    @Autowired
    private ManagerResidentController residentController;
    
    @GetMapping("/manager/index")
    //tìm kiếm theo keyword là 1 string
    public String index(Model model, @RequestParam(name = "keyword", required = false) String keyword, HttpServletRequest request,
    		@RequestParam(name = "pageNo", defaultValue ="1") Integer pageNo) {
        boolean flag1 = false;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    model.addAttribute("username", cookie.getValue());
                    flag1 = true;
                }
            }
        }
        if (!flag1) return "404";
        java.util.List<Request> listRequest1 = RequestRepo.findAll();
		java.util.List<AddResidentRequest> listRequest2 = AddResidentRequestRepo.findAll();
		int num1 = 0;
		for (Request req : listRequest1) {
			if (req.getApproved() == 1) {num1 ++;}
		}
		int num2 = 0;
		for (AddResidentRequest Addreq : listRequest2) {
			if (Addreq.getApproved() == 1) {num2 ++;}
		}
		int numNoti = num1 + num2;
		model.addAttribute("numNoti", numNoti);
        java.util.List<Room> listRoom = RoomRepo.findAll();
        java.util.List<Resident> listResidents = ResidentRepo.findAll();
        // Tạo dữ liệu mẫu cho biểu đồ cột
        model.addAttribute("numRoom", listRoom.size());
        model.addAttribute("numResident", listResidents.size());
        ChartData barData = new ChartData();
        int[] bardata = new int[6];
        java.util.List<MandatoryFee> listMandatoryFees = MandatoryFeeRepo.findAll();
        java.util.List<MandatoryFeeHistory> listMandatoryFeeHistories = MandatoryFeeHistoryRepo.findAll();
        for (int i = 0; i < 7; ++i){
            for (MandatoryFee mdF : listMandatoryFees) {
                int month = mdF.getMonth();
                if (month == 7){
                    bardata[0] += mdF.getRoomFeePaid();
                } else if (month == 8){
                    bardata[1] += mdF.getRoomFeePaid();
                } else if (month == 9){
                    bardata[2] += mdF.getRoomFeePaid();
                } else if (month == 10){
                    bardata[3] += mdF.getRoomFeePaid();
                } else if (month == 11){
                    bardata[4] += mdF.getRoomFeePaid();
                } else if (month == 12){
                    bardata[5] += mdF.getRoomFeePaid();
                }
		    }

            for (MandatoryFeeHistory mdF : listMandatoryFeeHistories) {
                int month = mdF.getMonth();
                if (month == 7){
                    bardata[0] += mdF.getRoomFeePaid();
                } else if (month == 8){
                    bardata[1] += mdF.getRoomFeePaid();
                } else if (month == 9){
                    bardata[2] += mdF.getRoomFeePaid();
                } else if (month == 10){
                    bardata[3] += mdF.getRoomFeePaid();
                } else if (month == 11){
                    bardata[4] += mdF.getRoomFeePaid();
                } else if (month == 12){
                    bardata[5] += mdF.getRoomFeePaid();
                }
		    }
        }
        barData.setLabels(new String[]{"Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"});
        barData.setData(bardata);
        barData.setTitle("Tổng hợp tiền nhà 6 tháng cuối năm 2023");

        // Tạo dữ liệu mẫu cho biểu đồ đường
        ChartData lineData = new ChartData();
        int[] linedata = new int[6];
        for (int i = 0; i < 7; ++i){
            for (MandatoryFee mdF : listMandatoryFees) {
                int month = mdF.getMonth();
                if (month == 7){
                    linedata[0] += mdF.getElectricFee() + mdF.getWaterFee();
                } else if (month == 8){
                    linedata[1] += mdF.getElectricFee() + mdF.getWaterFee();
                } else if (month == 9){
                    linedata[2] += mdF.getElectricFee() + mdF.getWaterFee();
                } else if (month == 10){
                    linedata[3] += mdF.getElectricFee() + mdF.getWaterFee();
                } else if (month == 11){
                    linedata[4] += mdF.getElectricFee() + mdF.getWaterFee();
                } else if (month == 12){
                    linedata[5] += mdF.getElectricFee() + mdF.getWaterFee();
                }
		    }

            for (MandatoryFeeHistory mdF : listMandatoryFeeHistories) {
                int month = mdF.getMonth();
                if (month == 7){
                    linedata[0] += mdF.getElectricFee() + mdF.getWaterFee();
                } else if (month == 8){
                    linedata[1] += mdF.getElectricFee() + mdF.getWaterFee();
                } else if (month == 9){
                    linedata[2] += mdF.getElectricFee() + mdF.getWaterFee();
                } else if (month == 10){
                    linedata[3] += mdF.getElectricFee() + mdF.getWaterFee();
                } else if (month == 11){
                    linedata[4] += mdF.getElectricFee() + mdF.getWaterFee();
                } else if (month == 12){
                    linedata[5] += mdF.getElectricFee() + mdF.getWaterFee();
                }
		    }
        }
        lineData.setLabels(new String[]{"Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"});
        lineData.setData(linedata);
        lineData.setTitle("Tiền điện và tiền nước 6 tháng cuối năm 2023");

        // Tạo dữ liệu mẫu cho biểu đồ tròn
        int numapp = 0;
        int numrej = 0;
        int numprc = 0;
		for (Request req : listRequest1) {
            int res = req.getApproved();
			if (res == 1) {numprc++;}
            else if (res == 2){numrej++;}
            else if (res == 0){numapp++;}
		}
		for (AddResidentRequest Addreq : listRequest2) {
            int res = Addreq.getApproved();
            if (res == 1) {numprc++;}
            else if (res == 2){numrej++;}
            else if (res == 0){numapp++;}
		}
        ChartData pieData = new ChartData();
        pieData.setLabels(new String[]{"Chấp nhận", "Từ chối", "Chưa phản hồi"});
        pieData.setData(new int[]{numapp, numrej, numprc});
        pieData.setTitle("Yêu cầu");

        // Truyền dữ liệu vào model
        model.addAttribute("barData", barData);
        model.addAttribute("lineData", lineData);
        model.addAttribute("pieData", pieData);

        java.util.List<Integer> listnoRoom = new ArrayList<>();
        java.util.List<Integer> listDonation = new ArrayList<>();
        java.util.List<DonationFee> listDonationFees = DonationFeeRepo.findAll();
        for (Room room : listRoom){
            listnoRoom.add(room.getNoRoom());
            int count = 0;
            for (DonationFee fee : listDonationFees){
                if (fee.getNoRoom() == room.getNoRoom()){
                    count += fee.getAmount();
                }
            }
            listDonation.add(count);
        }
        model.addAttribute("listDonation", listDonation);
        model.addAttribute("listnoRoom", listnoRoom);
        


        return "manager/index";
    }

    // public String index1(Model model) {
    //     java.util.List<Room> listRoom = RoomRepo.findAll();
    //     model.addAttribute("listRoom", listRoom);
    //     return "manager/index";
    // }
    
    @GetMapping("/manager/createRoom")
    public String create(Model model, HttpServletRequest request) {
        boolean flag1 = false;
		Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
					flag1 = true;
                }
            }
        }
		if (!flag1) return "404";
        java.util.List<Request> listRequest1 = RequestRepo.findAll();
		java.util.List<AddResidentRequest> listRequest2 = AddResidentRequestRepo.findAll();
		int num1 = 0;
		for (Request req : listRequest1) {
			if (req.getApproved() == 1) {num1 ++;}
		}
		int num2 = 0;
		for (AddResidentRequest Addreq : listRequest2) {
			if (Addreq.getApproved() == 1) {num2 ++;}
		}
		int numNoti = num1 + num2;
		model.addAttribute("numNoti", numNoti);
        model.addAttribute("room", new Room());
        return "manager/room/create";
    }

    @GetMapping("/manager/room/index")
    public String roomindex(Model model, @RequestParam(name = "keyword", required = false) String keyword, HttpServletRequest request,
    		@RequestParam(name = "pageNo", defaultValue ="1") Integer pageNo) {
        boolean flag1 = false;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    flag1 = true;
                }
            }
        }
        if (!flag1) return "404";
        Page <Room> listRoom = this.service.listAll(keyword,pageNo);
        java.util.List<Request> listRequest1 = RequestRepo.findAll();
		java.util.List<AddResidentRequest> listRequest2 = AddResidentRequestRepo.findAll();
		int num1 = 0;
		for (Request req : listRequest1) {
			if (req.getApproved() == 1) {num1 ++;}
		}
		int num2 = 0;
		for (AddResidentRequest Addreq : listRequest2) {
			if (Addreq.getApproved() == 1) {num2 ++;}
		}
		int numNoti = num1 + num2;
		model.addAttribute("numNoti", numNoti);
        model.addAttribute("totalPage",listRoom.getTotalPages());
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("listRoom", listRoom);
        return "manager/room/index";
    }


    @GetMapping("/manager/room/edit/{key}")
    public String edit(@PathVariable String key, Model model, HttpServletRequest request) {
        boolean flag1 = false;
		Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
					flag1 = true;
                }
            }
        }
		if (!flag1) return "404";
        java.util.List<Request> listRequest1 = RequestRepo.findAll();
		java.util.List<AddResidentRequest> listRequest2 = AddResidentRequestRepo.findAll();
		int num1 = 0;
		for (Request req : listRequest1) {
			if (req.getApproved() == 1) {num1 ++;}
		}
		int num2 = 0;
		for (AddResidentRequest Addreq : listRequest2) {
			if (Addreq.getApproved() == 1) {num2 ++;}
		}
		int numNoti = num1 + num2;
		model.addAttribute("numNoti", numNoti);
        Room room = RoomRepo.findByKey(key).get(0);
        model.addAttribute("room", room);
        model.addAttribute("key", key);
        return "manager/room/edit";
    }

    @PostMapping("/manager/room/update/{key}")
    public String update(@PathVariable String key, @ModelAttribute Room room, Model model, HttpServletRequest request) {
        boolean flag1 = false;
		Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
					flag1 = true;
                }
            }
        }
		if (!flag1) return "404";
        Room a = RoomRepo.findByKey(key).get(0);
        a.setNoRoom(room.getNoRoom());
        a.setNumberPhoneOwner(room.getNumberPhoneOwner());
        a.setNameOwner(room.getNameOwner());
        a.setIdOwner(room.getIdOwner());
        a.setDefaultParkingFee(room.getDefaultParkingFee());
        a.setDefaultFeeRoom(room.getDefaultFeeRoom());
        int noRoom = a.getNoRoom();
        RoomRepo.save(a);
        RoomHistory aa = RoomHistoryRepo.findByKey(a.getKey()).get(0);
        aa.setNoRoom(room.getNoRoom());
        aa.setNumberPhoneOwner(room.getNumberPhoneOwner());
        aa.setNameOwner(room.getNameOwner());
        aa.setIdOwner(room.getIdOwner());
        aa.setDefaultParkingFee(room.getDefaultParkingFee());
        aa.setDefaultFeeRoom(room.getDefaultFeeRoom());
        RoomHistoryRepo.save(aa);

        java.util.List<Request> listRequest1 = RequestRepo.findAll();
		java.util.List<AddResidentRequest> listRequest2 = AddResidentRequestRepo.findAll();
		int num1 = 0;
		for (Request req : listRequest1) {
			if (req.getApproved() == 1) {num1 ++;}
		}
		int num2 = 0;
		for (AddResidentRequest Addreq : listRequest2) {
			if (Addreq.getApproved() == 1) {num2 ++;}
		}
		int numNoti = num1 + num2;
		model.addAttribute("numNoti", numNoti);
        model.addAttribute("noRoom", noRoom);
        model.addAttribute("listResidentRoom", a.getResidents());
        model.addAttribute("listRoom", RoomRepo.findByKey(key));
        return "manager/room/residentRoom";
    }

    @PostMapping("/manager/room/save")
    public String save(@ModelAttribute Room room) {
        Room new_room = RoomRepo.save(room);
        new_room.generateKey();
        RoomRepo.save(new_room);

        saveRoomInHistory(new_room);

        return "redirect:/manager/index";
    }

    @GetMapping("/manager/room/delete/{noRoom}")
    public String delete(@PathVariable String noRoom, Model model) {
        int roomNumber = Integer.parseInt(noRoom);
        Room a = RoomRepo.findByRoom(roomNumber).get(0);
        closeRoom(a);
        return "redirect:/manager/room/index";
    }
    // Room History

    @GetMapping("/manager/history/room/index")
    public String his_index(Model model, HttpServletRequest request,
	                   @RequestParam(required = false) String keyword,
	                   @RequestParam(required = false) String startDate,
	                   @RequestParam(required = false) String endDate,
	                   @RequestParam(defaultValue ="1") Integer pageNo) {
        boolean flag1 = false;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    flag1 = true;
                }
            }
        }
        if (!flag1) return "404";
	    Page<RoomHistory> listRoomHistory = this.serviceHR.listAll(keyword, startDate, endDate, pageNo);
	    model.addAttribute("keyword",keyword);
	    model.addAttribute("startDate",startDate);
	    model.addAttribute("endDate",endDate);
	    model.addAttribute("totalPage", listRoomHistory.getTotalPages());
	    model.addAttribute("currentPage", pageNo);
	    model.addAttribute("listRoomHistory", listRoomHistory);
        java.util.List<Request> listRequest1 = RequestRepo.findAll();
		java.util.List<AddResidentRequest> listRequest2 = AddResidentRequestRepo.findAll();
		int num1 = 0;
		for (Request req : listRequest1) {
			if (req.getApproved() == 1) {num1 ++;}
		}
		int num2 = 0;
		for (AddResidentRequest Addreq : listRequest2) {
			if (Addreq.getApproved() == 1) {num2 ++;}
		}
		int numNoti = num1 + num2;
		model.addAttribute("numNoti", numNoti);
	    return "manager/room/his_index";
	}

    @GetMapping("/manager/history/room/detail/{key}")
    public String his_detail(@PathVariable String key ,Model model, HttpServletRequest request) {
        boolean flag1 = false;
		Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
					flag1 = true;
                }
            }
        }
		if (!flag1) return "404";
        RoomHistory room = RoomHistoryRepo.findByKey(key).get(0);
        List<ResidentHistory> residents = room.getResidents();
        model.addAttribute("room", room);
        model.addAttribute("residents", residents); 
        java.util.List<Request> listRequest1 = RequestRepo.findAll();
		java.util.List<AddResidentRequest> listRequest2 = AddResidentRequestRepo.findAll();
		int num1 = 0;
		for (Request req : listRequest1) {
			if (req.getApproved() == 1) {num1 ++;}
		}
		int num2 = 0;
		for (AddResidentRequest Addreq : listRequest2) {
			if (Addreq.getApproved() == 1) {num2 ++;}
		}
		int numNoti = num1 + num2;
		model.addAttribute("numNoti", numNoti);
        return "manager/room/his_index";
    }

    public void saveRoomInHistory(Room room) {
        RoomHistory roomHis = new RoomHistory(room.getKey(), room.getNoRoom(), room.getIdOwner(), room.getNameOwner(), room.getNumberPhoneOwner(), room.getDefaultFeeRoom(), room.getDefaultParkingFee(), getTime());
        RoomHistoryRepo.save(roomHis);
    }

    public void closeRoom(Room room) {
        List<Resident> residents = room.getResidents();
        List<MandatoryFee> mandatoryFees = room.getMandatoryFees();
        List<DonationFee> donationFees = room.getDonationFees();
        for (int i = 0; i < donationFees.size(); i++) {
            feeController.eraseDonationFee(donationFees.get(i));
        }
        for (int i = 0; i < mandatoryFees.size(); i++) {
            feeController.eraseMandatoryFee(mandatoryFees.get(i));
        }
        for (int i = 0; i < residents.size(); i++) {
            residentController.eraseResident(residents.get(i));
        }
        updateCloseTimeInRoomHistory(room);
        RoomRepo.delete(room);
        return;
    }

    public void updateCloseTimeInRoomHistory(Room room) {
        RoomHistory a = RoomHistoryRepo.findByKey(room.getKey()).get(0);
        a.setdayOut(getTime());
        RoomHistoryRepo.save(a);
    }

    public String getTime() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String curDate_string = currentDate.format(dateFormatter);
        return curDate_string;
    }

    @GetMapping("/manager/history/room/full_detail/{key}")
    public String history_detail_room(@PathVariable("key") String key, Model model, HttpServletRequest request) {
        boolean flag1 = false;
		Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
					flag1 = true;
                }
            }
        }
		if (!flag1) return "404";
        List<RoomHistory> rooms = RoomHistoryRepo.findByKey(key);
        java.util.List<Request> listRequest1 = RequestRepo.findAll();
		java.util.List<AddResidentRequest> listRequest2 = AddResidentRequestRepo.findAll();
		int num1 = 0;
		for (Request req : listRequest1) {
			if (req.getApproved() == 1) {num1 ++;}
		}
		int num2 = 0;
		for (AddResidentRequest Addreq : listRequest2) {
			if (Addreq.getApproved() == 1) {num2 ++;}
		}
		int numNoti = num1 + num2;
		model.addAttribute("numNoti", numNoti);
        if (!rooms.isEmpty()) {
            RoomHistory room = rooms.get(0);
            model.addAttribute("room", room);
            List<ResidentHistory> listResident = room.getResidents();
            model.addAttribute("listResident", listResident);
            List<MandatoryFeeHistory> listFees = room.getMandatoryFees();
            model.addAttribute("listFees", listFees);
            List<DonationFeeHistory> listDonationFees = room.getDonationFees();
            model.addAttribute("listDonationFees", listDonationFees);
            return "manager/room/history_detail";
        } else {
            return "404";
        }
    }    
}
