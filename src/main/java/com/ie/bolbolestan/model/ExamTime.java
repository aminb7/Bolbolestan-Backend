package com.ie.bolbolestan.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;

public class ExamTime implements EventTime {
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private final LocalDateTime start;
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private final LocalDateTime end;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public ExamTime(@JsonProperty("start") LocalDateTime start, @JsonProperty("end") LocalDateTime end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public boolean overlaps(EventTime otherEventTime) {
		if (getClass() != otherEventTime.getClass())
			return false;

		ExamTime other = (ExamTime) otherEventTime;
		return this.start.isBefore(other.end) && this.end.isAfter(other.start);
	}

	@Override
	public ObjectNode getJsonInfo() {
		ObjectNode result = new ObjectMapper().createObjectNode();
		result.put("start", this.start.toString());
		result.put("end", this.start.toString());
		return result;
	}

	@Override
	public String getHtmlTable() {
		String result = "<td>" + this.start + "</td>"
				+ "<td>" + this.end + "</td>";

		return result;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public LocalDateTime getEnd() {
		return end;
	}
}
