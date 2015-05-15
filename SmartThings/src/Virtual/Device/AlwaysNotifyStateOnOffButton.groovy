/**
 *  Stateless On/Off Button Tile
 *
 *  Author: SmartThings
 *
 *  Date: 2015-05-14
 */
metadata {
	// Automatically generated. Make future change here.
	definition (name: "Always Notify State On/Off Button Tile", namespace: "gouldner", author: "Ronald Gouldner") {
		capability "Actuator"
		capability "Switch"
		capability "Sensor"
	}

	// simulator metadata
	simulator {
	}

	// UI tile definitions
	tiles {
		standardTile("button", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "off", label: 'Off', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'On', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821", nextState: "off"
		}
		standardTile("on", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "on", label: 'On', action: "switch.on", icon: "st.switches.switch.on", backgroundColor: "#79b821"
		}
		standardTile("off", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "off", label: 'Off', action: "switch.off", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
		}
		main "button"
		details "button"
	}
}

def parse(String description) {
}

def on() {
    log.debug "Always Notify State On/Off Button Tile Virtual Switch ${device.name} turned on"
    sendEvent(name: "switch", value: "on", isStateChange: true)
}

def off() {
    log.debug "Always Notify State On/Off Button Tile Virtual Switch ${device.name} turned off"
    sendEvent(name: "switch", value: "off", isStateChange: true)
}