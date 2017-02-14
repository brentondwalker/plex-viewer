#!/bin/sh

CPATH="plex-viewer.jar:../lib/javaplex.jar"

java -cp $CPATH \
  edu.stanford.math.plex_viewer.PovGenerator \
  $@
