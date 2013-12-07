package de.sensorcloud.android.entitaet;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ParVor {

	private String nr;
	private String pn;
	private String pns;
	private W w;
	private String eh;
	private String dt;
	private Map<String, String> sem = new TreeMap<String, String>();
	private ArrayList<String> aggfkt = new ArrayList<String>();
	private String ufkt;
	private String erl;

	
	public String getNr() {
		return nr;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}

	public String getPns() {
		return pns;
	}

	public void setPns(String pns) {
		this.pns = pns;
	}

	public W getW() {
		return w;
	}

	public void setW(W w) {
		this.w = w;
	}

	public String getEh() {
		return eh;
	}

	public void setEh(String eh) {
		this.eh = eh;
	}

	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public Map<String, String> getSem() {
		return sem;
	}

	public void setSem(Map<String, String> sem) {
		this.sem = sem;
	}

	public ArrayList<String> getAggfkt() {
		return aggfkt;
	}

	public void setAggfkt(ArrayList<String> aggfkt) {
		this.aggfkt = aggfkt;
	}

	public String getUfkt() {
		return ufkt;
	}

	public void setUfkt(String ufkt) {
		this.ufkt = ufkt;
	}

	public String getErl() {
		return erl;
	}

	public void setErl(String erl) {
		this.erl = erl;
	}

}
