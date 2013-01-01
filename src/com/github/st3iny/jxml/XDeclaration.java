package com.github.st3iny.jxml;

public class XDeclaration {

	public static final XDeclaration Default = new XDeclaration("1.0");

	private String version = null;
	private String encoding = null;
	private String standalone = null;

	public XDeclaration(String version) {
		this.version = version;
	}

	public XDeclaration(String version, String encoding) {
		this.version = version;
		this.encoding = encoding;
	}

	public XDeclaration(String version, String encoding, boolean standalone) {
		this.version = version;
		this.encoding = encoding;
		this.standalone = standalone == true ? "true" : "false";
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"");
		builder.append(version);
		builder.append("\"");
		if (encoding != null) {
			builder.append(" encoding=\"");
			builder.append(encoding);
			builder.append("\"");
		}
		if (standalone != null) {
			builder.append(" standalone=\"");
			builder.append(standalone);
			builder.append("\"");
		}
		builder.append(" ?>");
		return builder.toString();
	}

}
