package com.github.st3iny.jxml;

final class Helper {

	public static void checkName(String name, String source) throws XException {
		if (name.contains(" "))
			throw new XException("The name of an " + source + " can't contain spaces!");
		if (name.startsWith("0") || name.startsWith("1") || name.startsWith("2") || name.startsWith("3") || name.startsWith("4") || name.startsWith("5") || name.startsWith("6") || name.startsWith("7") || name.startsWith("8") || name.startsWith("9"))
			throw new XException("The name of an " + source + " can't start with a number!");
	}

}
