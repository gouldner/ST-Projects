/**
 *  Set ZXT-120 Mode on Schedule
 *
 *  Author: Ronald Gouldner
 */
definition(
    name: "Set ZXT-120 Mode on schdeule V2",
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
	section("Select time of day Mon-Fri...") {
		input "time1", "time", title: "Time Mon-Fri ?"
	}
	section("Select time of day Sat-Sun...") {
		input "time2", "time", title: "Time Sat-Sun ?"
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
    setNextSchedule()
}

def scheduleCheck()
{
	log.trace "schedule check"
	changeMode()
	sendNotificationWithMode()
	setNextSchedule()
}

def setNextSchedule()
{
	unschedule()    
	def now=new Date()
	def tz = location.timeZone
	def dayString = now.format("EEE",tz)
	// TODO: Change to cron format to fix issue when earlier time changes to later time
	//       between two schedules causing duplication of event.  IE M-F 7am S-S 9am results in second 9AM fire on Friday since
	//       the ST schedule command only looks at the time and assumes you are scheduling for same day if time is in future
	//   NOTE: cron format is like quartz http://quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger
	//   Sec Min Hour DayOfMonth Month DayOfWeek Year (* = all, ?=no value)
	//   exmaple 7:30AM is "0 30 7 * * * ?"
	if (dayString.equals('Fri') || dayString.equals('Sat')) {
	    // Next event will be Sat or Sunday
	    log.debug "$dayString: Scheduling Sat-Sun $time2"
	    schedule(time2, "scheduleCheck")
	} else {
	    log.debug "$dayString: Scheduling Mon-Fri $time1"
	    schedule(time1, "scheduleCheck")
	}
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