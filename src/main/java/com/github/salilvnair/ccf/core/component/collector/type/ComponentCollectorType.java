package com.github.salilvnair.ccf.core.component.collector.type;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Salil V Nair
 */

public enum ComponentCollectorType {
	//types
	SUBMIT_REQUEST(Value.SUBMIT_REQUEST),
	CANCEL_REQUEST(Value.CANCEL_REQUEST),
	;

	public static class Value {
		public static final String SUBMIT_REQUEST = "SUBMIT_REQUEST";
		public static final String CANCEL_REQUEST = "CANCEL_REQUEST";
	}

	private final String type;

	ComponentCollectorType(String type) {
		this.type = type;
	}

	public String value() {
		return type;
	}

	public static ComponentCollectorType type(String name) {
		Optional<ComponentCollectorType> typeEnum = Arrays.stream(ComponentCollectorType.values())
				.filter(comp -> comp.value() != null && comp.value().equals(name)).findFirst();
		return typeEnum.orElse(null);
	}
}
