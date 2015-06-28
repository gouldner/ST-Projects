/**
 *  Thermostat On/Off Mapper
 *
 *  Copyright 2015 Ronald Gouldner
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Thermostat On/Off Mapper",
    namespace: "gouldner",
    author: "Ronald Gouldner",
    description: "Creates virtual switch which can be mapped to Cool, Heat or Dry",
    category: "Convenience",
    iconUrl: "http://baldeagle072.github.io/icons/standard-tile@1x.png",
    iconX2Url: "http://baldeagle072.github.io/icons/standard-tile@2x.png",
    iconX3Url: "http://baldeagle072.github.io/icons/standard-tile@3x.png")


preferences {
	section("Select the ZXT-120 Device... "){
		input "thermostat", "capability.Thermostat", title: "ZXT-120", required: false
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "RRG Updated with settings: ${settings}"
    log.debug "removing Subscriptions"
	unsubscribe()
	initialize()
}

def initialize() {
    if (thermostat) {        
        log.debug("device selected seting up child devices and subscriptions")
        def heatDeviceId = app.id + "Heat"
        def heatDevice = getChildDevice(heatDeviceId)
        if (!heatDevice) {
		    def heatLabel = thermostat.label + " Heat"
		    log.debug "Creating Device $heatLabel of type Stateless On/Off Button Tile"
            heatDevice = addChildDevice("gouldner", "Stateless On/Off Button Tile", heatDeviceId, null, [label: heatLabel])
        }
	    subscribe(heatDevice, "switch.on", heatOnHandler)
	    subscribe(heatDevice, "switch.off", heatOffHandler)
        
        def dryDeviceId = app.id + "Dry"
        def dryDevice = getChildDevice(dryDeviceId)
        if (!dryDevice) {
		    def dryLabel = thermostat.label + " Dry"
		    log.debug "Creating Device $dryLabel of type Stateless On/Off Button Tile"
            heatDevice = addChildDevice("gouldner", "Stateless On/Off Button Tile", dryDeviceId, null, [label: dryLabel])
        }
	    subscribe(dryDevice, "switch.on", dryOnHandler)
	    subscribe(dryDevice, "switch.off", dryOffHandler)
        
        def coolDeviceId = app.id + "Cool"
        def coolDevice = getChildDevice(coolDeviceId)
        if (!coolDevice) {
		    def coolLabel = thermostat.label + " Cool"
		    log.debug "Creating Device $coolLabel of type Stateless On/Off Button Tile"
            coolDevice = addChildDevice("gouldner", "Stateless On/Off Button Tile", coolDeviceId, null, [label: coolLabel])
        }
	    subscribe(coolDevice, "switch.on", coolOnHandler)
	    subscribe(coolDevice, "switch.off", coolOffHandler)
    } else {
        runIn(300, removeAllChildDevices)
    }
}

def uninstalled() {
}

def removeAllChildDevices() {
    removeChildDevices(getChildDevices())
}

private removeChildDevices(delete) {
    delete.each {
        deleteChildDevice(it.deviceNetworkId)
    }
}

def heatOnHandler(evt) {
    log.debug "setting $thermostat.label to Heat Mode"
	thermostat.heat()
}

def heatOffHandler(evt) {
    log.debug "setting $thermostat.label to Heat Off"
	thermostat.off()
}

def coolOnHandler(evt) {
    log.debug "setting $thermostat.label to Cool Mode"
	thermostat.dry()
}

def coolOffHandler(evt) {
    log.debug "setting $thermostat.label to Cool Off"
	thermostat.off()
}

def dryOnHandler(evt) {
    log.debug "setting $thermostat.label to Dry Mode"
	thermostat.dry()
}

def dryOffHandler(evt) {
    log.debug "setting $thermostat.label to Dry Off"
	thermostat.off()
}