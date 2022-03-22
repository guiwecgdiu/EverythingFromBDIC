#!/usr/bin/env python3
import porter

# load stopwords into appropriate data structure (a set)
stopwords = set()
with open( 'stopwords.txt', 'r' ) as f:
	for line in f:
		stopwords.add(line.rstrip())

# load the porter stemmer
stemmer = porter.PorterStemmer()

# open the document collection
docno = 0
f = open( 'npl-doc-text.txt', 'r' )

docs = f.read().split( '   /\n' )

# close the file
f.close()

# set up dictionary to store document frequencies:
# key is docid
# value is a dictionary:
#   key is term
#   value is frequency within that document
dictionary = {}

# making dictionary
for doc in docs:
	terms = doc.split();
	if terms[0] != '/':
		docdict = {}
		for term in terms[1:]:
			if term not in stopwords:
				term = stemmer.stem(term)
				if term not in docdict:
					docdict[term] = 1
				else:
					docdict[term] += 1
		dictionary[terms[0]] = docdict

# now we have stored everything in the data structure, now print the details.
for docid in dictionary:
	sorted_keys = sorted( dictionary[docid], key=dictionary[docid].get, reverse=True )
	if dictionary[docid][sorted_keys[0]] > 1:
		print( '{}:'.format(docid), end='' )
		i = 0;
		while dictionary[docid][sorted_keys[i]] > 1: 
			print( ' {} ({})'.format( sorted_keys[i], dictionary[docid][sorted_keys[i]] ), end='' )
			i += 1
		print()
