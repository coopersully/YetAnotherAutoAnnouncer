<p align="center">
  <img src="https://i.imgur.com/UZUFZBh.png">
</p>

# Y.A.A.A. (Yet Another Auto-Announcer)

## About
### Give me the sparknotes.
Y.A.A.A. (Yet Another Auto-Announcer) is just what it implies- an auto-announcer for paper servers. It's designed to be lightweight
and easy to use out of the box. Paste json-formatted messages in the config file (anything you can do in /tellraw works here) and
start your server. That's it.

### Tell me about it.
Y.A.A.A. (Yet Another Auto-Announcer) is a plugin for Paper servers that enables automated broadcasting of custom messages within 
the game at specified intervals. Users can craft their unique announcements using JSON formatting, similar to the /tellraw command 
in Minecraft, allowing for colored text and other text formatting options. Announcements can be set to broadcast at regular 
intervals or in a randomized order. Additionally, each announcement can be accompanied by a sound from Minecraft's sound event 
list to catch players' attention. Admins have the ability to reload the plugin's configuration on the fly, check when the next 
announcement will be broadcasted, and view all messages or a specific message from the config as they would appear in-game. Any 
issues encountered while loading messages are logged and saved for easy troubleshooting. Through a simple setup process, Y.A.A.A. 
provides a straightforward way to keep players informed and engaged with server updates, rules, or any other important information.

## F.A.Q.
### I don't know how to format my announcements!
Use a tellraw-generator like [this one](https://www.minecraftjson.com/), and just slice off the command prefix and use the data in brackets. That will work just fine.

### Can I add links to announcements?
Sure can. Do something like this:
```json
{"text":"Hello world!","clickEvent":{"action":"open_url","value":"https://coopersully.me/"}}
```

### Can I add permissions?
No.

### Does this plugin work with \<other plugin\>?
Short answer? Probably. I can't test it against every plugin out there, but the plugin is extremely light-weight and shouldn't step on any toes.


### Can you add \<feature\>?
Feel free to suggest features in [the issues tab on this plugin's GitHub.](https://github.com/coopersully/yetanotherautoannouncer/issues/). Make sure someone hasn't suggested it before you do, and you're all set. Just let me know what you want added and I can try my best to fulfill your request. I am a full-time college student, though, so please be patient. :)

### It's not working! I keep getting errors.
Chances are, you've configured an announcement or two incorrectly. There's some built-in error support, though! Check the plugin's config folder. 
If an error occurs, the plugin should create an ``errors/`` folder and spit out some technical mumbo jumbo to help you through it. If all else 
fails- Google it. If you believe it to be an issue with the plugin itself, feel free to send in a bug report in [the issues tab on this plugin's GitHub.](https://github.com/coopersully/yetanotherautoannouncer/issues/) But, be warned I am a full-time college student, so it may take a few days for me to get to your issue.
