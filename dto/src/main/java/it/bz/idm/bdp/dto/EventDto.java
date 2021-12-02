/**
 * dto - Data Transport Objects for an object-relational mapping
 *
 * Copyright © 2018 IDM Südtirol - Alto Adige (info@idm-suedtirol.com)
 * Copyright © 2019 NOI Techpark - Südtirol / Alto Adige (info@opendatahub.bz.it)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program (see LICENSES/GPL-3.0.txt). If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * SPDX-License-Identifier: GPL-3.0
 */
package it.bz.idm.bdp.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import io.swagger.annotations.ApiModelProperty;

/**
 * Data transfer object representing an event
 *
 * @author Patrick Bertolla
 */
@JsonInclude(value=Include.NON_EMPTY)
public class EventDto implements Serializable {

	private static final long serialVersionUID = 7928534360551629831L;

	@ApiModelProperty (notes = "The unique ID associated to the event.")
	@JsonProperty(required = true)
	@JsonPropertyDescription("Unique event code describing a single event (ex. fog1458)")
	protected String id;

	@ApiModelProperty (notes = "The event description")
	@JsonProperty(required = false)
	@JsonPropertyDescription("Describes the event in few words")
	protected String description;

	@ApiModelProperty (notes = "The event category")
	@JsonProperty(required = false)
	@JsonPropertyDescription("describes a group in which the event falls e.g. a car accident can be part of the category traffic jam ")
	protected String category;

	@JsonPropertyDescription("Who provided the event?")
	private String origin;

	@JsonPropertyDescription("Meta data, describing additional features of the event")
	private Map<String, Object> metaData = new HashMap<>();

	@ApiModelProperty (notes = "The event location")
	@JsonProperty(required = false)
	@JsonPropertyDescription("A short text summarizing the location")
	protected String locationDescription;

	@ApiModelProperty (notes = "The geografic representation of the location using the projection EPSG:4326")
	@JsonProperty(required = false)
	@JsonPropertyDescription("Well-known Text representation of this Geometry(OpenGIS Simple Features Specification)")
	private String wktGeometry;

	@ApiModelProperty (notes = "The start time of the event (included, we have a half-open interval)")
	@JsonProperty(required = false)
	@JsonPropertyDescription("Start time as unix timestamp in milliseconds")
	private Long eventStart;

	@ApiModelProperty (notes = "The end time of the event (excluded, we have a half-open interval)")
	@JsonProperty(required = false)
	@JsonPropertyDescription("End time as unix timestamp in milliseconds (excluded)")
	private Long eventEnd;

	@ApiModelProperty (notes = "The data collector name and version that inserts this event")
	@JsonProperty(required = true)
	@JsonPropertyDescription("The UUID of a data collector name and version")
	private String provenance;

	public String getWktGeometry() {
		return wktGeometry;
	}

	public void setWktGeometry(String wktGeometry) {
		this.wktGeometry = wktGeometry;
	}


	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Map<String, Object> getMetaData() {
		return metaData;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getEventStart() {
		return eventStart;
	}

	public void setEventStart(Long eventStart) {
		this.eventStart = eventStart;
	}

	public Long getEventEnd() {
		return eventEnd;
	}

	public void setEventEnd(Long eventEnd) {
		this.eventEnd = eventEnd;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	public void setMetaData(Map<String, Object> metaData) {
		if (metaData == null) {
			this.metaData = null;
			return;
		}
		for (Entry<String, Object> entry : metaData.entrySet()) {
			if (entry.getValue() != null && entry.getKey() != null) {
				this.metaData.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public String getProvenance() {
		return provenance;
	}

	public void setProvenance(String provenanceUuid) {
		this.provenance = provenanceUuid;
	}

	@JsonIgnore
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EventDto){
			EventDto dto =(EventDto) obj;
			if (this.getId().equals(dto.getId()))
				return true;
		}
		return false;
	}
	@JsonIgnore
	@Override
	public int hashCode() {
		return 1;
	}
}
