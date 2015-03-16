/**
 *  Fitbit Data Device
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
 
preferences {
    input("user", "text", title: "Fitbut User")
    
}
metadata {
	definition (name: "Fitbit Device", namespace: "gouldner", author: "Ronald Gouldner") {
    capability "Refresh"
	capability "Polling"
        
    attribute "weight", "STRING"     
    fingerprint deviceId: "RRGfitbit"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
            valueTile("weight", "device.weight") {
   	         state("weight", label: '${currentValue}Lbs', unit:"Lbs", backgroundColors: [
                    [value: 120, color: "#bc2323"],
                    [value: 130, color: "#d04e00"],
                    [value: 140, color: "#f1d801"],
                    [value: 150, color: "#90d2a7"],
		            [value: 160, color: "#44b621"],
                    [value: 170, color: "#1e9cbb"],
                    [value: 180, color: "#153591"]
    	            ]
            	)
        	}
   

            standardTile("refresh", "device.energy_today", inactiveLabel: false, decoration: "flat") {
                state "default", action:"polling.poll", icon:"st.secondary.refresh"
            }

        
        main (["weight"])
        details(["weight","refresh"])

	}
}


// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"

}

def poll() {
	refresh()
}

def refresh() { 
  log.debug "Executing 'refresh'"
  
  def cmd = "https://api.fitbit.com/1/${settings.user}/-/body/log/weight/date/2015-03-05.json";
  log.debug "Sending request cmd[${cmd}]"
  
  httpGet(cmd) {resp ->
        if (resp.data) {
        	log.debug "${resp.data}"
        }
        if(resp.status == 200) {
            	log.debug "poll results returned"
        }
         else {
            log.error "polling children & got http status ${resp.status}"
        }
    }
}
