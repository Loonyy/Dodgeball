/*******************************************************************************
 * Copyright (C) 2016 (jedk1)
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package minigame.dodgeball.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public abstract class Serializer {

	private static Logger log;
	
	/** GSON Version used when serialising. **/
	private static final double version = 1.0;
	private File file;
	
	public Serializer(File file) {
		this.file = file;
	}
	
	/**
	 * Gets the file to be serialized
	 * @return file to be serialized
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * Sets the file that will be serialized
	 * @param file to be serialized
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	/**
	 * Serialize the object and save it to the file
	 */
	public void serialize() {
		try {
			create();
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			out.write(getGson().toJson(this));
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the object from file
	 * @return
	 */
	public Object get() {
		return get(getClass(), file);
	}
	
	/**
	 * Gets the object from file
	 * @param caller Class<?> of the object being deserialized
	 * @param file File containing the object
	 * @return Deserialized Object
	 */
	public static Object get(Class<?> caller, File file) {
		try {
			if (!file.exists()) {
				return null;
			}
			if (file.length() == 0) {
				return null;
			}
			JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")));
			Gson gson = new Gson();
			reader.setLenient(true);
			Object object = gson.fromJson(reader, caller);
			reader.close();
			if (object != null && object instanceof Serializer) {
				((Serializer) object).setFile(file);
			}
			return object;
		} catch (Exception e) {
			log.warning("Error loading GSON Object '" + caller.getSimpleName() + "' for '" + file.toString() + "'");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Create the file and parent directories for the the file
	 */
	private void create() {
		if (!file.getParentFile().exists()) {
			try {
				file.getParentFile().mkdirs();
			} catch (Exception e) {
				log.warning("Failed to generate directory!");
				e.printStackTrace();
			}
		}

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				log.warning("Failed to generate " + file.getName() + "!");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Checks if the file exists
	 * @return
	 */
	public boolean exists() {
		return file.exists();
	}

	/**
	 * Deletes the file
	 */
	public void delete() {
		file.delete();
	}
	
	/**
	 * Gets the GSON instance for serializing
	 * @return
	 */
	private static Gson getGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.setVersion(version);
		builder.serializeNulls();
		builder.setPrettyPrinting();
		builder.enableComplexMapKeySerialization();
		builder.excludeFieldsWithoutExposeAnnotation();
		return builder.create();
	}
	
	/**
	 * Set the logger to be used
	 * @param log Logger to be used
	 */
	public static void setLogger(Logger log) {
		Serializer.log = log;
	}
}