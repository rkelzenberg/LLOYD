/**
 *  Turn On Light When Door Opens
 *
 *  Copyright 2014 Matt Nohr
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
    name: "Turn On Light When Door Opens",
    namespace: "mrnohr",
    author: "Matt Nohr",
    description: "Turn on a light when a door opens",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    page(name: "mainPage", title: "Turn On Light When It Opens", nextPage: "selectPhrase", uninstall:true) {
        section("When the door opens..."){
            input "contact1", "capability.contactSensor", title: "Where?"
        }
        section("Turn on a light..."){
            input "switches", "capability.switch", multiple: true
        }
        section("Turn it off after this many minutes"){
            input "offMinutes", "number", required: false
        }
        section("Only during a certain time"){
            input "starting", "time", title: "Starting", required: false
            input "ending", "time", title: "Ending", required: false
        }
    }
    page(name: "notificationPage", title: "Notifications", install: true) {
        section("Notification method") {
            input "pushNotification", "bool", title: "Push notification", required: false
            input "phone", "phone", title: "Text message at", description: "Tap to enter phone number", required: false
            input "notificationDelay", "number", title: "Send at most 1 message every X minutes", required: false
        }
    }
    page(name: "selectPhrase")
}

def selectPhrase() {
    dynamicPage(name: "selectPhrase", title: "Invoke a Hello Home Action", nextPage: "notificationPage") {
        def phrases = location.helloHome?.getPhrases()*.label
        section("Hello Home Actions") {
            input "phraseToExecute", "enum", title: "Phrase", required: false, options: phrases
        }
    }
}

def installed() {
    log.debug "Installed with settings: ${settings}"

    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"

    unsubscribe()
    unschedule()
    initialize()
}

def initialize() {
    // TODO: subscribe to attributes, devices, locations, etc.
    subscribe(contact1, "contact.open", contactOpenHandler)
}

// TODO: implement event handlers
def contactOpenHandler(evt) {
    log.debug "$evt.value: $evt, $settings"
    log.info "Turning on switches: $switches"
    if(timeOk) {
        switches.on()

        if(phraseToExecute) {
            log.info "Executing $phraseToExecute"
            location.helloHome.execute(phraseToExecute)
        }

        if(offMinutes) {
            runIn(offMinutes * 60, "scheduledTurnOff")
        }

        //send message if none in past X minutes
        def recentNotification = false
        if(notificationDelay) {
            def timeAgo = new Date(now() - notificationDelay * 60 * 1000)
            def recentEvents = contact1.eventsSince(timeAgo)
            recentNotification = recentEvents.count { it.value && it.value == "open" } > 1
        }

        if((pushNotification || phone) && !recentNotification) {
            def msg = "You $contact1 is open"
            log.info "Sending notification $msg"
            if (pushNotification) {
                sendPush(msg)
            }
            if (phone) {
                sendSms(phone, msg)
            }
        } else {
            log.info "Not sending notification"
        }
    } else {
        log.info "Not during the correct time"
    }
}

def scheduledTurnOff() {
    if(contact1.latestValue("contact") == "closed") {
        log.info "Turning off switches after $offMinutes: $switches"
        switches.off()
    } else {
        log.info "Not turning off because the door is still open"
    }
}

private getTimeOk() {
    def result = true
    if (starting && ending) {
        def currTime = now()
        def start = timeToday(starting).time
        def stop = timeToday(ending).time
        result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
    }
    log.debug "timeOk = $result"
    result
}
