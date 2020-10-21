#!/bin/bash

tty -s || lxterminal -e "$0" "$@"

webhook -hooks /home/pi/gnet/hooks.json -verbose

read -p "Here is your terminal Application (press Enter to quit):" reply



Did you copy the code I posted, word for word? Does your browser automatically run executable files? Is there an entry in ~/.profile to run the script when you log in? Does the .profile line look like this:

 (path to script) &

Note the ampersand at the end of the line.

Ah, I just looked at one of your pictures and you are using lxterminal. This terminal has a special requirement -- launch it this way:

  tty -s || lxterminal -e "$0" "$@"

See the added "-e" command?