package net.meteor.common.util;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

import java.util.ArrayList;
import java.util.List;

public class Util {
	
	public static List<String> getFormattedLines(String unlocal, TextFormatting ecf) {
		List<String> lines = new ArrayList<>();
		String s = I18n.translateToLocalFormatted(unlocal, "\n");
		String[] seperated = s.split("\\n");
		for (String value : seperated) {
			lines.add(ecf + value);
		}
		return lines;
	}

}
