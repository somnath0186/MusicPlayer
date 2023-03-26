package com.jspider.multiplayer;



public class Song {
     
	private int id;
	private String name;
	private double duration;
	private String album;
	private String singer;
	
	
	
	
	@Override
	public String toString() {
		return "Song [id=" + id + ", name=" + name + ", duration=" + duration + ", album=" + album + ", singer="
				+ singer + "]";
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	
	
	
	
	
	
	   }

