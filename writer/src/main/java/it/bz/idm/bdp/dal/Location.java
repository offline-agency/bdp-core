// Copyright © 2018 IDM Südtirol - Alto Adige (info@idm-suedtirol.com)
// Copyright © 2019 NOI Techpark - Südtirol / Alto Adige (info@opendatahub.com)
//
// SPDX-License-Identifier: GPL-3.0-only

package it.bz.idm.bdp.dal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Geometry;

@Table(name = "location")
@Entity
public class Location {

	@Id
	@GeneratedValue(generator = "event_location_gen", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "event_location_gen", sequenceName = "event_location_seq", allocationSize = 1)
	@ColumnDefault(value = "nextval('event_location_seq')")
	protected Long id;

	@Column(nullable = true)
	protected Geometry geometry;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String description;

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
