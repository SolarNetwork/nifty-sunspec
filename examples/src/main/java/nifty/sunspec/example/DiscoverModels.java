/* ==================================================================
 * DiscoverModels.java - 10 Oct 2026 9:30:18 am
 *
 * Copyright 2026 SolarNetwork.net Dev Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * ==================================================================
 */

package nifty.sunspec.example;

import java.io.IOException;
import java.util.List;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.core.ModelDataFactory;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Example of how to discover the available models from a SunSpec modbus device.
 *
 * @author matt
 * @version 1.0
 */
public final class DiscoverModels {

	private DiscoverModels() {
		// not available
	}

	/**
	 * Discover the available models on a SunSpec device and print them out.
	 *
	 * @param conn
	 *        the modbus connection to the device
	 * @throws IOException
	 *         if any IO error occurs
	 */
	public static final void discoverModels(ModbusConnection conn) throws IOException {
		ModelDataFactory factory = ModelDataFactory.getInstance();
		ModelData data = factory.discoverModels(conn);

		System.out.printf("Manufacturer: %s\n", data.getManufacturer());
		System.out.printf("Model:        %s\n", data.getModel());

		List<ModelAccessor> models = data.getModels();

		System.out.printf("%d models discovered:\n\n", models.size());

		// print out list of all model IDs with their descriptions
		for ( ModelAccessor model : models ) {
			ModelId id = model.getModelId();
			System.out.printf("%d (%s)\n", id.getId(), id.getDescription());
		}
	}

}
