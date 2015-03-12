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
    input("user", "text", title: "Fitbit User")
    input("fitbit_auth_pin", "text", title: "Fitbit Access Auth Pin")
    input("fitbit_auth_token", "text", title: "Fitbit oauth token")
    input("fitbit_auth_token_secret", "text", title: "Fitbit oauth token secret")
    input("fitbit_dev_smarthings_app_client_consumer_key", "text", title: "Fitbit registered app client consumer key")
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
	//refresh()
}




def OAuthGet() {

/* 
   code found here https://code.google.com/p/oauth-signpost/wiki/TwitterAndSignpost
*/ 

/*
	    OAuthConsumer consumer = new DefaultOAuthConsumer(
                // the consumer key of this app (replace this with yours)
                "iIlNngv1KdV6XzNYkoLA",
                // the consumer secret of this app (replace this with yours)
                "exQ94pBpLXFcyttvLoxU2nrktThrlsj580zjYzmoM");

        OAuthProvider provider = new DefaultOAuthProvider(
                "http://twitter.com/oauth/request_token",
                "http://twitter.com/oauth/access_token",
                "http://twitter.com/oauth/authorize");
 */               
        /****************************************************
         * The following steps should only be performed ONCE
         ***************************************************/
/*
        // we do not support callbacks, thus pass OOB
        String authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);

        // bring the user to authUrl, e.g. open a web browser and note the PIN code
        // ...         

        String pinCode = // ... you have to ask this from the user, or obtain it
        // from the callback if you didn't do an out of band request

        // user must have granted authorization at this point
        provider.retrieveAccessToken(consumer, pinCode);

        // store consumer.getToken() and consumer.getTokenSecret(),
        // for the current user, e.g. in a relational database
        // or a flat file
        // ...
*/
        /****************************************************
         * The following steps are performed everytime you
         * send a request accessing a resource on Twitter
         ***************************************************/
/*
        // if not yet done, load the token and token secret for
        // the current user and set them
        consumer.setTokenWithSecret(ACCESS_TOKEN, TOKEN_SECRET);

        // create a request that requires authentication
        URL url = new URL("http://twitter.com/statuses/mentions.xml");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        // sign the request
        consumer.sign(request);

        // send the request
        request.connect();

        // response status should be 200 OK
        int statusCode = request.getResponseCode();
        */
}

def refresh() { 

  log.debug "Executing 'refresh'"
  
  def fetchDate="2015-03-05"
  def oauthNonce=(int) (Math.random() * 100000000)
  def now=new Date()
  def timeStamp=now.toTimestamp()
  
  /* Get Sample
  def pollParams = [
    uri: "https://api.ecobee.com",
    path: "/1/thermostat",
    headers: ["Content-Type": "text/json", "Authorization": "Bearer ${atomicState.authToken}"],
    query: [format: 'json', body: jsonRequestBody]
    
    */
  
  /*
  def bodyDataPollParams = [
    uri: "https//api.fitbit.com",
    path: "/1/user/${settings.user}/body/date/${fetchDate}.json",
    headers: ["Content-Type": "text/json", "Authorization: OAuth oauth_consumer_key="${settings.fitbit_dev_smarthings_app_client_consumer_key}", oauth_nonce="${oauthNonce}", oauth_signature="1gqjxxh1dWGExCe8NH%2Fo%2BTfadOk%3D", oauth_signature_method="HMAC-SHA1", oauth_timestamp="${timeStamp}", oauth_token="${settings.fitbit_auth_token}", oauth_version="1.0"],
    query: [format: 'json', body: jsonRequestBody]
    
    */
  
  def cmd = "https//api.fitbit.com/1/user/${settings.user}/body/date/${fetchDate}.json&oauth_consumer_key=${settings.fitbit_dev_smarthings_app_client_consumer_key}&oauth_nonce=${randomText}&oauth_signature_method=HMAC-SHA1&oauth_timestamp=${timeStamp}&oauth_token=${settings.fitbit_auth_token}&oauth_version=1.0";
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
