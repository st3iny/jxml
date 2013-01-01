package com.github.st3iny.jxml;

public class XAttribute {

	public String name;
	public String value;
	
	public XAttribute(String name, String value) throws XException {
		Helper.checkName(name, "attribute");
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return String.format("%s=\"%s\"", name, value);
	}

}
