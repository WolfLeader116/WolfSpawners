package io.github.pseudoresonance.pseudospawners;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetNMSName {
	
	/**
	 * Code for getting names from locale was written with the suggestion of KyleDaHorsey
	 */

	private static HashMap<Integer, String> nameMap = new HashMap<Integer, String>();

	public static void getNames() {
		try {
			Class<?> localeClass = Class.forName("net.minecraft.server." + PseudoSpawners.getBukkitVersion() + ".LocaleLanguage");
			Class<?> entityTypes = Class.forName("net.minecraft.server." + PseudoSpawners.getBukkitVersion() + ".EntityTypes");
			Object locale = localeClass.newInstance();
			Field nameListField = entityTypes.getDeclaredField("g");
			nameListField.setAccessible(true);
			Object nameList = nameListField.get(null);
			if (Integer.valueOf(PseudoSpawners.getBukkitVersion().split("_")[1]) >= 11) {
				if (nameList instanceof List) {
					@SuppressWarnings("unchecked")
					List<Object> nameIterate = (List<Object>) nameList;
					for (int id = 0; id < nameIterate.size(); id++) {
						try {
							Object o = nameIterate.get(id);
							if (o instanceof String) {
								String name = (String) o;
								Method trans = Arrays.stream(locale.getClass().getMethods()).filter(m -> m.getReturnType().equals(String.class)).filter(m -> m.getParameterCount() == 1).filter(m -> m.getParameters()[0].getType().equals(String.class)).collect(Collectors.toList()).get(0);
								String friendlyName = (String) trans.invoke(locale, "entity." + name + ".name");
								nameMap.put(id, friendlyName);
							}
						} catch (IndexOutOfBoundsException e) {}
					}
				}
			} else {
				if (nameList instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<Object, Object> nameIterate = (Map<Object, Object>) nameList;
					for (Object o : nameIterate.keySet()) {
						Object oid = nameIterate.get(o);
						if (o instanceof String && oid instanceof Integer) {
							String name = (String) o;
							int id = (Integer) oid;
							Method trans = Arrays.stream(locale.getClass().getMethods()).filter(m -> m.getReturnType().equals(String.class)).filter(m -> m.getParameterCount() == 1).filter(m -> m.getParameters()[0].getType().equals(String.class)).collect(Collectors.toList()).get(0);
							String friendlyName = (String) trans.invoke(locale, "entity." + name + ".name");
							nameMap.put(id, friendlyName);
						}
					}
				}
			}
		} catch (ClassNotFoundException | IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}
	}

}
