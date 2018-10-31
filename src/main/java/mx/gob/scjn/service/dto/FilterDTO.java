package mx.gob.scjn.service.dto;

import java.util.Set;
import java.util.TreeSet;

public class FilterDTO {

	Set<String> country = new TreeSet<String>();

	Set<String> group = new TreeSet<String>();

	public Set<String> getCountry() {
		return country;
	}

	public void setCountry(Set<String> country) {
		this.country = country;
	}

	public Set<String> getGroup() {
		return group;
	}

	public void setGroup(Set<String> group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "FilterDTO [country=" + country + ", group=" + group + "]";
	}
}
