#!/bin/sh

CPATH="plex-viewer.jar:../lib/javaplex.jar:../lib/ext/gluegen-rt.jar:../lib/ext/jogl-all.jar:../bin"
LPATH="../lib/ext/amd64/"
#LPATH=""

LD_LIBRARY_PATH=$LPATH java -cp $CPATH \
  -Djava.library.path=$LPATH -Djogamp.gluegen.UseTempJarCache=false \
  $@
