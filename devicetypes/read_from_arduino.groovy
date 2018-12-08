/**
 *  Read From Arduino
 *
 *  Author: urman
 *  Date: 2013-03-14
 *  Revision 2014-07-10
 */
 // for the UI

metadata {

	definition (name: "Read A String", author: "urman") {
    		capability "refresh"
	}
  // simulator metadata
	simulator {
		for (int i = 0; i <= 100; i += 5) {
        	def str = i.toString().collect{new BigInteger(new Integer((byte)it).toString()).toString(16)}.join('')
			status "${i}": "catchall: 0104 0000 01 01 0040 00 0A21 00 00 0000 0A 00 0A${str}2E30"
		}
	}

	// UI tile definitions
	tiles {
		valueTile("refresh", "device.refresh") {
			state("refresh", label:'${currentValue}',
				
			)
		}
		main "refresh"
		details "refresh"
	}
}

// Parse incoming device messages to generate events
def parse(String description) {
	log.trace description
	def result = createEvent(name: "refresh", value: zigbee.parse(description)?.text as Double)
    log.debug result?.descriptionText
    return result
}

