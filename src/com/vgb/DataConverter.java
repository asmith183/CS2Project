package com.vgb;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

/**
 * Aden Smith 2025-3-19
 * 
 * Converts the VGB invoice CSV data files to XML and JSON
 */
public class DataConverter {

	public static void main(String[] args) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		xstream.alias("address", Address.class);
		xstream.alias("company", Company.class);
		xstream.alias("contract", Contract.class);
		xstream.alias("equipment", Equipment.class);
		xstream.alias("item", Item.class);
		xstream.alias("material", Material.class);
		xstream.alias("person", Person.class);

		List<Person> personData = DataLoader.loadPersonData("data/Persons.csv");
		List<Company> companyData = DataLoader.loadCompanyData("data/Companies.csv", personData);
		List<Item> itemData = DataLoader.loadItemData("data/Items.csv", companyData);

		String personDataXML = xstream.toXML(personData);
		String companyDataXML = xstream.toXML(companyData);
		String itemDataXML = xstream.toXML(itemData);

		try {
			FileWriter personFileWriter = new FileWriter("data/Persons.xml");
			FileWriter companyFileWriter = new FileWriter("data/Companies.xml");
			FileWriter itemFileWriter = new FileWriter("data/Items.xml");

			personFileWriter.write(personDataXML);
			companyFileWriter.write(companyDataXML);
			itemFileWriter.write(itemDataXML);

			personFileWriter.close();
			companyFileWriter.close();
			itemFileWriter.close();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String personDataJson = gson.toJson(personData);
		String companyDataJson = gson.toJson(companyData);
		String itemDataJson = gson.toJson(itemData);

		try {
			FileWriter personFileWriter = new FileWriter("data/Persons.json");
			FileWriter companyFileWriter = new FileWriter("data/Companies.json");
			FileWriter itemFileWriter = new FileWriter("data/Items.json");

			personFileWriter.write(personDataJson);
			companyFileWriter.write(companyDataJson);
			itemFileWriter.write(itemDataJson);

			personFileWriter.close();
			companyFileWriter.close();
			itemFileWriter.close();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
