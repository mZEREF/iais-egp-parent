package com.ecquaria.cloud.moh.iais.helper.excel;

public enum BooleanEnum {

	TrueY("Y", Boolean.TRUE),
	True("TRUE", Boolean.TRUE),
	TrueNum("1", Boolean.TRUE),
	TrueYes("YES", Boolean.TRUE),
	TrueT("T", Boolean.TRUE),
	FalseN("N", Boolean.FALSE),
	False("FALSE", Boolean.FALSE),
	FalseNum("0", Boolean.FALSE),
	FalseNo("NO", Boolean.FALSE),
	FalseF("F", Boolean.FALSE);
	
	
	private final String name;
	private final Boolean value;

	BooleanEnum(String name, Boolean value) {
		this.name = name;
		this.value = value;
	}

	String getName() {
		return name;
	}

	Boolean getValue() {
		return value;
	}
}
