GitHub Integration Info:
Owner: ajpri
Name: VPresPlus
Branch: master

Manual Install Instructions:

Go to the SmartThings IDE 62
Click “Create new device handler”
Click the “From Code” tab. Paste the GitHub code linked below. Click Create
Click "Publish, then “For me”
Go to “My Devices” and create a new device by clicking on “+ New Device”
Chose any name you want (Will show as than on app & other ST tools.
Choose any Device Network ID you want
Important: Select “Virtual Presence+” for Type and click Create!
Please read!:
I am an undergrad College Student. Automating my house has been a passion of mine for awhile now. Everything I’ve automated was paid for with my part-time job. Please donate with the link below. Every little bit helps!
paypal.me/ajpri 4
Bitcoin Address: 1pb7oUxjJnh1W9ztxqkWvdeckvtxnQ2ER







//Release History
//		1.0 May 20, 2016
//			Initial Release


metadata {
        definition (name: "Virtual Presence Plus", namespace: "ajpri", author: "Austin Pritchett") {
        capability "Switch"
        capability "Refresh"
        capability "Presence Sensor"
		capability "Sensor"
        
		command "arrived"
		command "departed"
    }

	// simulator metadata
	simulator {
	}

	// UI tile definitions
	tiles {
		standardTile("button", "device.switch", width: 2, height: 2, canChangeIcon: false,  canChangeBackground: true) {
			state "off", label: 'Away', action: "switch.on", icon: "st.Kids.kid10", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'Present', action: "switch.off", icon: "st.Kids.kid10", backgroundColor: "#53a7c0", nextState: "off"
		}
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}
        standardTile("presence", "device.presence", width: 1, height: 1, canChangeBackground: true) {
			state("present", labelIcon:"st.presence.tile.mobile-present", backgroundColor:"#53a7c0")
			state("not present", labelIcon:"st.presence.tile.mobile-not-present", backgroundColor:"#ffffff")
		}
		main (["button", "presence"])
		details(["button", "presence", "refresh"])
	}
}

def parse(String description) {
	def pair = description.split(":")
	createEvent(name: pair[0].trim(), value: pair[1].trim())
}

// handle commands
def arrived() {
	on()
}


def departed() {
    off()
}

def on() {
	sendEvent(name: "switch", value: "on")
    sendEvent(name: "presence", value: "present")

}

def off() {
	sendEvent(name: "switch", value: "off")
    sendEvent(name: "presence", value: "not present")

}