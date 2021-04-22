package com.IE_CA.CA5.model;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface EventTime {
	public boolean overlaps(EventTime other);
	public ObjectNode getJsonInfo();

	public String getHtmlTable();
}
