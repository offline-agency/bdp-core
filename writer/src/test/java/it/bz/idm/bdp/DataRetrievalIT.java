// Copyright © 2018 IDM Südtirol - Alto Adige (info@idm-suedtirol.com)
// Copyright © 2019 NOI Techpark - Südtirol / Alto Adige (info@opendatahub.bz.it)
//
// SPDX-License-Identifier: GPL-3.0-only

package it.bz.idm.bdp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.map.SingletonMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import it.bz.idm.bdp.dal.DataType;
import it.bz.idm.bdp.dal.MeasurementAbstract;
import it.bz.idm.bdp.dal.Measurement;
import it.bz.idm.bdp.dal.Station;
import it.bz.idm.bdp.dto.DataMapDto;
import it.bz.idm.bdp.dto.DataTypeDto;
import it.bz.idm.bdp.dto.EventDto;
import it.bz.idm.bdp.dto.RecordDtoImpl;
import it.bz.idm.bdp.dto.SimpleRecordDto;
import it.bz.idm.bdp.dto.StationDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackages = "it.bz.idm.bdp")
@Component
@WebAppConfiguration
@TestPropertySource(properties = {
	"spring.flyway.enabled=false"
})
public class DataRetrievalIT extends WriterTestSetup {

	@Test
	public void testStationFetch() {
		Station station = Station.findStation(em, PREFIX + "non-existent-stationtype", PREFIX + "hey");
		assertNull(station);
		List<Station> stationsWithOrigin = Station.findStations(em, PREFIX + "TrafficSensor", PREFIX + "FAMAS-traffic");
		assertNotNull(stationsWithOrigin);
		List<Station> stations = Station.findStations(em, PREFIX + "TrafficSensor", null);
		assertNotNull(stations);
	}

	@Test
	public void testFindLatestEntry() {
		Integer period = 500;
		DataType type = DataType.findByCname(em, this.type.getCname());
		Station station = Station.findStation(em, this.station.getStationtype(), this.station.getStationcode());
		MeasurementAbstract latestEntry = new Measurement().findLatestEntry(em, station, type, period);
		assertNotNull(latestEntry);
		assertEquals(period, latestEntry.getPeriod());
		assertTrue(this.station.getActive());
		assertTrue(this.station.getAvailable());
	}

	@Test
	public void testSyncStations() {
		StationDto s = new StationDto(PREFIX + "WRITER", "Some name", null, null);
		List<StationDto> dtos = new ArrayList<StationDto>();
		dtos.add(s);
		ResponseEntity<Object> result = dataManager.syncStations(
			PREFIX + "EnvironmentStation",
			dtos,
			null,
			"testProvenance",
			"testProvenanceVersion",
			true,
			false
		);
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
	}

	@Test
	public void testSyncDataTypes() {
		DataTypeDto t = new DataTypeDto(PREFIX + "WRITER", null, null, null);
		List<DataTypeDto> dtos = new ArrayList<DataTypeDto>();
		dtos.add(t);
		ResponseEntity<Object> result = dataManager.syncDataTypes(dtos, null);
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
	}

	@Test
	public void testAddEvents() {
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

		EventDto t = new EventDto();
		t.setUuid(UUID.randomUUID().toString());
		t.setCategory("category");
		t.setDescription("description");
		t.setEventEnd(new Date().getTime());
		t.setEventStart(new Date().getTime());
		Coordinate coordinate = new Coordinate(45., 11.);
		t.setWktGeometry(geometryFactory.createPoint(coordinate).toText());
		t.setLocationDescription("Fake location");
		Map<String, Object> metaData = new HashMap<>();
		metaData.put("test", 5);
		t.setMetaData(metaData);
		t.setOrigin("origin");
		t.setEventSeriesUuid(UUID.randomUUID().toString());
		t.setName("some-event-name");
		t.setProvenance("12345678");
		List<EventDto> dtos = new ArrayList<>();
		dtos.add(t);
		ResponseEntity<Object> result = dataManager.addEvents(dtos, null);
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
	}

	@Test
	public void testDuplicateStations() {
		List<StationDto> dtos = new ArrayList<StationDto>();
		dtos.add(new StationDto(PREFIX + "WRITER", "Some name 1", null, null));
		dtos.add(new StationDto(PREFIX + "WRITER", "Some name 1", null, null));
		dtos.add(new StationDto(PREFIX + "WRITER", "Some name 2", null, null));
		ResponseEntity<Object> result = dataManager.syncStations(
			STATION_TYPE,
			dtos,
			null,
			"testProvenance",
			"testProvenanceVersion",
			true,
			false
		);
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
	}

	@Test
	public void testDuplicateMeasurements() {
		List<RecordDtoImpl> values = new ArrayList<>();
		values.add(new SimpleRecordDto(measurementOld.getTimestamp().getTime(), measurementOld.getValue(), measurementOld.getPeriod()));
		values.add(new SimpleRecordDto(measurement.getTimestamp().getTime(), measurement.getValue(), measurement.getPeriod()));
		values.add(new SimpleRecordDto(measurementOld.getTimestamp().getTime(), measurementOld.getValue(), measurementOld.getPeriod()));

		// Number measurements newer as the latest entry
		values.add(new SimpleRecordDto(measurement.getTimestamp().getTime() + 1000, 3.33, 1800));
		values.add(new SimpleRecordDto(measurement.getTimestamp().getTime() + 1000, 3.33, 1800));

		// String measurements newer as the latest entry
		values.add(new SimpleRecordDto(measurement.getTimestamp().getTime() + 1000, "abc", 1800));
		values.add(new SimpleRecordDto(measurement.getTimestamp().getTime() + 1000, "abc", 1800));

		// JSON measurements newer as the latest entry
		values.add(new SimpleRecordDto(measurement.getTimestamp().getTime() + 1000, new SingletonMap("a", 1), 1800));
		values.add(new SimpleRecordDto(measurement.getTimestamp().getTime() + 1000, new SingletonMap("a", 1), 1800));

		ResponseEntity<Object> result = dataManager.pushRecords(
			STATION_TYPE,
			null,
			DataMapDto.build(provenance.getUuid(), station.getStationcode(), type.getCname(), values)
		);
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
	}

}
