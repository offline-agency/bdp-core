// Copyright © 2018 IDM Südtirol - Alto Adige (info@idm-suedtirol.com)
// Copyright © 2019 NOI Techpark - Südtirol / Alto Adige (info@opendatahub.com)
//
// SPDX-License-Identifier: GPL-3.0-only

package it.bz.idm.bdp.dal;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import it.bz.idm.bdp.dto.StationDto;

/**
 * <p>
 * MetaData is a versioned JSONB map containing all additional information for a<br/>
 * {@link Station}. If a data collector provides a different meta data object it<br/>
 * will replace the current one shown as meta data through the API.
 * </p>
 *
 * @author Peter Moser
 * @author Patrick Bertolla
 *
 */
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Entity
@Table(name = "metadata")
public class MetaData {

	@Id
	@GeneratedValue(generator = "metadata_gen", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "metadata_gen", sequenceName = "metadata_seq", allocationSize = 1)
	@ColumnDefault(value = "nextval('metadata_seq')")
	protected Long id;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private Map<String, Object> json;

	@ManyToOne
	private Station station;

	private Date created_on;

	public MetaData() {
		created_on = new Date();
	}

	public Map<String, Object> getJson() {
		return json;
	}

	/**
	 * Set JSON data (= meta data). We do not eliminate null values here,
	 * because we want to set values to null to remove them from the result.
	 * For instance, when retrieving a {@link StationDto}.
	 *
	 * @param metaData a key/object map, containing whatever you want
	 */
	public void setJson(Map<String, Object> metaData) {
		this.json = metaData;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public Date getCreated() {
		return created_on;
	}
}
