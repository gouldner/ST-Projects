/**
 *  TestSlider
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
metadata {
	definition (name: "TestSlider", namespace: "gouldner", author: "Ronald Gouldner") {
		command "setTestValue"
		command "reportTestValue"
        command "setTestValue2"
		command "reportTestValue2"
		attribute "testValue", "NUMBER"
		attribute "testValue2", "NUMBER"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		valueTile("value", "device.testValue", inactiveLabel: false, decoration: "flat") {
			state "value", action:"reportTestValue", label:'${currentValue}', unit:""
		}
		controlTile("valueSliderControl", "device.testValue", "slider", height: 1, width: 2, inactiveLabel: false, range:"(67..84)") {
			state "valueSliderControl", action:"setTestValue", backgroundColor:"#d04e00"
		}
		valueTile("value2", "device.testValue2", inactiveLabel: false, decoration: "flat") {
			state "value2", action:"reportTestValue2", label:'${currentValue}', unit:""
		}
		controlTile("value2SliderControl", "device.testValue2", "slider", height: 1, width: 2, inactiveLabel: false) {
			state "value2SliderControl", action:"setTestValue2", backgroundColor:"#00ff00"
		}
		
		main (["value"])
		details(["value", "valueSliderControl","value2","value2SliderControl"])
	}
}

def setTestValue(int val) {
	log.debug "setTestValue called $val"
    sendEvent("name":"testValue", "value":val)
}

def reportTestValue() {

	log.debug "reportTestValue called"
	
	def testValue = device.currentValue("testValue")
	
	if (testValue > 83) {
		sendEvent("name":"testValue", "value":67)
	} else {
		sendEvent("name":"testValue", "value":testValue+1)
	}
}

def setTestValue2(int val) {
	log.debug "setTestValue2 called $val"
    sendEvent("name":"testValue2", "value":val)
}

def reportTestValue2() {

	log.debug "reportTestValue2 called"
	
	def testValue = device.currentValue("testValue2")
	
	if (testValue > 83) {
		sendEvent("name":"testValue2", "value":67)
	} else {
		sendEvent("name":"testValue2", "value":testValue+1)
	}
}
	

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}