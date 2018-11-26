# Yamaha MusicCast binding for OpenHab 2.x

Binding which allows to switch MusicCast Device ON / OFF and set the Volume.

## Getting started

To start using this binding, you just need to install this binding on an OpenHab installation. We have tested this binding on OpenHab 2.3 and higher. 

## Prerequisites

You just need two things:
	* an OpenHab 2.x installation
	* a compatible Yamaha MusicCast speaker. We have tested this binding successfully on a WX-010, WX-030 and YSP-1600 soundbar. Yamaha may always provide me with more equipment to test it on ;)

## Configuration

Once you have installed this binding, you can add a MusicCast thing  (e.g. through habmin, paper-UI, ... ). 

There are two configuration settings:

* **host** the IP-adress or hostname of the speaker
* **refresh** refresh interval. This is interval between two update requests from OpenHab towards the speaker. I currently use 2 seconds, but it depends on your requirements.

## Channels 

Currently, the following channels are available:

* **Power** Channel indicating the speaker is on or off
* **Volume** Current sound volume (percentage of maximum)
* **Mute** Switch indicating the speaker is muted or not.
