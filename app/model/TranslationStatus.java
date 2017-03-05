package model;


import java.util.*;
/**
 * Created by martin on 3/5/17.
 */
public enum TranslationStatus {
	NONE(0) ,
	COMPLETED (1),
	IN_PROGRESS(2);

	public int value;

	private static final Map<Integer, TranslationStatus> intToTypeMap = new HashMap<Integer, TranslationStatus>();
	static {
		for (TranslationStatus type : TranslationStatus.values()) {
			intToTypeMap.put(type.value, type);
		}
	}

	public static TranslationStatus fromInt(int statusEnum) {
		TranslationStatus translationStatus = intToTypeMap.get(statusEnum);
		if (translationStatus == null) {
			return NONE;
		}
		return translationStatus;
	}

	TranslationStatus(int status) {
		this.value = status;
	}
}
