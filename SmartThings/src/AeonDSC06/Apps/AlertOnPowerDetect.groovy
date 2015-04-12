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
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    section("Select Power Meter to monitor...") {
        input name: "checkPowerMeter", type: "capability.powerMeter", title: "PowerMeter", multiple: false
	}
    section("Report power over or under...") {
        input name: "reportWhen", type: "enum", title:"Over/Under", required: true, options: ["over","under"], multiple: false
    }
    section("Power level to start reporting...") {
	    input name: "powerLimit", type: "number", title:"Power", required: true, defaultValue:10, multiple: false
	}
    section("Report every N Minutes ...") {
	    input name: "reportMin", type: "number", title:"Min", required: true, defaultValue:30, multiple: false
	}
    section("Debug Logging...") {
        input name: "debugOutput", type: "boolean", title: "Enable debug logging?", defaultValue: false
    }
}

def logDebugIfEnabled(message) {
    if (state.debugOutput) {
        log.debug "${message}"
    }
}

def installed() {
	logDebugIfEnabled("Installed with settings: ${settings}")

	initialize()
}

def updated() {
	logDebugIfEnabled("Updated with settings: ${settings}")

	unsubscribe()
	initialize()
}

def initialize() {
    logDebugIfEnabled("initialize called reportMin=${reportMin}")
	subscribe(checkPowerMeter, "power", powerCheck)
    state.reportedTime = 0
    state.debugOutput = ("true" == debugOutput)
}

def sendPowerNotification(message) {
    def reportedTime = state.reportedTime as int
    def now = new Date();
    def nowMinutes = Math.round(now.getTime() / 60000);
    
    logDebugIfEnabled("sendPowerNotification called nowMinutes=$nowMinutes reportedTime:$reportedTime")
    
    if ((nowMinutes - reportedTime) > reportMin) {
        logDebugIfEnabled("sending push message:${message}")
	    sendPush(message)      
        state.reportedTime = nowMinutes
    } else {
        logDebugIfEnabled("Message already reported reporting every:${reportMin}")
    }
}

def powerCheck(evt) {
    def meterValue = evt.value as double
    logDebugIfEnabled("powerCheck called powerLimit=$powerLimit meterValue=$meterValue")

    if (reportWhen == "over" ) {
        // If over report
        if (meterValue > powerLimit) {
            def message = "$checkPowerMeter reporting power over $powerLimit:${meterValue}"
            sendPowerNotification(message)
        }
    } else {
        // If Under report
        if (meterValue < powerLimit) {
            def message = "$checkPowerMeter reporting power under $powerLimit:${meterValue}"
            sendPowerNotification(message)
        }
    }
}