package com.xx.lxz.bean;

import java.io.Serializable;

public class Area implements Serializable {
	private String code;
	private String name;
	private String pcode;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	public Area() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Area [code=" + code + ", name=" + name + ", pcode=" + pcode
				+ "]";
	}
	
	
}
