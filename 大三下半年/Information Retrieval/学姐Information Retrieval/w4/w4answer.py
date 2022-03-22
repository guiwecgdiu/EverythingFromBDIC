

import porter

# data structure for stopwords
stopwords = set()

# open stopwords file and read
#   lines into the set
with open( 'stopwords.txt', 'r' ) as f:
	for line in f:
		stopwords.add( line.rstrip() )

# open NPL collection and read the documents
with open( 'npl-doc-text.txt', 'r' ) as f:
	alldocs = f.read().split( '   /' )[:-1]

# alldocs is now a list
#    each element is a document

# now we should prepare for processing
# create the stemmer
p = porter.PorterStemmer()

# initialise data structure
data = {}

# remember stemmed words
stemmed = {}

# process the documents
for doc in alldocs:
	# dictionary for terms & frequencies
	freq = {}

	# tokenise
	terms = doc.split()

	# get the docid
	docid = terms[0]

	# process the terms
	for term in terms[1:]:
		# only process if it's not a stopword
		if term not in stopwords:
			# perform stemming
			if term not in stemmed:
				stemmed[term] = p.stem( term )

			term = stemmed[term]

			# count the frequency
			# ... it's a new term
			if term not in freq:
				freq[term] = 1
			# ... it's not new
			else:
				freq[term] += 1

	# add freq to data
	# (easier to sort correctly later if
	# ... the docid is an int)
	data[int(docid)] = freq

# stage 2: print the frequencies
for docid in sorted(data.keys()): # <-- remove
	freq = data[docid]

	# sort the terms by their frequency
	# (most common term first)
	sorted_terms = sorted( freq, key=freq.get, reverse=True )

	# only print if highest frequency is >1
	if freq[ sorted_terms[0] ] > 1:
		print( '{}:'.format( docid ), end='' )

		# print the frequencies that are > 1
		i = 0
		while freq[ sorted_terms[ i ] ] > 1:
			print( ' {} ({})'.format(sorted_terms[i], freq[sorted_terms[i]]), end='')
			i += 1
		print()












