#!/usr/bin/env python3
##
#
# Sample solution to Worksheet 7.
# 
# Runs a query against 3 simple short documents using vector model with TF-IDF weights.
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


idfs = {} # key is term, value is IDF of that term
vectors = {} # key is docid, value is dictionary (key is term, value is 1/0 weight)
lengths = {} # key is docid, value is vector length

# calculate vectors
# for the moment, "idfs" will just have a value that is the number of documents
#    the term appears in (i.e. n_i)
for did in documents:
	freq = {} # will be stored in 'vectors' later
	maxfreq = 0
	for term in documents[did]: # iterate all terms
		term = term.lower()
		if term not in stopwords:
			term = stemmer.stem(term)
			if term in freq:
				freq[term] += 1 # update weight if it's in the document
			else:
				freq[term] = 1
				if term in idfs:
					idfs[term] += 1
				else:
					idfs[term] = 1
			if freq[term] > maxfreq:
				maxfreq = freq[term]

	# divide all frequencies by the maximum frequency for this document
	for term in freq:
		freq[term] /= maxfreq

	# store the TFs in vectors (we will deal with IDFs later)
	vectors[did] = freq

# now we have processed all the documents, we can fix the IDFs
for term in idfs:
	idfs[term] = math.log( len(documents)/idfs[term],2)

# now we can do all the weights for all documents (and calculate document lengths)
for did in vectors:
	length = 0
	for term in vectors[did]:
		vectors[did][term] = vectors[did][term] * idfs[term]
		length += math.pow(vectors[did][term],2)
	lengths[did] = math.sqrt(length)

# do the same for the query
query_vector = {}
query_length = 0
maxfreq_query = 0
for term in query:
	if term not in stopwords:
		term = stemmer.stem(term)
		if term not in query_vector:
			query_vector[term] = 1
		else:
			query_vector[term] += 1
		if query_vector[term] > maxfreq_query:
			maxfreq_query = query_vector[term]

for term in query_vector:
	query_vector[term] = ( query_vector[term] / maxfreq_query ) * idfs[term]
	query_length += math.pow(query_vector[term],2)

query_length = math.sqrt(query_length)

# calculate similarities
sims = {} # key is docid, value is similarity to query
for did in vectors:
	sim = 0
	# numerator of cosine function
	for term in query_vector:
		if term in vectors[did]:
			sim += query_vector[term] * vectors[did][term]
	sim = sim / ( lengths[did] * query_length ) # divide by product of vector lengths
	sims[did] = sim # add similarity score

# print docids sorted by similarity (descending)
for did in sorted( sims, key=sims.get, reverse=True ):
	print( '{}: {}'.format( did, sims[did] ) )