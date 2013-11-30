package de.sensorcloud.android.entitaet;

import java.util.ArrayList;

public class EventRegel {

	private Event event;
	private EventBenachrichtigung eventBen;
	private ArrayList<SensorEvent> sensorEvent;
	private ArrayList<EventAktion> eventAktion;

	
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public EventBenachrichtigung getEventBen() {
		return eventBen;
	}

	public void setEventBen(EventBenachrichtigung eventBen) {
		this.eventBen = eventBen;
	}

	public ArrayList<SensorEvent> getSensorEvent() {
		return sensorEvent;
	}

	public void setSensorEvent(ArrayList<SensorEvent> sensorEvent) {
		this.sensorEvent = sensorEvent;
	}

	public ArrayList<EventAktion> getEventAktion() {
		return eventAktion;
	}

	public void setEventAktion(ArrayList<EventAktion> eventAktion) {
		this.eventAktion = eventAktion;
	}

}
