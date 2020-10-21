#!/bin/bash
# Script: Pushover Ba.sh
# GitHub: GRClark
# Pushover curl script

curl --form-string  "token=at97iqswzkxbcd4p9k22bkcg3nn9sf" --form-string "user=uxmsj5a28waic3azbdtqi1cp655cth" --form-string "message=test" https://api.pushover.net/1/messages.json