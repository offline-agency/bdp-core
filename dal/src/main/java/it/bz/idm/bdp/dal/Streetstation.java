package it.bz.idm.bdp.dal;

import it.bz.idm.bdp.dal.bluetooth.StreetBasicData;
import it.bz.idm.bdp.dal.meteo.Meteostation;
import it.bz.idm.bdp.dto.StationDto;
import it.bz.idm.bdp.dto.StreetStationDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Entity
public class Streetstation extends ElaborationStation{

	@Override
	public List<StationDto> convertToDtos(EntityManager em, List<Station> resultList) {
		List<StationDto> stationList = new ArrayList<StationDto>();
		for (Station station: resultList){
			Double x = null,y = null;
			if (station.getPointprojection() != null){
				y = station.getPointprojection().getY();
				x = station.getPointprojection().getX();
			}
			StreetBasicData basicData = (StreetBasicData) new StreetBasicData().findByStation(em, station);
			if (basicData == null)
				continue;
			StreetStationDto dto = new StreetStationDto(station.getStationcode(),station.getName(),y,x);
			dto.setDescription(basicData.getDescription());
			dto.setOld_idstr(basicData.getOld_idstr());
			dto.setSpeed_default(basicData.getSpeed_default());
			dto.setLength(basicData.getLength());
			stationList .add(dto);
		}
		return stationList;
	}

	@Override
	public List<String[]> findDataTypes(EntityManager em,String stationId) {
		TypedQuery<Object[]> query;
		if (stationId == null) {
			query = em.createQuery("SELECT elab.type.cname,elab.type.cunit,elab.type.description,elab.period FROM Elaboration elab INNER JOIN elab.type where elab.station.class=:stationType GROUP BY elab.type.cname,elab.type.cunit,elab.type.description,elab.period",Object[].class);
			query.setParameter("stationType", this.getClass().getSimpleName());
		} else {
			query = em.createQuery("SELECT elab.type.cname,elab.type.cunit,elab.type.description,elab.period FROM Elaboration elab INNER JOIN elab.type where elab.station.class=:stationType AND elab.station.stationcode=:station GROUP BY elab.type.cname,elab.type.cunit,elab.type.description,elab.period",Object[].class);
			query.setParameter("stationType", this.getClass().getSimpleName());
			query.setParameter("station",stationId);
		}
		
		List<Object[]> resultList = query.getResultList();
		if (resultList.isEmpty())
			return new Meteostation().findDataTypes(em,stationId);
		return getDataTypesFromQuery(resultList);
	}

	@Override
	public Date getDateOfLastRecord(EntityManager em, Station station, DataType type, Integer period) {
		Date date = null;
		if (station != null){
			String queryString = "select record.timestamp from Elaboration record where record.station=:station";
			if (type != null){
				queryString += " AND record.type = :type";
			}
			if (period != null){
				queryString += " AND record.period=:period";
			}
			queryString += " ORDER BY record.timestamp DESC";
			TypedQuery<Date> query = em.createQuery(queryString, Date.class);
			query.setParameter("station", station);
			if (type!=null)
				query.setParameter("type", type);
			if (period!=null)
				query.setParameter("period", period);
			List<Date> resultList = query.getResultList();
			date = resultList.isEmpty() ? new Date(0) : resultList.get(0);
		}
		return date;
	}

	@Override
	public Object pushRecords(EntityManager em, Object... object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sync(EntityManager em, Station station, StationDto dto) {
		// TODO Auto-generated method stub
		
	}

}
