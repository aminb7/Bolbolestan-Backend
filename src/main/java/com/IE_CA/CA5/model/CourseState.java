package com.IE_CA.CA5.model;

public enum CourseState {
	FINALIZED,
	NON_FINALIZED;

	@Override
	public String toString() {
		if (this == FINALIZED) return "finalized";
		else return "non-finalized";
	}
}
