#!/usr/bin/env python3
##
#
# Sample solution to Worksheet 5.
# 
# Runs a query against 3 simple short documents using vector model with binary weights.
#
##

import math
import porter

documents = {
	'd1':['Shipment', 'of', 'gold', 'damaged', 'in', 'a', 'fire'],
	'd2':['Delivery', 'of', 'silver', 'arrived', 'in', 'a', 'silver', 'truck'],
	'd3':['Shipment', 'of', 'gold', 'arrived', 'in', 'a', 'large', 'truck']
	}
query = ['gold', 'silver', 'truck']

# load stopwords into appropriate data structure
stopwords = set()
with open( 'stopwords.txt', 'r' ) as f:
	for line in f:
		stopwords.add(line.rstrip())

# load the porter stemmer
stemmer = porter.PorterStemmer()

vectors = {} # key is docid, value is dictionary (key is term, value is 1/0 weight)
lengths = {} # key is docid, value is vector length

# calculate vectors
for did in documents:
	freq = {} # will be stored in 'vectors' later
	length = 0 # will be stored in 'lengths' later
	for term in documents[did]: # iterate all terms
		term = term.lower()
		if term not in stopwords and term not in freq: # don't count terms twice
			freq[term] = 1 # update weight if it's in the document
			length += 1 # mathematically, this is 1 squared, but no point in putting that in the code
	vectors[did] = freq
	lengths[did] = math.sqrt(length)

# do the same for the query
query_vector = {}
query_length = 0
for term in query:
	if term not in stopwords:
		term = stemmer.stem(term)
		query_vector[term] = 1
		query_length += math.pow(1,2)
query_length = math.sqrt(query_length)

# calculate similarities
sims = {} # key is docid, value is similarity to query
for did in vectors:
	sim = 0
	# numerator of cosine function
	for term in query_vector:
		if term in vectors[did]:
			sim += query_vector[term] * vectors[did][term]
	sim /= lengths[did] * query_length # divide by product of vector lengths
	sims[did] = sim # add similarity score

# print docids sorted by similarity (descending)
for did in sorted( sims, key=sims.get, reverse=True ):
	print( '{}: {}'.format( did, sims[did] ) )