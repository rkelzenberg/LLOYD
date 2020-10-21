#!/bin/bash
# Script: Ba.sh
# GitHub: GRClark
# Pushbullet curl script

curl -u o.tG8tHoZTjjWpjS6NcmpUeKDVIp2tUYnD: -X POST https://api.pushbullet.com/v2/pushes --header 'Content-Type: application/json' --data-binary '{"type": "note", "title": "Note Title", "body": "Note Body"}'