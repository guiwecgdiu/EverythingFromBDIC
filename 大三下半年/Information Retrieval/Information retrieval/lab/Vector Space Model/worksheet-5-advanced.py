#!/usr/bin/env python3
##
#
# Sample solution to Advanced (a) in Worksheet 5.
# 
# Loads from npl-doc-text.txt and runs a query ("memories sequential capacities")
#    using vector model with binary weights.
#
# Query is intended to retrieve document 1.
#
##
import math
import porter

query = ['memories', 'sequential', 'capacities']
# query = ['systems', 'data', 'coding', 'information', 'transfer']

# load stopwords into appropriate data structure
stopwords = set()
with open( 'stopwords.txt', 'r' ) as f:
	for line in f:
		stopwords.add(line.rstrip())

# load the porter stemmer
stemmer = porter.PorterStemmer()

# open document collection

f = open( 'npl-doc-text.txt', 'r' )

# 'docs' will be a list of documents
docs = f.read().split('   /')

f.close()

vectors = {} # key is docid, value is dictionary (key is term, value is 1/0 weight)
lengths = {} # key is docid, value is vector length
stemmed = {}

# iterate through all the docs
for doc in docs:
	# divide this document into its terms
	terms = doc.split()
	if terms[0] != '/': # skip rubbish at the end
		# count terms in this doc
		docdict = {}
		length = 0 # will be stored in 'lengths' later
		# iterate terms in this doc
		for term in terms[1:]:
			# only do something if it's NOT a stopword
			if term not in stopwords:
				if term not in stemmed:
					stemmed[term] = stemmer.stem(term)
				term = stemmed[term]

				# first time I've seen 'term'
				if term not in docdict:
					length += 1
				docdict[term] = 1
			
			
		vectors[terms[0]] = docdict;
		lengths[terms[0]] = math.sqrt(length)

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

# print docids sorted by similarity (descending) - only show first 10
for did in sorted( sims, key=sims.get, reverse=True )[:10]:
	print( '{}: {}'.format( did, sims[did] ) )
