package com.IE_CA.CA5.model;

public enum CourseState {
	FINALIZED,
	NON_FINALIZED;

	@Override
	public String toString() {
		return switch (this) {
			case FINALIZED -> "finalized";
			case NON_FINALIZED -> "non-finalized";
			default -> "";
		};
	}
}
