/**
 *  Set ZXT-120 Mode on Schedule
 *
 *  Author: Ronald Gouldner
 */
definition(
    name: "Set ZXT-120 Mode on schdeule",
    namespace: "gouldner",
    author: "Ronald Gouldner",
    description: "Set ZXT-120 Mode based on time setting.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/temp_thermo-switch.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/temp_thermo-switch@2x.png"
)

preferences {
	section("Select the ZXT-120 Device... "){
		input "thermostat", "capability.Thermostat", title: "ZXT-120"
	}
	section("Change Mode at...") {
		input "time1", "time", title: "When?"
	}
	section("Set Mode"){
		input "mode", "enum", title: "Mode?", options: ["heat","cool","off","dry"]
	}
    section("Notify with Push Notification"){
		input "pushNotify", "enum", title: "Send Push notification ?", options: ["yes","no"]
	}
}

def installed()
{
	schedule(time1, "scheduleCheck")
}

def updated()
{
	unschedule()
	schedule(time1, "scheduleCheck")
}

def scheduleCheck()
{
	log.trace "schedule check"
	changeMode()
	sendNotificationWithMode()
}

private changeMode()
{
	log.debug "Scheduled Mode Change to $mode"
	
	if (mode == "cool") {
		log.debug "Turning on cool mode"
		thermostat.cool()
	}
	if (mode == "heat") {
		log.debug "Turning on cool mode"
		thermostat.heat()
	}
	if (mode == "off") {
		log.debug "Turning on cool mode"
		thermostat.off()
	}
	if (mode == "dry") {
		log.debug "Turning on cool mode"
		thermostat.dry()
	}
}

private sendNotificationWithMode() {
	if (pushNotify == "yes") {
		sendPush("${thermostat.displayName} mode changed to $mode")
	}
}