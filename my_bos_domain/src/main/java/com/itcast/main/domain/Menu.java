package com.itcast.main.domain;

public class Menu {

	private int id;
	private int pid;
	private String name;
	private boolean open;
	private String file;

	public Menu() {
	}

	public Menu(int id, int pid, String name, String file, boolean open) {
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.open = open;
		this.file = file;
	}

	public Menu(int id, int pid, String name, String file) {
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.file = file;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
