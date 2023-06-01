// Copyright © 2018 IDM Südtirol - Alto Adige (info@idm-suedtirol.com)
// Copyright © 2019 NOI Techpark - Südtirol / Alto Adige (info@opendatahub.bz.it)
//
// SPDX-License-Identifier: GPL-3.0-only

package it.bz.idm.bdp.dal;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import it.bz.idm.bdp.dto.RecordDto;

@Table(
	name = "measurementstringhistory",
	indexes = {
		@Index(
			columnList = "station_id, type_id, timestamp DESC, period"
		)
	},
	uniqueConstraints = {
		@UniqueConstraint(
			columnNames = {"station_id", "type_id", "timestamp", "period", "string_value"}
		)
	}
)
@Entity
public class MeasurementStringHistory extends MeasurementAbstractHistory {

	@Transient
	private static final long serialVersionUID = 8968054299664379971L;

	@Id
	@GeneratedValue(generator = "measurementstringhistory_gen", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "measurementstringhistory_gen", sequenceName = "measurementstringhistory_seq", allocationSize = 1)
	@ColumnDefault(value = "nextval('measurementstringhistory_seq')")
	private Long id;

    /*
     * Make sure all subclasses of MHistory contain different value names. If these
     * variable names would be called the same, but with different data types
     * Hibernate would complain about not being able to create a SQL UNION.
     * Ex. private String value; and private Double value; would not work
     *     inside MeasurementStringHistory and MeasurementHistory respectively
     */
	@Column(nullable = false)
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String stringValue;

	public MeasurementStringHistory() {
		super();
	}

	/**
	 * @param station entity associated with this measurement
	 * @param type entity associated with this measurement
	 * @param value string value for this measurement
	 * @param timestamp UTC time of measurement detection
	 * @param period standard interval between 2 measurements
	 */
	public MeasurementStringHistory(Station station, DataType type, String value, Date timestamp, Integer period) {
		setStation(station);
		setType(type);
		setTimestamp(timestamp);
		setPeriod(period);
		setValue(value);
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public String getValue() {
		return stringValue;
	}

	public void setValue(String value) {
		this.stringValue = value;
	}

	@Override
	public List<RecordDto> findRecords(EntityManager em, String stationtype, String identifier, String cname, Date start, Date end, Integer period) {
		return findRecordsImpl(em, stationtype, identifier, cname, start, end, period, this);
	}
	@Override
	public void setValue(Object value) {
		if (value != null)
			setValue(value.toString());
	}

}
