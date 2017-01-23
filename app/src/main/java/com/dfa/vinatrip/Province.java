package com.dfa.vinatrip;

public class Province {
    private String name;
    private String title;
    private String linkPhoto;

    public Province() {
    }

    public Province(String name, String title, String linkPhoto) {
        this.name = name;
        this.title = title;
        this.linkPhoto = linkPhoto;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }


    public String getLinkPhoto() {
        return linkPhoto;
    }
}
