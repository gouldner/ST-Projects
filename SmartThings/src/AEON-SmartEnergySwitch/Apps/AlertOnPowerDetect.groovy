/**
 *  Alert every x min when power detected
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
    name: "Alert every x min when power detected",
    namespace: "gouldner",
    author: "Ronald Gouldner",
    description: "When power is detected generate notification every x min.",
    category: "Green Living",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    section("Select Power Meter to monitor...") {
        input name: "checkPowerMeter", type: "capability.powerMeter", title: "PowerMeter", multiple: false
	}
    section("Wattage level to start reporting...") {
	    input name: "wattageLevel", type: "number", title:"Watts", defaultValue:10, multiple: false
	}
    section("Report every N Minutes (<60)...") {
	    input name: "reportMin", type: "number", title:"Min", defaultValue:30, multiple: false
	}
}

def installed() {
	log.debug("Installed with settings: ${settings}")

	initialize()
}

def updated() {
	log.debug("Updated with settings: ${settings}")

	//unsubscribe()
    unschedule()
	initialize()
}

def initialize() {
    log.debug("initialize called reportMin=${reportMin}")
	// TODO: subscribe to attributes, devices, locations, etc.
    // Is there an event for PowerLevel changes ?  Not sure So I have to just poll the
    // device on my own.  I wish Smart Things had better documentation
    
    // Check if preferences are correctly set and schedule
    if (checkPowerMeter != null && reportMin != null && reportMin > 0) {
        log.debug("Scheduling powerCheck every ${reportMin} min")
        def sched = "0 0/${reportMin} * * * ?"
        schedule(sched, powerCheck)
    } else {
        log.warn("${app.label} not configured correctly")
        log.debug("checkPowerMeter=${checkPowerMeter}")
        log.debug("reportEveryMin=${reportMin}")
        if (checkPowerMeter == null) {
            log.warn("Power Meter not configured")
        }
        if (reportMin == null) {
            log.warn("Report every N Min not configured")
        }
        if (reportMin <=0 ) {
            log.warn("Report every N Min not > 0")
        }
    }
}

// TODO: implement event handlers

def powerCheck() {
    log.debug("powerCheck called")
    
    // Refresh since device polling doesn't work correctly
    checkPowerMeter.refresh()
    
    // issued refresh so give some time to let it update
    // Run in 60 seconds
    runIn(60,powerCheckNow)
}

def powerCheckNow() {
    log.debug("powerCheckNow called")
    log.debug("checkPowerMeter.currentPower=${checkPowerMeter.currentPower}")
    log.debug("wattageLevel=${wattageLevel}")
    if (checkPowerMeter.currentPower > wattageLevel ) {
        sendNotificationWithPowerLevel()
    }
}

private sendNotificationWithPowerLevel() {
    log.debug("sendNotificationWithPowerLevel called")
	sendPush("Power in use by ${checkPowerMeter.name}:${checkPowerMeter.currentPower}")
}
