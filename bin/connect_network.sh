#!/bin/bash

# this script is autogenerated by 'ant startscripts'
# it starts a LAS2peer node providing the service 'i5.las2peer.services.demoService.DemoService' of this project
# pls execute it from the root folder of your deployment, e. g. ./bin/start_network.sh

java -cp "lib/*" i5.las2peer.tools.L2pNodeLauncher -c -p 9012 -b 134.61.172.204:9011 uploadStartupDirectory startService\(\'i5.las2peer.services.demoService.DemoService@1.0\'\) startWebConnector interactive
