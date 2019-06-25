# Create your views here.
from __future__ import unicode_literals

from django.shortcuts import render

import time
import RPi.GPIO as GPIO
from django.http import HttpResponse

def auto_mode(request):
	sensor = 7
	GPIO.setmode(GPIO.BCM)
	GPIO.setup(sensor,GPIO.IN)
	GPIO.setup(13,GPIO.OUT)
	GPIO.setup(17,GPIO.OUT)
	cnt=1
	while True:
		if GPIO.input(sensor):
			GPIO.output(13,GPIO.HIGH) #Light
			GPIO.output(17,GPIO.HIGH) #AC
			if cnt == 1:
				return HttpResponse('someone is in the room so turned on light and AC')
				cnt=2;
		else:
			GPIO.output(13,GPIO.LOW)
			GPIO.output(17,GPIO.LOW)
			if cnt == 2:
				return HttpResponse('no one is in the room so turned off light and AC')
				cnt=3;


def manual_mode(request):
	return render_to_response('abcd/menu.html')


def turn_light_on(request):
	GPIO.setmode(GPIO.BCM)
	GPIO.setup(13,GPIO.OUT)
	GPIO.output(13,GPIO.HIGH)


def turn_light_off(request):
	GPIO.setmode(GPIO.BCM)
	GPIO.setup(13,GPIO.OUT)
	GPIO.output(13,GPIO.LOW)


def turn_ac_on(request):
	GPIO.setmode(GPIO.BCM)
	GPIO.setup(13,GPIO.OUT)
	GPIO.output(13,GPIO.HIGH)


def turn_ac_off(request):
	GPIO.setmode(GPIO.BCM)
	GPIO.setup(17,GPIO.OUT)
	GPIO.output(17,GPIO.LOW)


def cm(request):
	return render_to_response('templates/index.html')



def light(request):
	GPIO.setmode(GPIO.BCM)
	GPIO.setup(12,GPIO.OUT)
	GPIO.setup(16,GPIO.OUT)
	GPIO.setup(20,GPIO.OUT)
	GPIO.setup(21,GPIO.OUT)	
	while True:
		for i in range(0,16):
			x = str(bin(i))
			if x[0] == 1:
				GPIO.setup(12,GPIO.HIGH)
			else:
				GPIO.setup(12,GPIO.LOW)
			
		
			if x[1] == 1:
				GPIO.setup(16,GPIO.HIGH)
			else:
				GPIO.setup(16,GPIO.LOW)

			if x[2] == 1:
					GPIO.setup(20,GPIO.HIGH)
				else:
					GPIO.setup(20,GPIO.LOW)
			if x[3] == 1:
					GPIO.setup(21,GPIO.HIGH)
				else:
					GPIO.setup(21,GPIO.LOW)
			time.sleep(10)

		


