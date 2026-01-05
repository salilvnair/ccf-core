package com.github.salilvnair.ccf.core.component.generator.type;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Salil V Nair
 */

public enum ComponentGeneratorType {
	GENERATE_USER_DATA(Value.GENERATE_USER_DATA),

	;

	public static class Value {
		public static final String GENERATE_USER_DATA = "GENERATE_USER_DATA";
	}

	private final String type;

	ComponentGeneratorType(String type) {
		this.type = type;
	}

	public String value() {
		return type;
	}

	public static ComponentGeneratorType type(String name) {
		Optional<ComponentGeneratorType> typeEnum = Arrays.stream(ComponentGeneratorType.values())
				.filter(comp -> comp.value() != null && comp.value().equals(name)).findFirst();
		return typeEnum.orElse(null);
	}
}
