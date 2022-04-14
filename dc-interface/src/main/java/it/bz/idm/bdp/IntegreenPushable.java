/**
 * dc-interface - Data Collector Interface for the Big Data Platform
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
package it.bz.idm.bdp;

import java.util.List;

import it.bz.idm.bdp.dto.DataMapDto;
import it.bz.idm.bdp.dto.DataTypeDto;
import it.bz.idm.bdp.dto.RecordDtoImpl;
import it.bz.idm.bdp.dto.StationDto;
import it.bz.idm.bdp.dto.StationList;

/**
 * Each data collector must implement these methods. It provides allows to push data
 * to the writer, which then saves everything to the DB.
 *
 * @author Patrick Bertolla
 */
public interface IntegreenPushable {

	/**
	 * @param data in any form with information of space, time and measurement type
	 * @return data records ordered in a tree structure
	 */
	public abstract <T> DataMapDto<RecordDtoImpl> mapData(T data);

	/**
	 * @param stationType unique existing station typology
	 * @param data data map to send to writer
	 * @return outcome of the api call
	 */
	public abstract Object pushData(String stationType, DataMapDto<? extends RecordDtoImpl> data);

	/**
	 * @param stationType unique existing station typology
	 * @param stations list of station dtos to sync with existing in database
	 * @return outcome of the api calls (single call)
	 */
	public abstract Object syncStations(String stationType, StationList stations);

	/**
	 * @param stationType unique existing station typology
	 * @param stations list of station dtos to sync with existing in database
	 * @return outcome of the api calls, more than one because we split the calls in chunks
	 */
	public abstract List<Object> syncStations(String stationType, StationList stations, int chunkSize);
	public abstract List<Object> syncStations(String stationType, StationList stations, boolean syncState, boolean onlyActivation);
	public abstract List<Object> syncStations(String stationType, StationList stations, int chunkSize, boolean syncState, boolean onlyActivation);

	/**
	 * @param stationType unique existing station typology
	 * @param data list of datatypes to sync with the existing in the database
	 * @return outcome of the api call
	 */
	public abstract Object syncDataTypes(String stationType, List<DataTypeDto> data);

	/**
	 * @param stationCode unique identifer of a station
	 * @param dataType unique identifier of a datatype
	 * @param period intervall between 2 measurements
	 * @return outcome of the api call
	 */
	public abstract Object getDateOfLastRecord(String stationCode, String dataType, Integer period);

	/**
	 * @param stationType  unique existing station typology
	 * @param origin unique reference to a webservice
	 * @return list of station dtos associated with that origin
	 */
	public abstract List<StationDto> fetchStations(String stationType, String origin);

}
