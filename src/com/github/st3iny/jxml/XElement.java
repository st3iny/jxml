package com.github.st3iny.jxml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XElement {

	public String name;
	private String value;
	public ArrayList<XAttribute> attributes;
	public ArrayList<XElement> elements;

	public XElement(String name) throws XException {
		this(name, "");
	}

	public XElement(String name, String value) throws XException {
		this(name, value, null);
	}

	public XElement(String name, XAttribute[] attributes) throws XException {
		this(name, "", attributes);
	}

	public XElement(String name, String value, XAttribute[] attributes) throws XException {
		Helper.checkName(name, "element");
		this.name = name;
		this.value = value;
		this.elements = new ArrayList<XElement>();
		this.attributes = new ArrayList<XAttribute>();
		if (attributes != null)
			for (XAttribute attribute : attributes)
				this.attributes.add(attribute);
	}

	public XElement(String name, XElement[] elements) throws XException {
		Helper.checkName(name, "element");
		this.name = name;
		this.value = "";
		this.attributes = new ArrayList<XAttribute>();
		this.elements = new ArrayList<XElement>();
		if (elements != null)
			for (XElement element : elements)
				this.elements.add(element);
	}

	public XElement(String name, XElement[] elements, XAttribute[] attributes) throws XException {
		Helper.checkName(name, "element");
		this.name = name;
		this.value = "";
		this.elements = new ArrayList<XElement>();
		if (elements != null)
			for (XElement element : elements)
				this.elements.add(element);
		this.attributes = new ArrayList<XAttribute>();
		if (attributes != null)
			for (XAttribute attribute : attributes)
				this.attributes.add(attribute);
	}

	public String getValue() {
		if (elements.size() > 0) {
			StringBuilder builder = new StringBuilder();
			for (XElement element : elements)
				element.save(builder, 0);
			return builder.toString();
		} else {
			return value;
		}
	}

	public void setValue(String value) throws XException {
		if (elements.size() > 0)
			throw new XException("This element has child elements!");
		else
			this.value = value;
	}

	public XElement getElement(String name) {
		for (XElement element : elements) {
			if (element.name.equals(name))
				return element;
		}
		return null;
	}

	public XElement[] getElements(String name) {
		ArrayList<XElement> elements = new ArrayList<XElement>();
		for (XElement element : this.elements) {
			if (element.name.equals(name))
				elements.add(element);
		}
		XElement[] result = new XElement[elements.size()];
		elements.toArray(result);
		return result;
	}

	public void save(String path) throws IOException {
		save(new File(path));
	}

	public void save(File file) throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write(toString());
		writer.flush();
		writer.close();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		save(builder, 0);
		return builder.toString();
	}

	void save(StringBuilder builder, int tab) {
		for (int i = 0; i < tab; i++)
			builder.append(XSettings.Indent);
		builder.append("<");
		builder.append(name);
		for (XAttribute attribute : attributes) {
			builder.append(" ");
			builder.append(attribute.toString());
		}
		if (elements.size() > 0) {
			builder.append(">\n");
			for (XElement element : elements)
				element.save(builder, tab + 1);
			for (int i = 0; i < tab; i++)
				builder.append(XSettings.Indent);
			builder.append("</");
			builder.append(name);
			builder.append(">");
		} else if (value != "") {
			builder.append(">");
			builder.append(value);
			builder.append("</");
			builder.append(name);
			builder.append(">");
		} else {
			builder.append(" />");
		}
		builder.append(XSettings.NewLine);
	}

	static XElement createFromDomNode(Node node, XElement parent) throws DOMException, XException {
		if (!(node.getNodeType() == Node.ELEMENT_NODE || node.getNodeType() == Node.TEXT_NODE))
			return null;
		if (node.getNodeType() == Node.TEXT_NODE && parent != null) {
			try {
				parent.setValue(node.getNodeValue());
			} catch (XException e) {
			}
			return null;
		}
		XElement element = new XElement(node.getNodeName());
		NamedNodeMap attributes = node.getAttributes();
		for (int j = 0; j < attributes.getLength(); j++) {
			Node attribute = attributes.item(0);
			element.attributes.add(new XAttribute(attribute.getNodeName(), attribute.getNodeValue()));
		}
		if (node.hasChildNodes()) {
			NodeList childs = node.getChildNodes();
			for (int i = 0; i < childs.getLength(); i++) {
				XElement element2 = createFromDomNode(childs.item(i), element);
				if (element2 != null)
					element.elements.add(element2);
			}
		} else {
			element.setValue(node.getNodeValue());
		}
		return element;
	}

}
