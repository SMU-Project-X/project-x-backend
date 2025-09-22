package com.pix.dto;

public class MyIdolMemberDto {
	private String name;
    private String profileImageUrl;
    private String position;

    public MyIdolMemberDto(String name, String profileImageUrl, String position) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.position = position;
    }
    
    public String getName() { return name; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public String getPosition() { return position; }
}
