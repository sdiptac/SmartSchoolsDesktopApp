package application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PageType {
	public enum Type{
		LOGIN,
		MAIN_MENU,
		LOCATION,
		USER,
		QUESTION,
		QUALTRICS,
		FITBIT,
		PREFERENCES,
		WIPE,
		SERIOUSWIPE
	}
	
	 public static final Map<Type, String> pageMap;
	    static {
	        Map<Type, String> map = new HashMap<Type, String>();
	        map.put(Type.LOGIN, "Login");
	        map.put(Type.MAIN_MENU, "MainMenu");
	        map.put(Type.LOCATION, "LocationsPage");
	        map.put(Type.USER, "UsersPage");
	        map.put(Type.QUESTION, "QuestionsPage");
	        map.put(Type.QUALTRICS, "Qualtrics");
	        map.put(Type.FITBIT, "FitbitPage");
	        map.put(Type.PREFERENCES, "Preferences");
	        map.put(Type.WIPE, "Wipe");
	        map.put(Type.SERIOUSWIPE, "SeriousWipe");
	        pageMap = Collections.unmodifiableMap(map);
	    }
}
