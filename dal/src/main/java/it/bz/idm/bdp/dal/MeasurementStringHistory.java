package it.bz.idm.bdp.dal;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

@Table(name="measurementstringhistory",schema="intime")
@Entity
public class MeasurementStringHistory {

    @Id
    @GeneratedValue(generator="measurementstringhistory_id_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="measurementstringhistory_id_seq", sequenceName = "measurementstringhistory_id_seq",schema="intime",allocationSize=1)
	private Long id;
	private Date created_on;
	private Date timestamp;
	private String value;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private Station station;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	private DataType type;

	private Integer period;

	public MeasurementStringHistory() {
	}
	public MeasurementStringHistory(Station station, DataType type,
			String value, Date timestamp, Integer period) {
		this.station = station;
		this.type = type;
		this.value = value;
		this.timestamp = timestamp;
		this.created_on = new Date();
		this.period = period;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public static MeasurementStringHistory findRecord(EntityManager em,Station station,
			DataType type, String value, Date timestamp,
			Integer period) {
		
		TypedQuery<MeasurementStringHistory> history = em.createQuery("SELECT record FROM MeasurementStringHistory record WHERE record.station = :station  AND record.type=:type AND record.timestamp=:timestamp AND record.value=:value AND record.period=:period", MeasurementStringHistory.class);
		history.setParameter("station", station);
		history.setParameter("type", type);
		history.setParameter("value", value);
		history.setParameter("timestamp", timestamp);
		history.setParameter("period", period);
		
		List<MeasurementStringHistory> resultList = history.getResultList();
		return resultList.isEmpty()?null:resultList.get(0);
	}
	public static Date findTimestampOfNewestRecordByStationId(EntityManager em,String stationtype, String id){
		TypedQuery<Date> query;
		if (stationtype != null){
			query = em.createQuery("SELECT record.timestamp from MeasurementString record WHERE record.class=:stationtype AND record.station.stationcode=:stationcode", Date.class);
			query.setParameter("stationtype",stationtype);
		}
		else
			query = em.createQuery("SELECT record.timestamp from MeasurementString record WHERE record.station.stationcode=:stationcode", Date.class);
		query.setParameter("stationcode", id);
		List<Date> resultList = query.getResultList();
		return resultList.isEmpty()? new Date(0):resultList.get(0);
	}
	
	
}
