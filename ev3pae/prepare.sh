#!/bin/bash

clear

killall -9 roscore
wait
killall -9 rosmaster
wait
source devel/setup.bash
wait
catkin_make
wait
roscore &
