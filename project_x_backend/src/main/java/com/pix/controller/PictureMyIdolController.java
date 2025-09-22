package com.pix.controller;

import com.pix.dto.MyIdolMemberDto;
import com.pix.service.PictureMyIdolService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/myidol")
@CrossOrigin(origins = "http://localhost:5173")
public class PictureMyIdolController {

    private final PictureMyIdolService pictureMyIdolService;
    
    public PictureMyIdolController(PictureMyIdolService pictureMyIdolService) {
    	this.pictureMyIdolService = pictureMyIdolService;
    }
    
    @GetMapping("/members/{userId}")
    public List<MyIdolMemberDto> getMyIdolMembers(@PathVariable("userId") Long userId){
    	return pictureMyIdolService.getMyIdolMembers(userId);
    }
}
