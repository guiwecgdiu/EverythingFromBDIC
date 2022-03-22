prin#!/usr/bin/env python3

allwords = {}

with open( 'words.txt', 'r' ) as f:
   for line in f:
      words = line.split();
      for word in words:
         word = word.lower()
         if word in allwords:
            allwords[word] += 1
         else:
            allwords[word] = 1

for w in sorted( allwords.keys(), key=allwords.get, reverse=True ):
   print( '{}: {}'.format( allwords[w], w ) )