#!/usr/bin/env python3

with open( 'words.txt', 'r' ) as f:
   for line in f:
      print( line.strip() )