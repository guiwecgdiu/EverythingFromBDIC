#!/usr/bin/env python3

with open( 'words.txt', 'r' ) as f:
   for line in f:
      words = line.split();
      for word in words:
         print( '({})'.format( word ), end='' )
      print()