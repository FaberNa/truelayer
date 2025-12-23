#!/bin/sh

set -e


export INFO_JAVA_OPTIONS=$JAVA_OPTIONS


if [ -z "${JAVA_OPTIONS}" ]; then
	JAVA_OPTIONS="-XX:InitialRAMPercentage=25.0 -XX:MaxRAMPercentage=75.0 -XX:MaxMetaspaceSize=256M -XshowSettings:vm -XX:NativeMemoryTracking=summary -XX:SharedArchiveFile=app.jsa"
fi

java $JAVA_OPTIONS -jar app.jar